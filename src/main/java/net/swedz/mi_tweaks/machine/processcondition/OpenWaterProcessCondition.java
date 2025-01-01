package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.tesseract.neoforge.api.WorldPos;
import net.swedz.tesseract.neoforge.tooltip.Parser;
import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record OpenWaterProcessCondition(Relative relative, int range, float fill) implements MachineProcessCondition
{
	public static final MapCodec<OpenWaterProcessCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
			.group(
					Relative.CODEC.fieldOf("relative").forGetter(OpenWaterProcessCondition::relative),
					Codec.intRange(1, 16).fieldOf("range").forGetter(OpenWaterProcessCondition::range),
					Codec.floatRange(0, 1).optionalFieldOf("fill", 1f).forGetter(OpenWaterProcessCondition::fill)
			)
			.apply(instance, OpenWaterProcessCondition::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenWaterProcessCondition> STREAM_CODEC = StreamCodec.composite(
			Relative.STREAM_CODEC,
			OpenWaterProcessCondition::relative,
			ByteBufCodecs.INT,
			OpenWaterProcessCondition::range,
			ByteBufCodecs.FLOAT,
			OpenWaterProcessCondition::fill,
			OpenWaterProcessCondition::new
	);
	
	private static final Cache<WorldPos, Boolean> OPEN_WATER_CACHE = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build();
	
	@Override
	public boolean canProcessRecipe(Context context, MachineRecipe recipe)
	{
		var level = context.getLevel();
		long currentGameTime = level.getGameTime();
		var pos = new WorldPos(level, context.getBlockEntity().getBlockPos());
		Boolean value = OPEN_WATER_CACHE.getIfPresent(pos);
		if(value == null)
		{
			value = this.calculateOpenWater(pos);
			OPEN_WATER_CACHE.put(pos, value);
		}
		return value;
	}
	
	private boolean calculateOpenWater(WorldPos pos)
	{
		var result = this.countWater(pos);
		int areaNeeded = Math.round(result.total() * fill);
		return result.water() >= areaNeeded;
	}
	
	private WaterCountResult countWater(WorldPos origin)
	{
		var blocks = StreamSupport.stream(relative.blocks(origin.pos(), range).spliterator(), false)
				.map(BlockPos::immutable)
				.filter((pos) -> !pos.equals(origin.pos()))
				.collect(Collectors.toCollection(Lists::newArrayList));
		long total = blocks.size();
		long water = blocks.stream()
				.map((pos) -> new WorldPos(origin.level(), pos))
				.filter(this::isWater)
				.count();
		return new WaterCountResult(total, water);
	}
	
	private record WaterCountResult(long total, long water)
	{
	}
	
	private boolean isWater(WorldPos pos)
	{
		return pos.level().getBlockState(pos.pos()).getFluidState().is(FluidTags.WATER);
	}
	
	@Override
	public void appendDescription(List<Component> list)
	{
		list.add(MITweaksText.RECIPE_REQUIRES_OPEN_WATER.text(Parser.FLOAT_PERCENTAGE.parse(fill, 1), relative.text(), range));
	}
	
	@Override
	public MapCodec<? extends MachineProcessCondition> codec()
	{
		return CODEC;
	}
	
	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, ? extends MachineProcessCondition> streamCodec()
	{
		return STREAM_CODEC;
	}
	
	public enum Relative implements StringRepresentable
	{
		ALL(
				MITweaksText.RECIPE_REQUIRES_OPEN_WATER_ALL,
				(origin, range) -> BlockPos.betweenClosed(
						origin.offset(-range, -range, -range),
						origin.offset(range, range, range)
				)
		),
		AT_AND_BELOW(
				MITweaksText.RECIPE_REQUIRES_OPEN_WATER_AT_AND_BELOW,
				(origin, range) -> BlockPos.betweenClosed(
						origin.offset(-range, -range, -range),
						origin.offset(range, 0, range)
				)
		),
		BELOW(
				MITweaksText.RECIPE_REQUIRES_OPEN_WATER_BELOW,
				(origin, range) -> BlockPos.betweenClosed(
						origin.offset(-range, -range, -range),
						origin.offset(range, -1, range)
				)
		),
		AT_AND_ABOVE(
				MITweaksText.RECIPE_REQUIRES_OPEN_WATER_AT_AND_ABOVE,
				(origin, range) -> BlockPos.betweenClosed(
						origin.offset(-range, 0, -range),
						origin.offset(range, range, range)
				)
		),
		ABOVE(
				MITweaksText.RECIPE_REQUIRES_OPEN_WATER_ABOVE,
				(origin, range) -> BlockPos.betweenClosed(
						origin.offset(-range, 1, -range),
						origin.offset(range, range, range)
				)
		);
		
		public static final Codec<Relative> CODEC = StringRepresentable.fromEnum(Relative::values);
		
		public static final StreamCodec<ByteBuf, Relative> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
		
		private final TranslatableTextEnum                              text;
		private final BiFunction<BlockPos, Integer, Iterable<BlockPos>> blocks;
		
		Relative(TranslatableTextEnum text, BiFunction<BlockPos, Integer, Iterable<BlockPos>> blocks)
		{
			this.text = text;
			this.blocks = blocks;
		}
		
		public MutableComponent text()
		{
			return text.text();
		}
		
		public Iterable<BlockPos> blocks(BlockPos origin, int range)
		{
			return blocks.apply(origin, range);
		}
		
		@Override
		public String getSerializedName()
		{
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
}

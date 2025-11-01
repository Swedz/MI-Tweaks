package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.api.WorldPos;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public record OpenWaterProcessCondition(
		SurroundingArea relative, int range, float fill
) implements MachineProcessCondition
{
	public static final MapCodec<OpenWaterProcessCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
			.group(
					SurroundingArea.CODEC.fieldOf("relative").forGetter(OpenWaterProcessCondition::relative),
					Codec.intRange(1, 16).fieldOf("range").forGetter(OpenWaterProcessCondition::range),
					Codec.floatRange(0, 1).optionalFieldOf("fill", 1f).forGetter(OpenWaterProcessCondition::fill)
			)
			.apply(instance, OpenWaterProcessCondition::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenWaterProcessCondition> STREAM_CODEC = StreamCodec.composite(
			SurroundingArea.STREAM_CODEC,
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
	public void appendDescription(List<Component> lines)
	{
		lines.add(MITweaks.text().recipeRequiresOpenWater(fill, relative, range));
	}
	
	@Override
	public ItemStack icon()
	{
		return Items.WATER_BUCKET.getDefaultInstance();
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
}

package net.swedz.mi_tweaks.machine.processcondition;

import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.tesseract.neoforge.api.WorldPos;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

public record NearbyEntityProcessCondition(
		SurroundingArea relative, int range, EntityType<?> entity, int count
) implements MachineProcessCondition
{
	public static final MapCodec<NearbyEntityProcessCondition> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
			.group(
					SurroundingArea.CODEC.fieldOf("relative").forGetter(NearbyEntityProcessCondition::relative),
					Codec.intRange(1, 16).fieldOf("range").forGetter(NearbyEntityProcessCondition::range),
					CodecHelper.forRegistry(BuiltInRegistries.ENTITY_TYPE).fieldOf("entity").forGetter(NearbyEntityProcessCondition::entity),
					Codec.intRange(1, Integer.MAX_VALUE).fieldOf("count").forGetter(NearbyEntityProcessCondition::count)
			)
			.apply(instance, NearbyEntityProcessCondition::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, NearbyEntityProcessCondition> STREAM_CODEC = StreamCodec.composite(
			SurroundingArea.STREAM_CODEC,
			NearbyEntityProcessCondition::relative,
			ByteBufCodecs.INT,
			NearbyEntityProcessCondition::range,
			CodecHelper.forRegistryStream(BuiltInRegistries.ENTITY_TYPE),
			NearbyEntityProcessCondition::entity,
			ByteBufCodecs.INT,
			NearbyEntityProcessCondition::count,
			NearbyEntityProcessCondition::new
	);
	
	private static final Cache<WorldPos, Boolean> NEARBY_ENTITY_CACHE = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.SECONDS)
			.build();
	
	@Override
	public boolean canProcessRecipe(Context context, MachineRecipe recipe)
	{
		var level = context.getLevel();
		var pos = new WorldPos(level, context.getBlockEntity().getBlockPos());
		Boolean value = NEARBY_ENTITY_CACHE.getIfPresent(pos);
		if(value == null)
		{
			var area = relative.bounds(pos.pos(), range);
			var entities = level.getEntities((Entity) null, area, (e) -> e.getType() == entity);
			value = entities.size() >= count;
			NEARBY_ENTITY_CACHE.put(pos, value);
		}
		return value;
	}
	
	@Override
	public void appendDescription(List<Component> list)
	{
		list.add(MITweaksText.RECIPE_REQUIRES_NEARBY_ENTITY.text(count, entity.getDescription(), relative.text(), range));
	}
	
	@Override
	public ItemStack icon()
	{
		return SpawnEggItem.byId(entity).getDefaultInstance();
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

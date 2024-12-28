package net.swedz.mi_tweaks.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.swedz.mi_tweaks.MITweaksDataMaps;
import net.swedz.tesseract.neoforge.helper.RegistryHelper;

public record WaterExplosive(float strength, boolean fire)
{
	public static final Codec<WaterExplosive> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(
					ExtraCodecs.floatRangeMinExclusiveWithMessage(0, 200, (v) -> "Value must be within range [1;200]: " + v).fieldOf("strength").forGetter(WaterExplosive::strength),
					Codec.BOOL.fieldOf("fire").forGetter(WaterExplosive::fire)
			)
			.apply(instance, WaterExplosive::new));
	
	public static WaterExplosive getFor(Item item)
	{
		return RegistryHelper.holder(BuiltInRegistries.ITEM, item).getData(MITweaksDataMaps.WATER_EXPLOSIVE);
	}
}

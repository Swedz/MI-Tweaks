package net.swedz.mi_tweaks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.swedz.mi_tweaks.datamap.WaterExplosive;

public final class MITweaksDataMaps
{
	public static final DataMapType<Item, WaterExplosive> WATER_EXPLOSIVE = DataMapType
			.builder(MITweaks.id("water_explosive"), Registries.ITEM, WaterExplosive.CODEC)
			.build();
	
	public static void init(RegisterDataMapTypesEvent event)
	{
		event.register(WATER_EXPLOSIVE);
	}
}

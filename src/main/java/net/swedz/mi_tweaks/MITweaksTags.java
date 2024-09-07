package net.swedz.mi_tweaks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MITweaksTags
{
	public static final TagKey<Item> FE_CABLES = item("fe_cables");
	
	public static TagKey<Item> item(String path)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), MITweaks.id(path));
	}
}

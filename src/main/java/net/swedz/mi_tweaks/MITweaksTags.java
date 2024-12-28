package net.swedz.mi_tweaks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MITweaksTags
{
	public static final TagKey<Item> FE_CABLES = itemCommon("fe_cables");
	
	public static final TagKey<Item> VERY_HOT = item("very_hot");
	
	public static TagKey<Item> itemCommon(String path)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath("c", path));
	}
	
	public static TagKey<Item> item(String path)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), MITweaks.id(path));
	}
}

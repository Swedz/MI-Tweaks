package net.swedz.mi_tweaks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MITweaksTags
{
	public static final TagKey<Item> FE_CABLES = itemCommon("fe_cables");
	
	public static TagKey<Item> itemCommon(String path)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath("c", path));
	}
}

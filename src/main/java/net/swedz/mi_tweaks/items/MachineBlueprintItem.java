package net.swedz.mi_tweaks.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.swedz.mi_tweaks.items.renderer.MachineBlueprintItemRenderer;

import java.util.function.Consumer;

public final class MachineBlueprintItem extends Item
{
	public MachineBlueprintItem(Properties properties)
	{
		super(properties.stacksTo(1));
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		// TODO this doesnt work???
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer()
			{
				return new MachineBlueprintItemRenderer();
			}
		});
	}
}

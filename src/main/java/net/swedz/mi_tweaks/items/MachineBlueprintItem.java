package net.swedz.mi_tweaks.items;

import aztech.modern_industrialization.machines.MachineBlock;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.swedz.mi_tweaks.items.renderer.MachineBlueprintItemRenderer;

import java.util.Optional;
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
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer()
			{
				return new MachineBlueprintItemRenderer();
			}
		});
	}
	
	public static Optional<Block> getBlock(ItemStack stack)
	{
		CompoundTag tag = stack.getOrCreateTag();
		if(tag.contains("machine"))
		{
			String machineId = tag.getString("machine");
			Optional<Block> machineBlockOptional = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(machineId));
			if(machineBlockOptional.isPresent())
			{
				Block machineBlock = machineBlockOptional.get();
				if(machineBlock instanceof MachineBlock)
				{
					return Optional.of(machineBlock);
				}
			}
		}
		return Optional.empty();
	}
}

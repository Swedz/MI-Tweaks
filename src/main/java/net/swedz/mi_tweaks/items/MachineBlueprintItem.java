package net.swedz.mi_tweaks.items;

import aztech.modern_industrialization.machines.MachineBlock;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.items.renderer.MachineBlueprintItemRenderer;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
	
	public static Optional<Block> getMachineBlock(ItemStack stack)
	{
		CompoundTag tag = stack.getOrCreateTag();
		if(tag.contains("machine"))
		{
			String machineId = tag.getString("machine");
			Optional<Block> machineBlockOptional = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(machineId));
			if(machineBlockOptional.isPresent() && machineBlockOptional.get() instanceof MachineBlock)
			{
				return machineBlockOptional;
			}
		}
		return Optional.empty();
	}
	
	public static boolean hasBlueprint(Inventory playerInventory, Block machineBlock)
	{
		return Stream.concat(playerInventory.items.stream(), playerInventory.offhand.stream())
				.anyMatch((stack) ->
				{
					if(stack.getItem().equals(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
					{
						Optional<Block> machineBlockOptional = getMachineBlock(stack);
						return machineBlockOptional.isPresent() && machineBlockOptional.get().equals(machineBlock);
					}
					return false;
				});
	}
}

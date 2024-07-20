package net.swedz.mi_tweaks.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;

import java.util.List;
import java.util.Random;

public final class EmiCopyBlueprintRecipe extends EmiPatternCraftingRecipe
{
	public EmiCopyBlueprintRecipe(ResourceLocation id)
	{
		super(
				List.of(EmiStack.of(MITweaksItems.MACHINE_BLUEPRINT), EmiStack.of(Items.PAPER)),
				EmiStack.of(MITweaksItems.MACHINE_BLUEPRINT),
				id
		);
	}
	
	@Override
	public SlotWidget getInputWidget(int slot, int x, int y)
	{
		if(slot == 0)
		{
			return new GeneratedSlotWidget(EmiCopyBlueprintRecipe::generateBlueprintItem, unique, x, y).catalyst(true);
		}
		else if(slot == 1)
		{
			return new SlotWidget(EmiStack.of(Items.PAPER), x, y);
		}
		else
		{
			return new SlotWidget(EmiStack.EMPTY, x, y);
		}
	}
	
	@Override
	public SlotWidget getOutputWidget(int x, int y)
	{
		return new GeneratedSlotWidget(EmiCopyBlueprintRecipe::generateBlueprintItem, unique, x, y);
	}
	
	private static EmiStack generateBlueprintItem(Random random)
	{
		ItemStack blueprintItem = MITweaksItems.MACHINE_BLUEPRINT.asItem().getDefaultInstance();
		if(!MITweaksConfig.machineBlueprintsMachines.isEmpty())
		{
			int index = random.nextInt(MITweaksConfig.machineBlueprintsMachines.size());
			Block machineBlock = MITweaksConfig.machineBlueprintsMachines.get(index);
			MachineBlueprintItem.setMachineBlock(blueprintItem, machineBlock);
		}
		return EmiStack.of(blueprintItem);
	}
}

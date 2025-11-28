package net.swedz.mi_tweaks.mixin.machinecabletier;

import aztech.modern_industrialization.api.energy.CableTierHolder;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.blockentities.AbstractCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.ElectricCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.components.MachineInventoryComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = ElectricCraftingMachineBlockEntity.class,
		remap = false
)
public abstract class ElectricCraftingSingleblockCableTierGetterMixin extends AbstractCraftingMachineBlockEntity
{
	public ElectricCraftingSingleblockCableTierGetterMixin(BEP bep, MachineRecipeType recipeType, MachineInventoryComponent inventory, MachineGuiParameters guiParams, ProgressBar.Params progressBarParams, MachineTier tier)
	{
		super(bep, recipeType, inventory, guiParams, progressBarParams, tier);
	}
	
	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void init(BEP bep, MachineRecipeType recipeType, MachineInventoryComponent inventory, MachineGuiParameters guiParams, EnergyBar.Params energyBarParams, ProgressBar.Params progressBarParams, RecipeEfficiencyBar.Params efficiencyBarParams, MachineTier tier, long euCapacity, CallbackInfo callback)
	{
		this.registerGuiComponent(new ExposeCableTierGui((CableTierHolder) this));
	}
}

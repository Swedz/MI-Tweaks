package net.swedz.mi_tweaks.mixin.machinecabletier;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.hatches.EnergyHatch;
import aztech.modern_industrialization.machines.blockentities.multiblocks.AbstractElectricCraftingMultiblockBlockEntity;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import net.swedz.mi_tweaks.api.CableTierHolder;
import net.swedz.mi_tweaks.guicomponent.exposecabletier.ExposeCableTierGui;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied.AbstractElectricMultipliedCraftingMultiblockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = {
				AbstractElectricCraftingMultiblockBlockEntity.class,
				AbstractElectricMultipliedCraftingMultiblockBlockEntity.class
		},
		remap = false
)
public abstract class ElectricCraftingMultiblockCableTierGetterMixin extends MachineBlockEntity implements CableTierHolder
{
	public ElectricCraftingMultiblockCableTierGetterMixin(BEP bep, MachineGuiParameters guiParams, OrientationComponent.Params orientationParams)
	{
		super(bep, guiParams, orientationParams);
	}
	
	@Unique
	private CableTier cableTier = CableTier.LV;
	
	@Unique
	@Override
	public CableTier getCableTier()
	{
		return cableTier;
	}
	
	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void init(CallbackInfo callback)
	{
		this.registerGuiComponent(new ExposeCableTierGui.Server(this));
	}
	
	@Inject(
			method = "onSuccessfulMatch",
			at = @At("HEAD")
	)
	private void onSuccessfulMatch(ShapeMatcher shapeMatcher, CallbackInfo callback)
	{
		cableTier = CableTier.LV;
		for(HatchBlockEntity hatch : shapeMatcher.getMatchedHatches())
		{
			if(hatch instanceof EnergyHatch)
			{
				CableTierHolder energyHatch = (CableTierHolder) hatch;
				if(cableTier.eu < energyHatch.getCableTier().eu)
				{
					cableTier = energyHatch.getCableTier();
				}
			}
		}
	}
}

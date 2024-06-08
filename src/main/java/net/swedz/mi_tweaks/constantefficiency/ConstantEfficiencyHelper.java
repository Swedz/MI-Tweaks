package net.swedz.mi_tweaks.constantefficiency;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.api.CableTierHolder;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;

public final class ConstantEfficiencyHelper
{
	public static long getRecipeEu(CableTier tier)
	{
		return tier.eu / 4;
	}
	
	public static long getActualMaxRecipeEu(Object blockEntity, CrafterComponent.Behavior behavior)
	{
		if(MITweaksConfig.machineEfficiencyHack.useVoltageForEfficiency() && blockEntity instanceof CableTierHolder machine)
		{
			return ConstantEfficiencyHelper.getRecipeEu(machine.getCableTier());
		}
		return behavior.getMaxRecipeEu();
	}
	
	public static long getActualMaxRecipeEu(Object blockEntity, ModularCrafterAccessBehavior behavior)
	{
		if(MITweaksConfig.machineEfficiencyHack.useVoltageForEfficiency() && blockEntity instanceof CableTierHolder machine)
		{
			return ConstantEfficiencyHelper.getRecipeEu(machine.getCableTier());
		}
		return behavior.getBaseMaxRecipeEu();
	}
}

package net.swedz.mi_tweaks.constantefficiency.hack;

import net.swedz.mi_tweaks.api.CableTierHolder;
import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.MachineMIHookContext;

public final class UseVoltageMachineEfficiencyHack implements MachineEfficiencyHack
{
	@Override
	public boolean constantEfficiency(EfficiencyMIHookContext context)
	{
		return true;
	}
	
	@Override
	public boolean preventsUpgrades(MachineMIHookContext context)
	{
		return true;
	}
	
	@Override
	public int getEfficiencyTicks(EfficiencyMIHookContext context)
	{
		return context.getMaxEfficiencyTicks();
	}
	
	@Override
	public long getMaxRecipeEu(EfficiencyMIHookContext context)
	{
		if(context.getMachineBlockEntity() instanceof CableTierHolder machine)
		{
			return ConstantEfficiencyHelper.getRecipeEu(machine.getCableTier());
		}
		return context.getMaxRecipeEu();
	}
}

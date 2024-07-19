package net.swedz.mi_tweaks.constantefficiency.hack;

import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.MachineMIHookContext;

public final class DisabledMachineEfficiencyHack implements MachineEfficiencyHack
{
	@Override
	public boolean constantEfficiency(EfficiencyMIHookContext context)
	{
		return false;
	}
	
	@Override
	public boolean preventsUpgrades(MachineMIHookContext context)
	{
		return false;
	}
	
	@Override
	public int getEfficiencyTicks(EfficiencyMIHookContext context)
	{
		return context.getEfficiencyTicks();
	}
	
	@Override
	public long getMaxRecipeEu(EfficiencyMIHookContext context)
	{
		return context.getMaxRecipeEu();
	}
}

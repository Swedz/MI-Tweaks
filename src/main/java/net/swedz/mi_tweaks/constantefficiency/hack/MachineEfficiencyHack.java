package net.swedz.mi_tweaks.constantefficiency.hack;

import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.MachineMIHookContext;

public interface MachineEfficiencyHack
{
	boolean constantEfficiency(EfficiencyMIHookContext context);
	
	boolean preventsUpgrades(MachineMIHookContext context);
	
	int getEfficiencyTicks(EfficiencyMIHookContext context);
	
	long getMaxRecipeEu(EfficiencyMIHookContext context);
}

package net.swedz.mi_tweaks.compat.mi;

import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.constantefficiency.hack.MachineEfficiencyHack;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;

public final class MITweaksMIHookEfficiency implements MIHookEfficiency
{
	public static final MachineEfficiencyHack HACK = MITweaksConfig.efficiencyHack.createInstance();
	
	@Override
	public int getPriority()
	{
		return Integer.MIN_VALUE;
	}
	
	@Override
	public boolean shouldAlwaysRun()
	{
		return true;
	}
	
	@Override
	public void onGetRecipeMaxEu(EfficiencyMIHookContext context)
	{
		context.setMaxRecipeEu(HACK.getMaxRecipeEu(context));
	}
	
	@Override
	public void onDecreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(HACK.constantEfficiency(context))
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onIncreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(HACK.constantEfficiency(context))
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onTickStart(EfficiencyMIHookContext context)
	{
		if(HACK.constantEfficiency(context))
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? HACK.getEfficiencyTicks(context) : 0);
		}
	}
	
	@Override
	public void onTickEnd(EfficiencyMIHookContext context, long eu)
	{
		if(HACK.constantEfficiency(context) && eu == 0)
		{
			context.setEfficiencyTicks(0);
		}
	}
	
	@Override
	public void onReadNbt(EfficiencyMIHookContext context)
	{
		if(HACK.constantEfficiency(context))
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? HACK.getEfficiencyTicks(context) : 0);
		}
	}
}

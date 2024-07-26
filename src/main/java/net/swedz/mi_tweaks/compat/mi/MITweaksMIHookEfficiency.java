package net.swedz.mi_tweaks.compat.mi;

import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.TesseractMIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;

@TesseractMIHookEntrypoint
public final class MITweaksMIHookEfficiency implements MIHookEfficiency
{
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
		context.setMaxRecipeEu(MITweaksConfig.efficiencyHack.instance().getMaxRecipeEu(context));
	}
	
	@Override
	public void onDecreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.efficiencyHack.instance().constantEfficiency(context))
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onIncreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.efficiencyHack.instance().constantEfficiency(context))
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onTickStart(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.efficiencyHack.instance().constantEfficiency(context))
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? MITweaksConfig.efficiencyHack.instance().getEfficiencyTicks(context) : 0);
		}
	}
	
	@Override
	public void onTickEnd(EfficiencyMIHookContext context, long eu)
	{
		if(MITweaksConfig.efficiencyHack.instance().constantEfficiency(context) && eu == 0)
		{
			context.setEfficiencyTicks(0);
		}
	}
	
	@Override
	public void onReadNbt(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.efficiencyHack.instance().constantEfficiency(context))
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? MITweaksConfig.efficiencyHack.instance().getEfficiencyTicks(context) : 0);
		}
	}
}

package net.swedz.mi_tweaks.compat.mi;

import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;

@MIHookEntrypoint
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
		context.setMaxRecipeEu(MITweaks.config().efficiency().hack().instance().getMaxRecipeEu(context));
	}
	
	@Override
	public void onDecreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(MITweaks.config().efficiency().hack().instance().constantEfficiency(context))
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onIncreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(MITweaks.config().efficiency().hack().instance().constantEfficiency(context))
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onTickStart(EfficiencyMIHookContext context)
	{
		if(MITweaks.config().efficiency().hack().instance().constantEfficiency(context))
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? MITweaks.config().efficiency().hack().instance().getEfficiencyTicks(context) : 0);
		}
	}
	
	@Override
	public void onTickEnd(EfficiencyMIHookContext context, long eu)
	{
		if(MITweaks.config().efficiency().hack().instance().constantEfficiency(context) && eu == 0)
		{
			context.setEfficiencyTicks(0);
		}
	}
	
	@Override
	public void onReadNbt(EfficiencyMIHookContext context)
	{
		if(MITweaks.config().efficiency().hack().instance().constantEfficiency(context))
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? MITweaks.config().efficiency().hack().instance().getEfficiencyTicks(context) : 0);
		}
	}
}

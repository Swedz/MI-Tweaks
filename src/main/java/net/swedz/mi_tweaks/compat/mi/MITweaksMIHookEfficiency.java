package net.swedz.mi_tweaks.compat.mi;

import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.api.CableTierHolder;
import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.efficiency.EfficiencyMIHookContext;

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
		if(MITweaksConfig.machineEfficiencyHack.useVoltageForEfficiency() &&
				context.getMachineBlockEntity() instanceof CableTierHolder machine)
		{
			context.setMaxRecipeEu(ConstantEfficiencyHelper.getRecipeEu(machine.getCableTier()));
		}
	}
	
	@Override
	public void onDecreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onIncreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			context.setCancelled(true);
		}
	}
	
	@Override
	public void onTickStart(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? context.getMaxEfficiencyTicks() : 0);
		}
	}
	
	@Override
	public void onResetRecipe(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			context.setEfficiencyTicks(0);
		}
	}
	
	@Override
	public void onReadNbt(EfficiencyMIHookContext context)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			context.setEfficiencyTicks(context.hasActiveRecipe() ? context.getMaxEfficiencyTicks() : 0);
		}
	}
}

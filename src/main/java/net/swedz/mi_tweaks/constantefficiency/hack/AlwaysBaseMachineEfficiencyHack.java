package net.swedz.mi_tweaks.constantefficiency.hack;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.api.machine.holder.CrafterComponentHolder;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import net.swedz.tesseract.neoforge.compat.mi.api.ActiveRecipeHolder;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccess;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.MachineMIHookContext;

public final class AlwaysBaseMachineEfficiencyHack implements MachineEfficiencyHack
{
	private static long getBaseMachineEu(MachineBlockEntity machine)
	{
		if(machine instanceof CrafterComponentHolder crafterHolder)
		{
			return getBaseMachineEu(crafterHolder.getCrafterComponent());
		}
		else
		{
			throw new IllegalStateException("Efficiency hack was called for non crafter component holding machine (%s)".formatted(machine.getClass()));
		}
	}
	
	private static long getBaseMachineEu(CrafterAccess crafter)
	{
		if(crafter instanceof ModularCrafterAccess modularCrafter)
		{
			ModularCrafterAccessBehavior behavior = modularCrafter.getBehavior();
			return behavior.getBaseRecipeEu();
		}
		else if(crafter instanceof CrafterComponent crafterComponent)
		{
			CrafterComponent.Behavior behavior = crafterComponent.getBehavior();
			return behavior.getBaseRecipeEu();
		}
		else
		{
			throw new IllegalStateException("Unsupported crafter component type (%s)".formatted(crafter.getClass()));
		}
	}
	
	private static long getRecipeMaxEu(long machineBaseEu, long machineMaxEu, long recipeEu, long totalEu, int efficiencyTicks)
	{
		long baseEu = Math.max(machineBaseEu, recipeEu);
		return Math.min(totalEu, Math.min((int) Math.floor(baseEu * CrafterComponent.getEfficiencyOverclock(efficiencyTicks)), machineMaxEu));
	}
	
	private static int calculateEfficiencyTicks(long machineBaseEu, long machineMaxEu, long recipeEu, long totalEu, long targetEu)
	{
		long lastRecipeMaxEu = -1;
		for(int ticks = 0; true; ++ticks)
		{
			long recipeMaxEu = getRecipeMaxEu(machineBaseEu, machineMaxEu, recipeEu, totalEu, ticks);
			if(recipeMaxEu == Math.min(targetEu, totalEu) ||
			   (targetEu > lastRecipeMaxEu && targetEu < recipeMaxEu))
			{
				return ticks;
			}
			lastRecipeMaxEu = recipeMaxEu;
		}
	}
	
	@Override
	public boolean constantEfficiency(EfficiencyMIHookContext context)
	{
		return true;
	}
	
	@Override
	public boolean preventsUpgrades(MachineMIHookContext context)
	{
		return false;
	}
	
	@Override
	public int getEfficiencyTicks(EfficiencyMIHookContext context)
	{
		MachineBlockEntity machine = context.getMachineBlockEntity();
		
		long machineBaseEu;
		long maxRecipeEu;
		long recipeEu;
		long recipeTotalEu;
		if(machine instanceof CrafterComponentHolder crafterHolder)
		{
			CrafterAccess crafter = crafterHolder.getCrafterComponent();
			
			if(crafter instanceof ModularCrafterAccess modularCrafter)
			{
				ModularCrafterAccessBehavior behavior = modularCrafter.getBehavior();
				machineBaseEu = behavior.getBaseRecipeEu();
				maxRecipeEu = behavior.getBaseMaxRecipeEu();
			}
			else if(crafter instanceof CrafterComponent crafterComponent)
			{
				CrafterComponent.Behavior behavior = crafterComponent.getBehavior();
				machineBaseEu = behavior.getBaseRecipeEu();
				maxRecipeEu = behavior.getMaxRecipeEu();
			}
			else
			{
				throw new IllegalStateException("Unsupported crafter component type (%s) due to invalid CrafterAccess".formatted(crafter.getClass()));
			}
			
			if(crafter instanceof ActiveRecipeHolder activeRecipeHolder)
			{
				recipeEu = activeRecipeHolder.getRecipeEuCost(activeRecipeHolder.getActiveRecipe());
				recipeTotalEu = activeRecipeHolder.getRecipeTotalEuCost(activeRecipeHolder.getActiveRecipe());
			}
			else
			{
				throw new IllegalStateException("Unsupported crafter component type (%s) due to lack of ActiveRecipeHolder".formatted(crafter.getClass()));
			}
		}
		else
		{
			throw new IllegalStateException("Efficiency hack was called for non crafter component holding machine (%s)".formatted(machine.getClass()));
		}
		long additionalEuFromUpgrades = machine.mapComponentOrDefault(UpgradeComponent.class, UpgradeComponent::getAddMaxEUPerTick, 0L);
		long machineBaseMaxEu = maxRecipeEu - additionalEuFromUpgrades;
		
		long targetEu;
		if(recipeEu > machineBaseMaxEu)
		{
			long upgradesToExclude = recipeEu - machineBaseMaxEu;
			targetEu = recipeEu + additionalEuFromUpgrades - upgradesToExclude;
		}
		else
		{
			targetEu = Math.max(recipeEu, machineBaseEu) + additionalEuFromUpgrades;
		}
		
		return calculateEfficiencyTicks(machineBaseEu, maxRecipeEu, recipeEu, recipeTotalEu, targetEu);
	}
	
	@Override
	public long getMaxRecipeEu(EfficiencyMIHookContext context)
	{
		return context.getMaxRecipeEu();
	}
}

package net.swedz.mi_tweaks;

import aztech.modern_industrialization.MITooltips;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.blockentities.hatches.EnergyHatch;
import aztech.modern_industrialization.machines.components.CasingComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.swedz.mi_tweaks.api.CableTierHolder;
import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

import static aztech.modern_industrialization.MITooltips.*;

public final class MITweaksTooltips
{
	public static final TooltipAttachment MACHINE_HULL_AND_ENERGY_HATCH_VOLTAGE = MITooltips.TooltipAttachment.ofMultilines(
			(itemStack, item) ->
			{
				List<Component> lines = Lists.newArrayList();
				
				CableTier tier;
				if(item instanceof BlockItem blockItem &&
						blockItem.getBlock() instanceof MachineBlock machineBlock &&
						machineBlock.getBlockEntityInstance() instanceof EnergyHatch energyHatch)
				{
					tier = ((CableTierHolder) energyHatch).getCableTier();
				}
				else
				{
					tier = CasingComponent.getCasingTier(item);
				}
				
				if(tier != null)
				{
					if(MITweaksConfig.displayMachineVoltage)
					{
						lines.add(DEFAULT_PARSER.parse(MITweaksText.MACHINE_VOLTAGE_RECIPES.text(Component.translatable(tier.shortEnglishKey()))));
					}
					if(MITweaksConfig.machineEfficiencyHack.useVoltageForEfficiency())
					{
						lines.add(DEFAULT_PARSER.parse(MITweaksText.MACHINE_VOLTAGE_RUNS_AT.text(EU_PER_TICK_PARSER.parse(ConstantEfficiencyHelper.getRecipeEu(tier)))));
					}
				}
				
				return lines.isEmpty() ? Optional.empty() : Optional.of(lines);
			}
	);
	
	public static void init()
	{
	}
}

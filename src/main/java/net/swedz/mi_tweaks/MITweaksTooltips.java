package net.swedz.mi_tweaks;

import aztech.modern_industrialization.MITooltips;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.blockentities.hatches.EnergyHatch;
import aztech.modern_industrialization.machines.components.CasingComponent;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.swedz.mi_tweaks.api.CableTierHolder;
import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import net.swedz.mi_tweaks.constantefficiency.hack.MachineEfficiencyHackOption;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;
import net.swedz.mi_tweaks.proxy.CommonProxy;

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
					if(MITweaksConfig.efficiencyHack == MachineEfficiencyHackOption.USE_VOLTAGE)
					{
						lines.add(DEFAULT_PARSER.parse(MITweaksText.MACHINE_VOLTAGE_RUNS_AT.text(EU_PER_TICK_PARSER.parse(ConstantEfficiencyHelper.getRecipeEu(tier)))));
					}
				}
				
				return lines.isEmpty() ? Optional.empty() : Optional.of(lines);
			}
	);
	
	public static final TooltipAttachment MACHINE_BLUEPRINT_MISSING = MITooltips.TooltipAttachment.of(
			(itemStack, item) ->
			{
				if(CommonProxy.INSTANCE.isClient())
				{
					Player player = CommonProxy.INSTANCE.getClientPlayer();
					if(player != null &&
					   MITweaksConfig.machineBlueprintsRequiredTooltip.isEnabled() &&
					   item instanceof BlockItem blockItem && blockItem.getBlock() instanceof MachineBlock machineBlock &&
					   MITweaksConfig.machineBlueprintsMachines.contains(machineBlock))
					{
						return MachineBlueprintItem.hasBlueprint(player, machineBlock, MITweaksConfig.machineBlueprintsRequiredTooltip) ?
								Optional.empty() :
								Optional.of(MITweaksConfig.machineBlueprintsRequiredTooltip.tooltip().text().withStyle(ChatFormatting.RED));
					}
				}
				return Optional.empty();
			}
	).noShiftRequired();
	
	public static void init()
	{
	}
}

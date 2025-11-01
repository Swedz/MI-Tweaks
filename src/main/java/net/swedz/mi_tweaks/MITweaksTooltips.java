package net.swedz.mi_tweaks;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.CableTierHolder;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.components.CasingComponent;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.swedz.mi_tweaks.item.MachineBlueprintItem;
import net.swedz.tesseract.neoforge.proxy.Proxies;
import net.swedz.tesseract.neoforge.proxy.builtin.TesseractProxy;
import net.swedz.tesseract.neoforge.tooltip.TooltipAttachment;

import java.util.List;
import java.util.Optional;

public final class MITweaksTooltips
{
	public static final TooltipAttachment MACHINE_HULL_AND_ENERGY_HATCH_VOLTAGE = TooltipAttachment.multilinesOptional(
			(flags, context, stack, item) ->
			{
				List<Component> lines = Lists.newArrayList();
				
				CableTier tier = CasingComponent.getCasingTier(item);
				if(tier == null &&
				   item instanceof BlockItem blockItem &&
				   blockItem.getBlock() instanceof MachineBlock machineBlock &&
				   machineBlock.getBlockEntityInstance() instanceof HatchBlockEntity &&
				   machineBlock.getBlockEntityInstance() instanceof CableTierHolder energyHatch)
				{
					tier = energyHatch.getCableTier();
				}
				if(tier != null)
				{
					if(MITweaks.config().tweaks().displayMachineVoltage())
					{
						lines.add(MITweaks.text().machineVoltageRecipes(tier));
					}
					if(MITweaks.config().efficiency().useCasingMaxOverclockOverrides())
					{
						lines.add(MITweaks.text().machineHullAndHatchMaxOverclock(MITweaks.config().efficiency().casingMaxOverclockOverrides().get(tier)));
					}
				}
				
				return lines.isEmpty() ? Optional.empty() : Optional.of(lines);
			}
	);
	
	public static final TooltipAttachment MACHINE_BLUEPRINT_MISSING = TooltipAttachment.singleLineOptional(
			(flags, context, stack, item) ->
			{
				TesseractProxy proxy = Proxies.get(TesseractProxy.class);
				if(proxy.isClient())
				{
					Player player = proxy.getClientPlayer();
					if(player != null &&
					   MITweaks.config().machineBlueprints().required().tooltip().isEnabled() &&
					   item instanceof BlockItem blockItem && blockItem.getBlock() instanceof MachineBlock machineBlock &&
					   MITweaks.config().machineBlueprints().machines().contains(machineBlock))
					{
						return MachineBlueprintItem.hasBlueprint(player, machineBlock, MITweaks.config().machineBlueprints().required().tooltip()) ?
								Optional.empty() :
								Optional.of(MITweaks.config().machineBlueprints().required().tooltip().tooltip());
					}
				}
				return Optional.empty();
			}
	).noShiftRequired();
	
	public static final TooltipAttachment FLUX_TRANSFORMER = TooltipAttachment.singleLine(
			List.of(MITweaks.id("flux_transformer")),
			MITweaks.text().fluxTransformerHelp(MITweaks.config().fluxTransformer().conversionRate())
	);
	
	public static final TooltipAttachment EU_TRANSFORMER = TooltipAttachment.singleLine(
			List.of(MITweaks.id("eu_transformer")),
			MITweaks.text().euTransformerHelp(MITweaks.config().euTransformer().conversionRate())
	);
	
	public static void init()
	{
	}
}

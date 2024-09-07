package net.swedz.mi_tweaks.compat.mi;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.GuiComponentsClient;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksTooltips;
import net.swedz.mi_tweaks.machine.blockentity.FluxTransformerBlockEntity;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGui;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGuiClient;
import net.swedz.mi_tweaks.machine.processcondition.EBFCoilProcessCondition;
import net.swedz.mi_tweaks.machine.processcondition.VoltageProcessCondition;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ClientGuiComponentsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;

@MIHookEntrypoint
public final class MITweaksMIHookListener implements MIHookListener
{
	@Override
	public void singleBlockSpecialMachines(SingleBlockSpecialMachinesMIHookContext hook)
	{
		hook.register(
				"Flux Transformer", "flux_transformer", "flux_transformer",
				CableTier.HV.casing, false, false, false, false,
				FluxTransformerBlockEntity::new,
				FluxTransformerBlockEntity::registerEnergyApi
		);
	}
	
	@Override
	public void clientGuiComponents(ClientGuiComponentsMIHookContext hook)
	{
		GuiComponentsClient.register(ExposeCableTierGui.ID, ExposeCableTierGuiClient::new);
	}
	
	@Override
	public void machineProcessConditions(MachineProcessConditionsMIHookContext hook)
	{
		hook.register(MITweaks.id("voltage"), VoltageProcessCondition.CODEC, VoltageProcessCondition.STREAM_CODEC);
		hook.register(MITweaks.id("ebf_coil"), EBFCoilProcessCondition.CODEC, EBFCoilProcessCondition.STREAM_CODEC);
	}
	
	@Override
	public void tooltips()
	{
		MITweaksTooltips.init();
	}
}

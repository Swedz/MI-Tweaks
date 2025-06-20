package net.swedz.mi_tweaks.compat.mi;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.GuiComponentsClient;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksTooltips;
import net.swedz.mi_tweaks.compat.kubejs.proxy.KubeJSProxy;
import net.swedz.mi_tweaks.machine.blockentity.EUTransformerBlockEntity;
import net.swedz.mi_tweaks.machine.blockentity.FluxTransformerBlockEntity;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGui;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGuiClient;
import net.swedz.mi_tweaks.machine.processcondition.EBFCoilProcessCondition;
import net.swedz.mi_tweaks.machine.processcondition.MachineTierProcessCondition;
import net.swedz.mi_tweaks.machine.processcondition.NearbyEntityProcessCondition;
import net.swedz.mi_tweaks.machine.processcondition.OpenWaterProcessCondition;
import net.swedz.mi_tweaks.machine.processcondition.VoltageProcessCondition;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ClientGuiComponentsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.proxy.Proxies;

@MIHookEntrypoint
public final class MITweaksMIHookListener implements MIHookListener
{
	@Override
	public void singleBlockSpecialMachines(SingleBlockSpecialMachinesMIHookContext hook)
	{
		hook.register(
				"Flux Transformer", "flux_transformer", "flux_transformer",
				CableTier.LV.casing, false, true, true, false,
				FluxTransformerBlockEntity::new,
				FluxTransformerBlockEntity::registerEnergyApi
		);
		hook.register(
				"EU Transformer", "eu_transformer", "eu_transformer",
				CableTier.LV.casing, false, true, true, false,
				EUTransformerBlockEntity::new,
				EUTransformerBlockEntity::registerEnergyApi
		);
		
		var kubejs = Proxies.get(KubeJSProxy.class);
		kubejs.fireRegisterPowerlessMachines(hook);
	}
	
	@Override
	public void multiblockMachines(MultiblockMachinesMIHookContext hook)
	{
		var kubejs = Proxies.get(KubeJSProxy.class);
		kubejs.fireRegisterBatchMultiblocks(hook);
		kubejs.fireRegisterTieredMultiblocks(hook);
	}
	
	@Override
	public void clientGuiComponents(ClientGuiComponentsMIHookContext hook)
	{
		GuiComponentsClient.register(ExposeCableTierGui.ID, ExposeCableTierGuiClient::new);
	}
	
	@Override
	public void machineProcessConditions(MachineProcessConditionsMIHookContext hook)
	{
		hook.register(MITweaks.id("ebf_coil"), EBFCoilProcessCondition.CODEC, EBFCoilProcessCondition.STREAM_CODEC);
		hook.register(MITweaks.id("machine_tier"), MachineTierProcessCondition.CODEC, MachineTierProcessCondition.STREAM_CODEC);
		hook.register(MITweaks.id("nearby_entity"), NearbyEntityProcessCondition.CODEC, NearbyEntityProcessCondition.STREAM_CODEC);
		hook.register(MITweaks.id("open_water"), OpenWaterProcessCondition.CODEC, OpenWaterProcessCondition.STREAM_CODEC);
		hook.register(MITweaks.id("voltage"), VoltageProcessCondition.CODEC, VoltageProcessCondition.STREAM_CODEC);
	}
	
	@Override
	public void tooltips()
	{
		MITweaksTooltips.init();
	}
}

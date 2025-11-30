package net.swedz.mi_tweaks.compat.mi;

import aztech.modern_industrialization.api.energy.CableTier;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksTooltips;
import net.swedz.mi_tweaks.compat.kubejs.proxy.KubeJSProxy;
import net.swedz.mi_tweaks.machine.blockentity.EUTransformerBlockEntity;
import net.swedz.mi_tweaks.machine.blockentity.FluxTransformerBlockEntity;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGui;
import net.swedz.mi_tweaks.machine.guicomponent.exposecabletier.ExposeCableTierGuiClient;
import net.swedz.mi_tweaks.machine.guicomponent.waterpumpenvironment.WaterPumpEnvironmentGui;
import net.swedz.mi_tweaks.machine.guicomponent.waterpumpenvironment.WaterPumpEnvironmentGuiClient;
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
		hook.builder("flux_transformer", "Flux Transformer", FluxTransformerBlockEntity::new)
				.builtinModel(CableTier.LV.casing, "flux_transformer", (b) -> b.front(false).top().side().active(false))
				.registrator(FluxTransformerBlockEntity::registerEnergyApi)
				.registerMachine();
		
		hook.builder("eu_transformer", "EU Transformer", EUTransformerBlockEntity::new)
				.builtinModel(CableTier.LV.casing, "eu_transformer", (b) -> b.front(false).top().side().active(false))
				.registrator(EUTransformerBlockEntity::registerEnergyApi)
				.registerMachine();
		
		if(MITweaks.config().machineNamespace().equals(MITweaks.ID))
		{
			var kubejs = Proxies.get(KubeJSProxy.class);
			kubejs.fireRegisterPowerlessSingleblocks(hook);
		}
	}
	
	@Override
	public void multiblockMachines(MultiblockMachinesMIHookContext hook)
	{
		if(MITweaks.config().machineNamespace().equals(MITweaks.ID))
		{
			var kubejs = Proxies.get(KubeJSProxy.class);
			kubejs.fireRegisterBatchMultiblocks(hook);
			kubejs.fireRegisterPowerlessMultiblocks(hook);
			kubejs.fireRegisterTieredMultiblocks(hook);
		}
	}
	
	@Override
	public void clientGuiComponents(ClientGuiComponentsMIHookContext hook)
	{
		hook.register(ExposeCableTierGui.TYPE, ExposeCableTierGuiClient::new);
		hook.register(WaterPumpEnvironmentGui.TYPE, WaterPumpEnvironmentGuiClient::new);
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

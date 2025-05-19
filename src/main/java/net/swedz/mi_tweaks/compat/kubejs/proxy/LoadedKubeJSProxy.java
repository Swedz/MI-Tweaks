package net.swedz.mi_tweaks.compat.kubejs.proxy;

import net.swedz.mi_tweaks.compat.kubejs.machine.MITweaksMachineKubeJSEvents;
import net.swedz.mi_tweaks.compat.kubejs.machine.RegisterBatchMultiblocksEventJS;
import net.swedz.mi_tweaks.compat.kubejs.machine.RegisterPowerlessMachinesEventJS;
import net.swedz.mi_tweaks.compat.kubejs.machine.RegisterTieredMultiblocksEventJS;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;
import net.swedz.tesseract.neoforge.proxy.ProxyEnvironment;

@ProxyEntrypoint(environment = ProxyEnvironment.MOD, modid = "kubejs")
public class LoadedKubeJSProxy extends KubeJSProxy
{
	@Override
	public boolean isLoaded()
	{
		return true;
	}
	
	@Override
	public void fireRegisterBatchMultiblocks(MultiblockMachinesMIHookContext hook)
	{
		MITweaksMachineKubeJSEvents.REGISTER_BATCH_MULTIBLOCKS.post(new RegisterBatchMultiblocksEventJS(hook));
	}
	
	@Override
	public void fireRegisterPowerlessMachines(SingleBlockSpecialMachinesMIHookContext hook)
	{
		MITweaksMachineKubeJSEvents.REGISTER_POWERLESS_MACHINES.post(new RegisterPowerlessMachinesEventJS(hook));
	}
	
	@Override
	public void fireRegisterTieredMultiblocks(MultiblockMachinesMIHookContext hook)
	{
		MITweaksMachineKubeJSEvents.REGISTER_TIERED_MULTIBLOCKS.post(new RegisterTieredMultiblocksEventJS(hook));
	}
}

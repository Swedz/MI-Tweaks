package net.swedz.mi_tweaks.compat.mi.custom;

import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.compat.kubejs.proxy.KubeJSProxy;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.proxy.Proxies;

@MIHookEntrypoint
public final class CustomMITweaksMIHookListener implements MIHookListener
{
	@Override
	public String modId()
	{
		return MITweaks.config().machineNamespace();
	}
	
	@Override
	public boolean shouldInitialize()
	{
		return !MITweaks.config().machineNamespace().equals(MITweaks.ID);
	}
	
	@Override
	public void singleBlockSpecialMachines(SingleBlockSpecialMachinesMIHookContext hook)
	{
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
}

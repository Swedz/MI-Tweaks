package net.swedz.mi_tweaks.compat.kubejs.proxy;

import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.proxy.Proxy;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;

@ProxyEntrypoint
public class KubeJSProxy implements Proxy
{
	public boolean isLoaded()
	{
		return false;
	}

	public void fireRegisterBatchMultiblocks(MultiblockMachinesMIHookContext hook)
	{
	}

	public void fireRegisterPowerlessSingleblocks(SingleBlockSpecialMachinesMIHookContext hook)
	{
	}

	public void fireRegisterPowerlessMultiblocks(MultiblockMachinesMIHookContext hook)
	{
	}

	public void fireRegisterTieredMultiblocks(MultiblockMachinesMIHookContext hook)
	{
	}

	public void fireRegisterExternalBlockCableTiers()
	{
	}
}

package net.swedz.mi_tweaks.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import net.swedz.mi_tweaks.compat.kubejs.machine.MITweaksMachineKubeJSEvents;

public final class MITweaksKJSPlugin implements KubeJSPlugin
{
	@Override
	public void registerEvents(EventGroupRegistry registry)
	{
		registry.register(MITweaksMachineKubeJSEvents.EVENT_GROUP);
	}
}

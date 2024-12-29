package net.swedz.mi_tweaks.compat.kubejs.machine;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface MITweaksMachineKubeJSEvents
{
	EventGroup EVENT_GROUP = EventGroup.of("MITweaksMachineEvents");
	
	EventHandler REGISTER_BATCH_MULTIBLOCKS = EVENT_GROUP.startup("registerBatchMultiblocks", () -> RegisterBatchMultiblocksEventJS.class);
}

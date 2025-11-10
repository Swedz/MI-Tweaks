package net.swedz.mi_tweaks.compat.kubejs.machine;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface MITweaksMachineKubeJSEvents
{
	EventGroup EVENT_GROUP = EventGroup.of("MITweaksMachineEvents");
	
	EventHandler REGISTER_BATCH_MULTIBLOCKS  = EVENT_GROUP.startup("registerBatchMultiblocks", () -> RegisterBatchMultiblocksEventJS.class);
	EventHandler REGISTER_POWERLESS_MACHINES = EVENT_GROUP.startup("registerPowerlessMachines", () -> RegisterPowerlessMachinesEventJS.class);
	EventHandler REGISTER_TIERED_MULTIBLOCKS = EVENT_GROUP.startup("registerTieredMultiblocks", () -> RegisterTieredMultiblocksEventJS.class);
	EventHandler REGISTER_EXTERNAL_BLOCK_CABLE_TIERS = EVENT_GROUP.startup("registerExternalBlockCableTiers", () -> RegisterExternalBlockCableTiersEventJS.class);
}

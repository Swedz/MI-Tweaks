package net.swedz.mi_tweaks.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.datagen.client.DatagenDelegatorClient;
import net.swedz.mi_tweaks.datagen.server.DatagenDelegatorServer;

@EventBusSubscriber(modid = MITweaks.ID)
public final class DatagenDelegator
{
	@SubscribeEvent
	private static void gatherData(GatherDataEvent event)
	{
		DatagenDelegatorClient.configure(event);
		DatagenDelegatorServer.configure(event);
	}
}

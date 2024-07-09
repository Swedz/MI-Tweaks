package net.swedz.mi_tweaks;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.swedz.mi_tweaks.items.renderer.MachineBlueprintItemRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MITweaks.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MITweaksClient
{
	@SubscribeEvent
	private static void onModifyBakingResult(ModelEvent.RegisterAdditional event)
	{
		event.register(MachineBlueprintItemRenderer.MODEL_LOCATION);
	}
}

package net.swedz.mi_tweaks;

import net.minecraft.ChatFormatting;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;

@EventBusSubscriber(value = Dist.CLIENT, modid = MITweaks.ID, bus = EventBusSubscriber.Bus.MOD)
public final class MITweaksClient
{
	@SubscribeEvent
	private static void init(FMLConstructModEvent __)
	{
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, (e) ->
		{
			if(!MITweaksConfig.machineBlueprintsLearning &&
			   (MITweaksConfig.machineBlueprintsRequiredTooltip.isLearning() ||
				MITweaksConfig.machineBlueprintsRequiredForPlacing.isLearning() ||
				MITweaksConfig.machineBlueprintsRequiredForRenderingHatches.isLearning()))
			{
				e.getPlayer().displayClientMessage(MITweaksText.LEARNING_DISABLED_BUT_REQUIRING_LEARNING.text().withStyle(ChatFormatting.GOLD), false);
			}
		});
	}
	
	@SubscribeEvent
	private static void onModifyBakingResult(ModelEvent.RegisterAdditional event)
	{
		event.register(MachineBlueprintItem.RAW_ITEM_MODEL_LOCATION);
	}
}

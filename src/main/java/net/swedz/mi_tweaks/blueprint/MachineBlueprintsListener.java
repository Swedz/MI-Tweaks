package net.swedz.mi_tweaks.blueprint;

import aztech.modern_industrialization.machines.MachineBlock;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;
import net.swedz.mi_tweaks.item.MachineBlueprintItem;
import net.swedz.mi_tweaks.network.packet.UpdateBlueprintsLearnedPacket;

@EventBusSubscriber(modid = MITweaks.ID)
public final class MachineBlueprintsListener
{
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		BlueprintsLearned blueprintsLearned = player.getData(MITweaksOtherRegistries.BLUEPRINTS_LEARNED);
		new UpdateBlueprintsLearnedPacket(blueprintsLearned).sendToClient(player);
	}
	
	@SubscribeEvent
	private static void onUseItemOnBlock(UseItemOnBlockEvent event)
	{
		Player player = event.getPlayer();
		if(player != null &&
		   MITweaks.config().machineBlueprints().required().placing().isEnabled() &&
		   event.getUsePhase() == UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK &&
		   event.getUseOnContext().getItemInHand().getItem() instanceof BlockItem blockItem &&
		   blockItem.getBlock() instanceof MachineBlock machineBlock &&
		   MITweaks.config().machineBlueprints().machines().contains(machineBlock) &&
		   !MachineBlueprintItem.hasBlueprint(player, machineBlock, MITweaks.config().machineBlueprints().required().placing()))
		{
			event.cancelWithResult(ItemInteractionResult.CONSUME);
			player.displayClientMessage(MITweaks.config().machineBlueprints().required().placing().tooltip(), true);
		}
	}
}

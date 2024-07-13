package net.swedz.mi_tweaks.blueprint;

import aztech.modern_industrialization.machines.MachineBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;
import net.swedz.mi_tweaks.packets.UpdateBlueprintsLearnedPacket;

@Mod.EventBusSubscriber(modid = MITweaks.ID)
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
		Player player = event.getEntity();
		if(player != null &&
		   MITweaksConfig.machineBlueprintsRequiredForPlacing.isEnabled() &&
		   event.getUsePhase() == UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK &&
		   event.getUseOnContext().getItemInHand().getItem() instanceof BlockItem blockItem &&
		   blockItem.getBlock() instanceof MachineBlock machineBlock &&
		   MITweaksConfig.machineBlueprintsMachines.contains(machineBlock) &&
		   !MachineBlueprintItem.hasBlueprint(player, machineBlock, MITweaksConfig.machineBlueprintsRequiredForPlacing))
		{
			event.cancelWithResult(InteractionResult.CONSUME);
			player.displayClientMessage(MITweaksConfig.machineBlueprintsRequiredForPlacing.tooltip().text().withStyle(ChatFormatting.RED), true);
		}
	}
}

package net.swedz.mi_tweaks.feature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksAttributes;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksTags;

@EventBusSubscriber(modid = MITweaks.ID)
public final class VeryHotItems
{
	private static void burn(Player player)
	{
		player.setRemainingFireTicks(MITweaksConfig.veryHotItemsBurnTime);
	}
	
	@SubscribeEvent
	private static void inventoryTick(PlayerTickEvent.Post event)
	{
		var player = event.getEntity();
		var level = player.level();
		if(level == null || level.isClientSide() ||
		   player.getAbilities().invulnerable ||
		   player.fireImmune() ||
		   player.isInWaterRainOrBubble() ||
		   player.getAttributeValue(MITweaksAttributes.HEAT_PROTECTION) > 0)
		{
			return;
		}
		var items = player.getInventory().items;
		for(ItemStack stack : items)
		{
			if(stack.is(MITweaksTags.VERY_HOT))
			{
				burn(player);
				return;
			}
		}
	}
}

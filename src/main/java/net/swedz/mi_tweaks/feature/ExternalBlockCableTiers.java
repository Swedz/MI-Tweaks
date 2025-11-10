package net.swedz.mi_tweaks.feature;

import aztech.modern_industrialization.api.energy.CableTier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.compat.kubejs.proxy.KubeJSProxy;
import net.swedz.tesseract.neoforge.proxy.Proxies;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = MITweaks.ID)
public final class ExternalBlockCableTiers {
	@ApiStatus.Internal
	public static final Map<Block, CableTier> BLOCKS = new HashMap<>();

	@ApiStatus.Internal
	public static final Map<BlockEntityType<?>, CableTier> BLOCK_ENTITIES = new HashMap<>();

	@ApiStatus.Internal
	public static boolean utilized = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void beforeOtherCapabilitiesRegistration(RegisterCapabilitiesEvent event) {
		var kubejs = Proxies.get(KubeJSProxy.class);
		kubejs.fireRegisterExternalBlockCableTiers();
	}
}

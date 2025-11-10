package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.api.energy.CableTier;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.swedz.mi_tweaks.feature.ExternalBlockCableTiers;

public class RegisterExternalBlockCableTiersEventJS implements KubeEvent {
	public void block(Block block, String tier) {
		if (ExternalBlockCableTiers.BLOCKS.containsKey(block)) {
			throw new IllegalArgumentException("A cable tier already exists for the block " + block);
		}

		ExternalBlockCableTiers.utilized = true;

		ExternalBlockCableTiers.BLOCKS.put(block, CableTier.getTier(tier));
	}

	public void block(ResourceLocation block, String tier) {
		block(BuiltInRegistries.BLOCK.get(block), tier);
	}

	public void blockEntity(BlockEntityType<?> blockEntity, String tier) {
		if (ExternalBlockCableTiers.BLOCK_ENTITIES.containsKey(blockEntity)) {
			throw new IllegalArgumentException("A cable tier already exists for the block entity " + blockEntity);
		}

		ExternalBlockCableTiers.utilized = true;

		ExternalBlockCableTiers.BLOCK_ENTITIES.put(blockEntity, CableTier.getTier(tier));
	}

	public void blockEntity(ResourceLocation blockEntity, String tier) {
		blockEntity(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(blockEntity), tier);
	}
}

package net.swedz.mi_tweaks;

import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;

import java.util.Set;

public final class MITweaksBlocks
{
	public static final class Registry
	{
		public static final  DeferredRegister.Blocks              BLOCKS         = DeferredRegister.createBlocks(MITweaks.ID);
		public static final  DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MITweaks.ID);
		private static final Set<BlockHolder>                     HOLDERS        = Sets.newHashSet();
		
		private static void init(IEventBus bus)
		{
			BLOCKS.register(bus);
			BLOCK_ENTITIES.register(bus);
		}
		
		public static void include(BlockHolder holder)
		{
			HOLDERS.add(holder);
		}
	}
	
	public static void init(IEventBus bus)
	{
		Registry.init(bus);
	}
	
	public static Set<BlockHolder> values()
	{
		return Set.copyOf(Registry.HOLDERS);
	}
}

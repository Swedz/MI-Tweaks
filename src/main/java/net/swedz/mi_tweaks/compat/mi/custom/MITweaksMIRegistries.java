package net.swedz.mi_tweaks.compat.mi.custom;

import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Collections;
import java.util.Set;

public final class MITweaksMIRegistries
{
	public static final  DeferredRegister.Blocks              BLOCKS         = DeferredRegister.createBlocks(MITweaks.config().machineNamespace());
	public static final  DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MITweaks.config().machineNamespace());
	private static final Set<BlockHolder>                     BLOCK_HOLDERS  = Sets.newHashSet();
	
	public static void include(BlockHolder block)
	{
		BLOCK_HOLDERS.add(block);
	}
	
	public static Set<BlockHolder> getBlocks()
	{
		return Collections.unmodifiableSet(BLOCK_HOLDERS);
	}
	
	public static final  DeferredRegister.Items ITEMS        = DeferredRegister.createItems(MITweaks.config().machineNamespace());
	private static final Set<ItemHolder>        ITEM_HOLDERS = Sets.newHashSet();
	
	public static void include(ItemHolder item)
	{
		ITEM_HOLDERS.add(item);
	}
	
	public static Set<ItemHolder> getItems()
	{
		return Collections.unmodifiableSet(ITEM_HOLDERS);
	}
	
	public static void init(IEventBus bus)
	{
		BLOCKS.register(bus);
		BLOCK_ENTITIES.register(bus);
		ITEMS.register(bus);
	}
}

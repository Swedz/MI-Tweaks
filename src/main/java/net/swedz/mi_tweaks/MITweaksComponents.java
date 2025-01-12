package net.swedz.mi_tweaks;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.function.Supplier;

public final class MITweaksComponents
{
	private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MITweaks.ID);
	
	public static final Supplier<DataComponentType<Block>> MACHINE_BLOCK = COMPONENTS.registerComponentType(
			"machine",
			(b) -> b.persistent(CodecHelper.forRegistry(BuiltInRegistries.BLOCK))
	);
	
	public static void init(IEventBus bus)
	{
		COMPONENTS.register(bus);
	}
}

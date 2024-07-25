package net.swedz.mi_tweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MITweaksDataComponents
{
	private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(MITweaks.ID);
	
	public static final Supplier<DataComponentType<Block>> MACHINE_BLOCK = COMPONENTS.registerComponentType(
			"machine",
			(b) -> b.persistent(Codec.STRING.comapFlatMap(
							(blockId) ->
							{
								ResourceLocation resourceLocation;
								try
								{
									resourceLocation = ResourceLocation.parse(blockId);
								}
								catch (ResourceLocationException ex)
								{
									return DataResult.error(() -> "Not a valid resource location: %s %s".formatted(blockId, ex.getMessage()));
								}
								return BuiltInRegistries.BLOCK.getOptional(resourceLocation)
										.map(DataResult::success)
										.orElseGet(() -> DataResult.error(() -> "Not a valid block id: %s".formatted(blockId)));
							},
							(block) -> BuiltInRegistries.BLOCK.getKey(block).toString()
					).stable()
			)
	);
	
	public static void init(IEventBus bus)
	{
		COMPONENTS.register(bus);
	}
}

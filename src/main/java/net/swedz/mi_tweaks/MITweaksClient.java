package net.swedz.mi_tweaks;

import aztech.modern_industrialization.client.machines.MachineBlockEntityRenderer;
import aztech.modern_industrialization.client.machines.multiblocks.MultiblockMachineBER;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.swedz.mi_tweaks.compat.mi.custom.MITweaksMIRegistries;
import net.swedz.mi_tweaks.item.MachineBlueprintItem;
import net.swedz.mi_tweaks.item.renderer.BlockOverlayingItemRenderer;

import java.util.stream.Stream;

@Mod(value = MITweaks.ID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = MITweaks.ID, bus = EventBusSubscriber.Bus.MOD)
public final class MITweaksClient
{
	private static final ModelResourceLocation RAW_ITEM_MODEL_LOCATION = ModelResourceLocation.standalone(MITweaks.id("item/machine_blueprint_raw"));
	
	public MITweaksClient(IEventBus bus)
	{
		bus.addListener(RegisterClientExtensionsEvent.class, (event) ->
				event.registerItem(
						new IClientItemExtensions()
						{
							@Override
							public BlockEntityWithoutLevelRenderer getCustomRenderer()
							{
								return new BlockOverlayingItemRenderer(RAW_ITEM_MODEL_LOCATION, MachineBlueprintItem::getMachineBlock);
							}
						},
						MITweaksItems.MACHINE_BLUEPRINT.asItem()
				));
	}
	
	@SubscribeEvent
	private static void registerBlockEntityRenderers(FMLClientSetupEvent event)
	{
		for(DeferredHolder<Block, ? extends Block> blockDef : Stream.concat(MITweaksBlocks.Registry.BLOCKS.getEntries().stream(), MITweaksMIRegistries.BLOCKS.getEntries().stream()).toList())
		{
			if(blockDef.get() instanceof MachineBlock machine)
			{
				MachineBlockEntity blockEntity = machine.getBlockEntityInstance();
				BlockEntityType type = blockEntity.getType();
				
				BlockEntityRendererProvider provider = switch (blockEntity)
				{
					case MultiblockMachineBlockEntity be -> MultiblockMachineBER::new;
					default -> MachineBlockEntityRenderer::new;
				};
				BlockEntityRenderers.register(type, provider);
			}
		}
	}
	
	@SubscribeEvent
	private static void init(FMLConstructModEvent __)
	{
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, (event) ->
		{
			if(!MITweaks.config().machineBlueprints().learning() &&
			   (MITweaks.config().machineBlueprints().required().tooltip().isLearning() ||
				MITweaks.config().machineBlueprints().required().placing().isLearning() ||
				MITweaks.config().machineBlueprints().required().renderingHatches().isLearning()))
			{
				event.getPlayer().displayClientMessage(MITweaks.text().learningDisabledButRequiringLearning(), false);
			}
		});
	}
	
	@SubscribeEvent
	private static void onModifyBakingResult(ModelEvent.RegisterAdditional event)
	{
		event.register(RAW_ITEM_MODEL_LOCATION);
	}
}

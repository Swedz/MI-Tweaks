package net.swedz.mi_tweaks.items;

import aztech.modern_industrialization.machines.MachineBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.mi_tweaks.blueprint.BlueprintsLearned;
import net.swedz.mi_tweaks.items.renderer.MachineBlueprintItemRenderer;
import net.swedz.mi_tweaks.packets.UpdateBlueprintsLearnedPacket;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static aztech.modern_industrialization.MITooltips.*;

public final class MachineBlueprintItem extends Item
{
	public MachineBlueprintItem(Properties properties)
	{
		super(properties.stacksTo(1));
	}
	
	@Override
	public Component getName(ItemStack stack)
	{
		if(getMachineBlock(stack).isPresent())
		{
			return super.getName(stack);
		}
		else
		{
			return Component.translatable(this.getDescriptionId() + ".blank");
		}
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer()
			{
				return new MachineBlueprintItemRenderer();
			}
		});
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		Player player = Minecraft.getInstance().player;
		
		getMachineBlock(stack).ifPresent((machineBlock) ->
		{
			tooltipComponents.add(MITweaksText.BLUEPRINT_MACHINE.text(ITEM_PARSER.parse(machineBlock.asItem())));
			
			if(player != null &&
			   MITweaksConfig.machineBlueprintsLearning &&
			   !hasBlueprintLearned(player, machineBlock))
			{
				tooltipComponents.add(MITweaksText.BLUEPRINT_LEARN.text(Component.keybind("key.use")).withStyle(DEFAULT_STYLE));
			}
		});
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		ItemStack stack = player.getItemInHand(usedHand);
		Optional<Block> machineBlockOptional = getMachineBlock(stack);
		if(machineBlockOptional.isPresent())
		{
			Block machineBlock = machineBlockOptional.get();
			BlueprintsLearned blueprintsLearned = player.getData(MITweaksOtherRegistries.BLUEPRINTS_LEARNED);
			if(!blueprintsLearned.hasLearned(machineBlock))
			{
				if(!level.isClientSide)
				{
					blueprintsLearned.learn(machineBlock);
					new UpdateBlueprintsLearnedPacket(blueprintsLearned).sendToClient((ServerPlayer) player);
					player.displayClientMessage(MITweaksText.BLUEPRINT_LEARNED.text(ITEM_PARSER.parse(machineBlock.asItem())).withStyle(ChatFormatting.GREEN), true);
				}
				return InteractionResultHolder.consume(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}
	
	public static void setMachineBlock(ItemStack stack, Block machineBlock)
	{
		if(!stack.is(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
		{
			throw new IllegalArgumentException("Cannot set machine block value of a non-blueprint item");
		}
		if(!(machineBlock instanceof MachineBlock))
		{
			throw new IllegalArgumentException("Cannot set machine block value to a non-machine block");
		}
		if(!MITweaksConfig.machineBlueprintsMachines.contains(machineBlock))
		{
			throw new IllegalArgumentException("Cannot set machine block value to a machine block that is not included in the config");
		}
		
		CompoundTag tag = stack.getOrCreateTag();
		tag.putString("machine", BuiltInRegistries.BLOCK.getKey(machineBlock).toString());
	}
	
	public static Optional<Block> getMachineBlock(ItemStack stack)
	{
		if(!stack.is(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
		{
			throw new IllegalArgumentException("Cannot get machine block value of a non-blueprint item");
		}
		
		CompoundTag tag = stack.getOrCreateTag();
		if(tag.contains("machine"))
		{
			String machineId = tag.getString("machine");
			Optional<Block> machineBlockOptional = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(machineId));
			if(machineBlockOptional.isPresent() && machineBlockOptional.get() instanceof MachineBlock machineBlock &&
			   MITweaksConfig.machineBlueprintsMachines.contains(machineBlock))
			{
				return machineBlockOptional;
			}
		}
		return Optional.empty();
	}
	
	private static boolean hasBlueprintInInventory(Player player, Block machineBlock)
	{
		Inventory playerInventory = player.getInventory();
		return Stream.concat(playerInventory.items.stream(), playerInventory.offhand.stream())
				.anyMatch((stack) ->
				{
					if(stack.getItem().equals(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
					{
						Optional<Block> machineBlockOptional = getMachineBlock(stack);
						return machineBlockOptional.isPresent() && machineBlockOptional.get().equals(machineBlock);
					}
					return false;
				});
	}
	
	private static boolean hasBlueprintLearned(Player player, Block machineBlock)
	{
		BlueprintsLearned blueprintsLearned = player.getData(MITweaksOtherRegistries.BLUEPRINTS_LEARNED);
		return blueprintsLearned.hasLearned(machineBlock);
	}
	
	public static boolean hasBlueprint(Player player, Block machineBlock, MITweaksConfig.MachineBlueprintRequiredMode requiredMode)
	{
		return switch (requiredMode)
				{
					case DISABLED -> true;
					case INVENTORY -> hasBlueprintInInventory(player, machineBlock);
					case LEARN -> hasBlueprintLearned(player, machineBlock);
					case INVENTORY_OR_LEARN -> hasBlueprintLearned(player, machineBlock) ||
											   hasBlueprintInInventory(player, machineBlock);
				};
	}
}

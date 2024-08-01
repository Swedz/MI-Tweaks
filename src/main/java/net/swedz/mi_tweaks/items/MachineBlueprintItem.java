package net.swedz.mi_tweaks.items;

import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.machines.multiblocks.SimpleMember;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksDataComponents;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.mi_tweaks.blueprint.BlueprintsLearned;
import net.swedz.mi_tweaks.packets.UpdateBlueprintsLearnedPacket;
import net.swedz.mi_tweaks.proxy.CommonProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static aztech.modern_industrialization.MITooltips.*;

public final class MachineBlueprintItem extends Item
{
	public static final ModelResourceLocation RAW_ITEM_MODEL_LOCATION = ModelResourceLocation.standalone(MITweaks.id("item/machine_blueprint_raw"));
	
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
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		if(CommonProxy.INSTANCE.isClient())
		{
			Player player = CommonProxy.INSTANCE.getClientPlayer();
			
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
	}
	
	private static Optional<ItemStack> getItemStackMatchingFromInventory(SimpleMember member, Player player)
	{
		if(player.isCreative())
		{
			return Optional.of(member.getPreviewState().getBlock().asItem().getDefaultInstance());
		}
		for(ItemStack item : player.getInventory().items)
		{
			if(item.getItem() instanceof BlockItem blockItem &&
			   member.matchesState(blockItem.getBlock().defaultBlockState()))
			{
				return Optional.of(item);
			}
		}
		return Optional.empty();
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
					player.swing(usedHand, true);
				}
				return InteractionResultHolder.consume(stack);
			}
			else
			{
				return InteractionResultHolder.fail(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		Player player = context.getPlayer();
		InteractionHand usedHand = context.getHand();
		Level level = context.getLevel();
		Optional<Block> machineBlockOptional = getMachineBlock(stack);
		if(machineBlockOptional.isPresent())
		{
			Block machineBlock = machineBlockOptional.get();
			
			if(level.getBlockState(context.getClickedPos()).is(machineBlock) &&
			   level.getBlockEntity(context.getClickedPos()) instanceof MultiblockMachineBlockEntity multiblockMachine &&
			   !multiblockMachine.isShapeValid())
			{
				ShapeMatcher matcher = new ShapeMatcher(
						level,
						multiblockMachine.getBlockPos(),
						multiblockMachine.getOrientation().facingDirection,
						multiblockMachine.getActiveShape()
				);
				List<BlockPos> sortedPositions = new ArrayList<>(matcher.getPositions());
				Collections.sort(sortedPositions);
				for(BlockPos pos : sortedPositions)
				{
					SimpleMember member = matcher.getSimpleMember(pos);
					if(!matcher.matches(pos, level, null) &&
					   level.getBlockState(pos).isAir())
					{
						Optional<ItemStack> memberStackOptional = getItemStackMatchingFromInventory(member, player);
						if(memberStackOptional.isPresent())
						{
							if(!level.isClientSide)
							{
								ItemStack memberStack = memberStackOptional.get();
								level.setBlock(pos, ((BlockItem) memberStack.getItem()).getBlock().defaultBlockState(), 1 | 2);
								if(!player.getAbilities().instabuild)
								{
									memberStack.shrink(1);
								}
								player.swing(usedHand, true);
							}
							return InteractionResult.CONSUME;
						}
					}
				}
			}
		}
		return InteractionResult.PASS;
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
		
		stack.set(MITweaksDataComponents.MACHINE_BLOCK, machineBlock);
	}
	
	public static Optional<Block> getMachineBlock(ItemStack stack)
	{
		if(!stack.is(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
		{
			throw new IllegalArgumentException("Cannot get machine block value of a non-blueprint item");
		}
		
		if(stack.has(MITweaksDataComponents.MACHINE_BLOCK))
		{
			Block machine = stack.get(MITweaksDataComponents.MACHINE_BLOCK);
			if(machine instanceof MachineBlock machineBlock && MITweaksConfig.machineBlueprintsMachines.contains(machineBlock))
			{
				return Optional.of(machine);
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

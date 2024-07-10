package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBER;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(MultiblockMachineBER.class)
public class MultiblockRendersWithBlueprintClientMixin
{
	@Shadow
	private static boolean isHoldingWrench()
	{
		throw new UnsupportedOperationException();
	}
	
	@Redirect(
			method = "render(Laztech/modern_industrialization/machines/multiblocks/MultiblockMachineBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/multiblocks/MultiblockMachineBER;isHoldingWrench()Z"
			)
	)
	private boolean isHoldingBlueprint(MultiblockMachineBlockEntity be, float tickDelta, PoseStack matrices, MultiBufferSource vcp, int light, int overlay)
	{
		if(MITweaksConfig.wrenchesRenderMultiblockShapes && isHoldingWrench())
		{
			return true;
		}
		
		Player player = Minecraft.getInstance().player;
		ItemStack blueprintItem =
				player.getMainHandItem().is(MITweaksItems.MACHINE_BLUEPRINT.asItem()) ? player.getMainHandItem() :
				player.getOffhandItem().is(MITweaksItems.MACHINE_BLUEPRINT.asItem()) ? player.getOffhandItem() : null;
		if(blueprintItem != null)
		{
			Optional<Block> blueprintMachineBlockOptional = MachineBlueprintItem.getMachineBlock(blueprintItem);
			if(blueprintMachineBlockOptional.isPresent())
			{
				Block blueprintMachineBlock = blueprintMachineBlockOptional.get();
				Block machineBlock = be.getBlockState().getBlock();
				return blueprintMachineBlock.equals(machineBlock);
			}
		}
		return false;
	}
}

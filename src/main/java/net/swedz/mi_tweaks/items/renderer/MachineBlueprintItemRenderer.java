package net.swedz.mi_tweaks.items.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.swedz.mi_tweaks.MITweaks;

public final class MachineBlueprintItemRenderer extends BlockEntityWithoutLevelRenderer
{
	public MachineBlueprintItemRenderer()
	{
		super(null, null);
	}
	
	@Override
	public void renderByItem(
			ItemStack stack, ItemDisplayContext displayContext,
			PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay
	)
	{
		MITweaks.LOGGER.info("test");
		
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, displayContext, packedLight, packedOverlay, poseStack, buffer, null, 0);
		
		poseStack.pushPose();
		
		poseStack.translate(0.5, 1, 0.5);
		poseStack.scale(0.5f, 0.5f, 0.5f);
		
		BlockState blockState = Blocks.STONE.defaultBlockState();
		BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
		
		blockRenderer.renderSingleBlock(blockState, poseStack, buffer, packedLight, packedOverlay);
		
		poseStack.popPose();
	}
}

package net.swedz.mi_tweaks.items.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;

import java.util.Optional;

public final class MachineBlueprintItemRenderer extends BlockEntityWithoutLevelRenderer
{
	public static final ResourceLocation MODEL_LOCATION = MITweaks.id("item/machine_blueprint_raw");
	
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
		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		
		poseStack.pushPose();
		
		poseStack.translate(0.5, 0.5, 0.5);
		BakedModel model = renderer.getItemModelShaper().getModelManager().getModel(MODEL_LOCATION);
		renderer.render(stack, displayContext, isLeftHand(displayContext), poseStack, buffer, packedLight, packedOverlay, model);
		
		if(displayContext == ItemDisplayContext.GUI)
		{
			Optional<Block> machineBlock = MachineBlueprintItem.getMachineBlock(stack);
			if(machineBlock.isPresent())
			{
				poseStack.pushPose();
				poseStack.popPose();
				
				poseStack.translate(0, -0.37, 0);
				poseStack.scale(0.4f, 0.4f, 0.4f);
				poseStack.rotateAround(Axis.XP.rotationDegrees(30), 0.5f, 0.5f, 0.5f);
				poseStack.rotateAround(Axis.YP.rotationDegrees(225), 0.5f, 0.5f, 0.5f);
				BlockState blockState = machineBlock.get().defaultBlockState();
				BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
				blockRenderer.renderSingleBlock(blockState, poseStack, buffer, packedLight, packedOverlay, ModelData.EMPTY, null);
			}
		}
		
		poseStack.popPose();
	}
	
	private static boolean isLeftHand(ItemDisplayContext context)
	{
		return context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ||
			   context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
	}
}

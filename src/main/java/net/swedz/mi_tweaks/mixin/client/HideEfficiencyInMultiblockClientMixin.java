package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.client.machines.guicomponents.CraftingMultiblockGuiClient;
import aztech.modern_industrialization.machines.guicomponents.CraftingMultiblockGui;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.swedz.mi_tweaks.MITweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(
		value = CraftingMultiblockGuiClient.Renderer.class,
		remap = false
)
public class HideEfficiencyInMultiblockClientMixin
{
	@ModifyExpressionValue(
			method = "renderBackground",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/guicomponents/CraftingMultiblockGui$Data;activeRecipe()Ljava/util/Optional;"
			)
	)
	private Optional<CraftingMultiblockGui.RecipeData> renderBackground(Optional<CraftingMultiblockGui.RecipeData> original)
	{
		if(MITweaks.config().efficiency().hide() && original.isPresent())
		{
			var recipeData = original.get();
			return Optional.of(new CraftingMultiblockGui.RecipeData(
					recipeData.progress(),
					0,
					0,
					recipeData.currentRecipeEu(),
					recipeData.baseRecipeEu()
			));
		}
		return original;
	}
}

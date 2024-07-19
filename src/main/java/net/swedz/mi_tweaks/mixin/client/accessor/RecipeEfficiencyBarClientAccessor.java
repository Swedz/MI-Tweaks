package net.swedz.mi_tweaks.mixin.client.accessor;

import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBarClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = RecipeEfficiencyBarClient.class,
		remap = false
)
public interface RecipeEfficiencyBarClientAccessor
{
	@Accessor
	long getCurrentRecipeEu();
}

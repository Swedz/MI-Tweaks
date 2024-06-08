package net.swedz.mi_tweaks.mixin.constantefficiency;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = CrafterComponent.Behavior.class,
		remap = false
)
public interface ConstantEfficiencyCrafterComponentBehaviorMixin
{
	@Redirect(
			method = "banRecipe",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/CrafterComponent$Behavior;getMaxRecipeEu()J"
			)
	)
	private long getMaxRecipeEu(CrafterComponent.Behavior behavior)
	{
		return ConstantEfficiencyHelper.getActualMaxRecipeEu(behavior, behavior);
	}
}

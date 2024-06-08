package net.swedz.mi_tweaks.mixin.constantefficiency;

import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = ModularCrafterAccessBehavior.class,
		remap = false
)
public interface ConstantEfficiencyModularCrafterComponentBehaviorMixin
{
	@Inject(
			method = "getMaxRecipeEu",
			at = @At("HEAD"),
			cancellable = true
	)
	private void getMaxRecipeEu(CallbackInfoReturnable<Long> callback)
	{
		ModularCrafterAccessBehavior behavior = (ModularCrafterAccessBehavior) this;
		callback.setReturnValue(ConstantEfficiencyHelper.getActualMaxRecipeEu(behavior, behavior));
	}
}

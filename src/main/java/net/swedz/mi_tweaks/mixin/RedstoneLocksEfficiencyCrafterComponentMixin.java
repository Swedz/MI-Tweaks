package net.swedz.mi_tweaks.mixin;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.AbstractModularCrafterComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = {
				CrafterComponent.class,
				AbstractModularCrafterComponent.class
		},
		remap = false,
		priority = 9999
)
public class RedstoneLocksEfficiencyCrafterComponentMixin
{
	@Inject(
			method = "tickRecipe",
			at = @At("HEAD"),
			cancellable = true
	)
	private void tickRecipe(CallbackInfoReturnable<Boolean> callback)
	{
		if(MITweaksConfig.lockEfficiencyWithRedstone)
		{
			boolean enabled;
			if((Object) this instanceof CrafterComponent crafter)
			{
				enabled = crafter.getBehavior().isEnabled();
			}
			else if((Object) this instanceof AbstractModularCrafterComponent crafter)
			{
				enabled = crafter.getBehavior().isEnabled();
			}
			else
			{
				throw new IllegalStateException();
			}
			
			if(!enabled)
			{
				callback.setReturnValue(false);
			}
		}
	}
}

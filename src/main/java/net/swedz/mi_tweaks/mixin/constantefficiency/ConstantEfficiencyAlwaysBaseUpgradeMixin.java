package net.swedz.mi_tweaks.mixin.constantefficiency;

import aztech.modern_industrialization.compat.viewer.usage.MachineCategory;
import aztech.modern_industrialization.machines.init.MachineTier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = MachineCategory.class,
		remap = false
)
public class ConstantEfficiencyAlwaysBaseUpgradeMixin
{
	@Redirect(
			method = "buildWidgets(Lnet/minecraft/world/item/crafting/RecipeHolder;Laztech/modern_industrialization/compat/viewer/abstraction/ViewerCategory$WidgetList;)V",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/init/MachineTier;getMaxEu()I",
					ordinal = 1
			)
	)
	private int upgradeEuRequired(MachineTier tier)
	{
		return tier.getBaseEu();
	}
}

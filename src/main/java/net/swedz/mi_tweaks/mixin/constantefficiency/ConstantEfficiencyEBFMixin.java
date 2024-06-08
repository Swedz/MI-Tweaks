package net.swedz.mi_tweaks.mixin.constantefficiency;

import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import net.swedz.mi_tweaks.constantefficiency.ConstantEfficiencyHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = ElectricBlastFurnaceBlockEntity.class,
		remap = false
)
public class ConstantEfficiencyEBFMixin
{
	@Redirect(
			method = "banRecipe",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/blockentities/multiblocks/ElectricBlastFurnaceBlockEntity;getMaxRecipeEu()J"
			)
	)
	private long getMaxRecipeEu(ElectricBlastFurnaceBlockEntity behavior)
	{
		return ConstantEfficiencyHelper.getActualMaxRecipeEu(behavior, behavior);
	}
}

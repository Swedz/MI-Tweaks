package net.swedz.mi_tweaks.mixin;

import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.blockentities.multiblocks.SteamBoilerMultiblockBlockEntity;
import aztech.modern_industrialization.machines.components.FuelBurningComponent;
import net.swedz.mi_tweaks.MITweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(
		value = SteamBoilerMultiblockBlockEntity.class,
		remap = false
)
public class DisableItemFuelBoilerMultiblockMixin
{
	@Redirect(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/FuelBurningComponent;tick(Ljava/util/List;Ljava/util/List;)V"
			)
	)
	private void fuelTick(FuelBurningComponent fuelBurning, List<ConfigurableItemStack> items, List<ConfigurableFluidStack> fluids)
	{
		fuelBurning.tick(MITweaks.config().tweaks().disableItemFuelInMultiblockBoilers() ? List.of() : items, fluids);
	}
}

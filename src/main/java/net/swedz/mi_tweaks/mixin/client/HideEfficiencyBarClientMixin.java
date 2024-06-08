package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.machines.gui.MachineScreen;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBarClient;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.recipeefficiency.ModularRecipeEfficiencyBarClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = {
				RecipeEfficiencyBarClient.class,
				ModularRecipeEfficiencyBarClient.class
		},
		remap = false
)
public class HideEfficiencyBarClientMixin
{
	@Inject(
			method = "createRenderer",
			at = @At("HEAD"),
			cancellable = true
	)
	private void createRenderer(MachineScreen machineScreen, CallbackInfoReturnable<ClientComponentRenderer> callback)
	{
		if(MITweaksConfig.hideMachineEfficiency)
		{
			callback.setReturnValue((guiGraphics, leftPos, topPos) ->
			{
			});
		}
	}
}

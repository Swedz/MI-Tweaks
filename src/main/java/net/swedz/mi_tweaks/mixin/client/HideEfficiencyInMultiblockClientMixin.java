package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.machines.guicomponents.CraftingMultiblockGuiClient;
import net.minecraft.network.FriendlyByteBuf;
import net.swedz.mi_tweaks.MITweaksConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = CraftingMultiblockGuiClient.class,
		remap = false
)
public class HideEfficiencyInMultiblockClientMixin
{
	@Shadow
	int efficiencyTicks;
	
	@Shadow
	int maxEfficiencyTicks;
	
	@Inject(
			method = "readCurrentData",
			at = @At("RETURN")
	)
	private void readCurrentData(FriendlyByteBuf buf, CallbackInfo callback)
	{
		if(MITweaksConfig.hideMachineEfficiency)
		{
			efficiencyTicks = 0;
			maxEfficiencyTicks = 0;
		}
	}
}

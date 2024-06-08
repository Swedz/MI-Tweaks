package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.machines.guicomponents.CraftingMultiblockGuiClient;
import net.minecraft.network.FriendlyByteBuf;
import net.swedz.mi_tweaks.MITweaksConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMultiblockGuiClient.class)
public class HideEfficiencyInMultiblockClientMixin
{
	@Shadow
	int efficiencyTicks;
	
	@Shadow
	int maxEfficiencyTicks;
	
	@Inject(
			method = "readCurrentData",
			at = @At("RETURN"),
			remap = false
	)
	private void readCurrentData(FriendlyByteBuf buf, CallbackInfo callback)
	{
		if(MITweaksConfig.machineEfficiencyHack.hideEfficiency())
		{
			efficiencyTicks = 0;
			maxEfficiencyTicks = 0;
		}
	}
}

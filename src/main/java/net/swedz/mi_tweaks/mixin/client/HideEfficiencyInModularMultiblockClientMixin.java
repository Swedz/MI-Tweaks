package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.MIText;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiClient;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(
		value = ModularMultiblockGuiClient.class,
		remap = false
)
public class HideEfficiencyInModularMultiblockClientMixin
{
	@Shadow
	private List<ModularMultiblockGuiLine> text;
	
	@Inject(
			method = "readCurrentData",
			at = @At("RETURN")
	)
	private void readCurrentData(FriendlyByteBuf buf, CallbackInfo callback)
	{
		if(MITweaksConfig.hideMachineEfficiency)
		{
			text.removeIf((line) ->
					line.text().getContents() instanceof TranslatableContents translatable &&
							translatable.getKey().equals(MIText.EfficiencyTicks.getTranslationKey()));
		}
	}
}

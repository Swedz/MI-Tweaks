package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.MIText;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiClient;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;
import java.util.List;

@Mixin(
		value = ModularMultiblockGuiClient.Renderer.class,
		remap = false
)
public class HideEfficiencyInModularMultiblockClientMixin
{
	@ModifyExpressionValue(
			method = "renderInfoText",
			at = @At(
					value = "INVOKE",
					target = "Lnet/swedz/tesseract/neoforge/compat/mi/guicomponent/modularmultiblock/ModularMultiblockGuiContent;lines()Ljava/util/List;"
			)
	)
	private List<ModularMultiblockGuiLine> renderInfoText(List<ModularMultiblockGuiLine> original)
	{
		if(MITweaks.config().efficiency().hide())
		{
			List<ModularMultiblockGuiLine> copy = Lists.newArrayList(original);
			copy.removeIf((line) ->
					line.text().getContents() instanceof TranslatableContents translatable &&
					translatable.getKey().equals(MIText.EfficiencyTicks.getTranslationKey()));
			return Collections.unmodifiableList(copy);
		}
		return original;
	}
}

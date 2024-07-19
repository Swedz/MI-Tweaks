package net.swedz.mi_tweaks.mixin.client;

import aztech.modern_industrialization.machines.gui.MachineScreen;
import aztech.modern_industrialization.machines.guicomponents.EnergyBarClient;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBarClient;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.MITweaksText;
import net.swedz.mi_tweaks.mixin.client.accessor.RecipeEfficiencyBarClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;

@Mixin(
		value = EnergyBarClient.Renderer.class,
		remap = false
)
public class DisplayEnergyConsumptionOnEnergyBarClientMixin
{
	@Redirect(
			method = "renderTooltip",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V"
			)
	)
	private void tooltipLines(GuiGraphics graphics, Font font, List<Component> originalTooltipLines, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY,
							  MachineScreen screen)
	{
		List<Component> tooltipLines = Lists.newArrayList();
		
		tooltipLines.addAll(originalTooltipLines);
		
		if(MITweaksConfig.displayEnergyConsumptionOnEnergyBar)
		{
			RecipeEfficiencyBarClient efficiencyBar = screen.getMenu().getComponent(RecipeEfficiencyBarClient.class);
			if(efficiencyBar != null)
			{
				long currentRecipeEu = ((RecipeEfficiencyBarClientAccessor) efficiencyBar).getCurrentRecipeEu();
				if(currentRecipeEu > 0)
				{
					tooltipLines.add(MITweaksText.ENERGY_BAR_CURRENT_CONSUMPTION.text(currentRecipeEu));
				}
			}
		}
		
		graphics.renderTooltip(font, List.copyOf(tooltipLines), visualTooltipComponent, mouseX, mouseY);
	}
}

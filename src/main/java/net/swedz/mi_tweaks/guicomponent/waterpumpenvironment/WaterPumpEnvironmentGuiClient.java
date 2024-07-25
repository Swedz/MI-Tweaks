package net.swedz.mi_tweaks.guicomponent.waterpumpenvironment;

import aztech.modern_industrialization.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.machines.gui.MachineScreen;
import aztech.modern_industrialization.util.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.FormattedCharSequence;
import net.swedz.mi_tweaks.MITweaksText;

import java.util.List;

public final class WaterPumpEnvironmentGuiClient implements GuiComponentClient
{
	private final WaterPumpEnvironmentGui.Parameters params;
	
	private boolean validEnvironment;
	
	public WaterPumpEnvironmentGuiClient(RegistryFriendlyByteBuf buf)
	{
		this.params = new WaterPumpEnvironmentGui.Parameters(buf.readInt(), buf.readInt());
		this.readCurrentData(buf);
	}
	
	@Override
	public void readCurrentData(RegistryFriendlyByteBuf buf)
	{
		validEnvironment = buf.readBoolean();
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		return new Renderer();
	}
	
	private final class Renderer implements ClientComponentRenderer
	{
		@Override
		public void renderBackground(GuiGraphics guiGraphics, int x, int y)
		{
			if(!validEnvironment)
			{
				int px = x + params.renderX();
				int py = y + params.renderY();
				guiGraphics.blit(MachineScreen.SLOT_ATLAS, px, py, 20, 58, 20, 20);
			}
		}
		
		@Override
		public void renderTooltip(MachineScreen screen, Font font, GuiGraphics guiGraphics, int x, int y, int cursorX, int cursorY)
		{
			if(!validEnvironment)
			{
				if(RenderHelper.isPointWithinRectangle(params.renderX(), params.renderY(), 20, 20, cursorX - x, cursorY - y))
				{
					List<FormattedCharSequence> lines = List.of(
							MITweaksText.WATER_PUMP_ENVIRONMENT_1.text().withStyle(ChatFormatting.RED).getVisualOrderText(),
							MITweaksText.WATER_PUMP_ENVIRONMENT_2.text().withStyle(ChatFormatting.RED).getVisualOrderText()
					);
					guiGraphics.renderTooltip(font, lines, cursorX, cursorY);
				}
			}
		}
	}
}

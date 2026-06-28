package net.swedz.mi_tweaks.machine.guicomponent.waterpumpenvironment;

import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import aztech.modern_industrialization.client.util.RenderHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.swedz.mi_tweaks.MITweaks;

import java.util.List;
import java.util.Optional;

public final class WaterPumpEnvironmentGuiClient extends GuiComponentClient<WaterPumpEnvironmentGui.Params, Boolean>
{
	public WaterPumpEnvironmentGuiClient(WaterPumpEnvironmentGui.Params params, Boolean data)
	{
		super(params, data);
	}
	
	public boolean isValidEnvironment()
	{
		return data;
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
			if(!WaterPumpEnvironmentGuiClient.this.isValidEnvironment())
			{
				int px = x + params.renderX();
				int py = y + params.renderY();
				guiGraphics.blit(MachineScreen.SLOT_ATLAS, px, py, 20, 58, 20, 20);
			}
		}
		
		@Override
		public boolean renderTooltip(MachineScreen screen, Font font, GuiGraphics graphics, int x, int y, int cursorX, int cursorY)
		{
			if(!WaterPumpEnvironmentGuiClient.this.isValidEnvironment())
			{
				if(RenderHelper.isPointWithinRectangle(params.renderX(), params.renderY(), 20, 20, cursorX - x, cursorY - y))
				{
					List<Component> lines = List.of(
							MITweaks.text().waterPumpEnvironment1(),
							MITweaks.text().waterPumpEnvironment2()
					);
					graphics.renderTooltip(font, lines, Optional.empty(), cursorX, cursorY);
					return true;
				}
			}
			return false;
		}
	}
}

package net.swedz.mi_tweaks.machine.guicomponent.exposecabletier;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import net.minecraft.util.Unit;

public class ExposeCableTierGuiClient extends GuiComponentClient<Unit, CableTier>
{
	public ExposeCableTierGuiClient(Unit params, CableTier data)
	{
		super(params, data);
	}
	
	public CableTier getCableTier()
	{
		return data;
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		return (guiGraphics, leftPos, topPos) ->
		{
		};
	}
}

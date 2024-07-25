package net.swedz.mi_tweaks.guicomponent.exposecabletier;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.machines.gui.MachineScreen;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ExposeCableTierGuiClient implements GuiComponentClient
{
	private CableTier cableTier;
	
	public ExposeCableTierGuiClient(RegistryFriendlyByteBuf buf)
	{
		this.readCurrentData(buf);
	}
	
	public CableTier getCableTier()
	{
		return cableTier;
	}
	
	@Override
	public void readCurrentData(RegistryFriendlyByteBuf buf)
	{
		cableTier = CableTier.getTier(new String(buf.readByteArray()));
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		return (guiGraphics, leftPos, topPos) ->
		{
		};
	}
}

package net.swedz.mi_tweaks.guicomponent.exposecabletier;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.gui.GuiComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.api.CableTierHolder;

public class ExposeCableTierGui
{
	public static final ResourceLocation ID = MITweaks.id("expose_cable_tier");
	
	public static final class Server implements GuiComponent.Server<CableTier>
	{
		private final CableTierHolder cableTierHolder;
		
		public Server(CableTierHolder cableTierHolder)
		{
			this.cableTierHolder = cableTierHolder;
		}
		
		@Override
		public CableTier copyData()
		{
			return cableTierHolder.getCableTier();
		}
		
		@Override
		public boolean needsSync(CableTier cachedData)
		{
			return cachedData != cableTierHolder.getCableTier();
		}
		
		@Override
		public void writeInitialData(RegistryFriendlyByteBuf buf)
		{
			this.writeCurrentData(buf);
		}
		
		@Override
		public void writeCurrentData(RegistryFriendlyByteBuf buf)
		{
			buf.writeByteArray(cableTierHolder.getCableTier().name.getBytes());
		}
		
		@Override
		public ResourceLocation getId()
		{
			return ID;
		}
	}
}

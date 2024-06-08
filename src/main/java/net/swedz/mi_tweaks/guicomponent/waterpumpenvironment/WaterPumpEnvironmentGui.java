package net.swedz.mi_tweaks.guicomponent.waterpumpenvironment;

import aztech.modern_industrialization.machines.gui.GuiComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.swedz.mi_tweaks.MITweaks;

import java.util.function.Supplier;

public final class WaterPumpEnvironmentGui
{
	public static ResourceLocation ID = MITweaks.id("water_pump_environment");
	
	public static final class Server implements GuiComponent.Server<Boolean>
	{
		public final Parameters params;
		public final Supplier<Boolean>                  validEnvironmentSupplier;
		
		public Server(Parameters params, Supplier<Boolean> validEnvironmentSupplier)
		{
			this.params = params;
			this.validEnvironmentSupplier = validEnvironmentSupplier;
		}
		
		@Override
		public Boolean copyData()
		{
			return validEnvironmentSupplier.get();
		}
		
		@Override
		public boolean needsSync(Boolean cachedData)
		{
			return !cachedData.equals(validEnvironmentSupplier.get());
		}
		
		@Override
		public void writeInitialData(FriendlyByteBuf buf)
		{
			buf.writeInt(params.renderX);
			buf.writeInt(params.renderY);
			this.writeCurrentData(buf);
		}
		
		@Override
		public void writeCurrentData(FriendlyByteBuf buf)
		{
			buf.writeBoolean(validEnvironmentSupplier.get());
		}
		
		@Override
		public ResourceLocation getId()
		{
			return ID;
		}
	}
	
	public record Parameters(int renderX, int renderY)
	{
	}
}

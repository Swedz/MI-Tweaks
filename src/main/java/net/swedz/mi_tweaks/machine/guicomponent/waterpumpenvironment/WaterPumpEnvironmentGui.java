package net.swedz.mi_tweaks.machine.guicomponent.waterpumpenvironment;

import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.swedz.mi_tweaks.MITweaks;

import java.util.function.Supplier;

public final class WaterPumpEnvironmentGui implements GuiComponentServer<WaterPumpEnvironmentGui.Params, Boolean>
{
	public static final Type<Params, Boolean> TYPE = new Type<>(MITweaks.id("water_pump_environment"), Params.STREAM_CODEC, ByteBufCodecs.BOOL);
	
	private final Params params;
	private final Supplier<Boolean> validEnvironmentSupplier;
	
	public WaterPumpEnvironmentGui(Params params, Supplier<Boolean> validEnvironmentSupplier)
	{
		this.params = params;
		this.validEnvironmentSupplier = validEnvironmentSupplier;
	}
	
	@Override
	public Params getParams()
	{
		return params;
	}
	
	@Override
	public Boolean extractData()
	{
		return validEnvironmentSupplier.get();
	}
	
	@Override
	public Type<Params, Boolean> getType()
	{
		return TYPE;
	}
	
	public record Params(int renderX, int renderY)
	{
		public static final StreamCodec<ByteBuf, Params> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT, Params::renderX,
				ByteBufCodecs.VAR_INT, Params::renderY,
				Params::new
		);
	}
}

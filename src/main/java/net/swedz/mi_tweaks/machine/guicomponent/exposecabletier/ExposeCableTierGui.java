package net.swedz.mi_tweaks.machine.guicomponent.exposecabletier;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.CableTierHolder;
import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.serialization.MICodecs;

public class ExposeCableTierGui implements GuiComponentServer<Unit, CableTier>
{
	public static final Type<Unit, CableTier> TYPE = new Type<>(MITweaks.id("expose_cable_tier"), StreamCodec.unit(Unit.INSTANCE), ByteBufCodecs.fromCodec(MICodecs.CABLE_TIER));
	
	private final CableTierHolder cableTierHolder;
	
	public ExposeCableTierGui(CableTierHolder cableTierHolder)
	{
		this.cableTierHolder = cableTierHolder;
	}
	
	@Override
	public Unit getParams()
	{
		return Unit.INSTANCE;
	}
	
	@Override
	public CableTier extractData()
	{
		return cableTierHolder.getCableTier();
	}
	
	@Override
	public Type<Unit, CableTier> getType()
	{
		return TYPE;
	}
}

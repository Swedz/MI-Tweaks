package net.swedz.mi_tweaks.network;

import net.swedz.tesseract.neoforge.packet.CustomPacket;

public interface MITweaksCustomPacket extends CustomPacket
{
	@Override
	default Type<MITweaksCustomPacket> type()
	{
		return MITweaksPackets.getType(this.getClass());
	}
}

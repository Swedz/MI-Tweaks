package net.swedz.mi_tweaks.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.swedz.mi_tweaks.MITweaksPackets;

public interface BasePacket extends aztech.modern_industrialization.network.BasePacket
{
	@Override
	default Type<? extends CustomPacketPayload> type()
	{
		return MITweaksPackets.Registry.getType(this.getClass());
	}
}

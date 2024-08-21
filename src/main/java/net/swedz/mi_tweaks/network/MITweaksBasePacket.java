package net.swedz.mi_tweaks.network;

import aztech.modern_industrialization.network.BasePacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface MITweaksBasePacket extends BasePacket
{
	@Override
	default Type<? extends CustomPacketPayload> type()
	{
		return MITweaksPackets.Registry.getType(this.getClass());
	}
}

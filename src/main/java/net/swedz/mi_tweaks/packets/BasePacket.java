package net.swedz.mi_tweaks.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.swedz.mi_tweaks.MITweaksPackets;

public interface BasePacket extends CustomPacketPayload
{
	void write(FriendlyByteBuf buf);
	
	void handle();
	
	default void sendToServer()
	{
		PacketDistributor.SERVER.noArg().send(this);
	}
	
	default void sendToClient(ServerPlayer player)
	{
		PacketDistributor.PLAYER.with(player).send(this);
	}
	
	@Override
	default ResourceLocation id()
	{
		return MITweaksPackets.Registry.getId(this);
	}
}

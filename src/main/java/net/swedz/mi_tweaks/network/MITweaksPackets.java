package net.swedz.mi_tweaks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.network.packet.UpdateBlueprintsLearnedPacket;
import net.swedz.tesseract.neoforge.packet.PacketRegistry;

public final class MITweaksPackets
{
	private static final PacketRegistry<MITweaksCustomPacket> REGISTRY = PacketRegistry.create(MITweaks.ID);
	
	public static CustomPacketPayload.Type<MITweaksCustomPacket> getType(Class<? extends MITweaksCustomPacket> packetClass)
	{
		return REGISTRY.getType(packetClass);
	}
	
	public static void init(RegisterPayloadHandlersEvent event)
	{
		REGISTRY.registerAll(event);
	}
	
	static
	{
		create("update_blueprints_learned", UpdateBlueprintsLearnedPacket.class, UpdateBlueprintsLearnedPacket.STREAM_CODEC);
	}
	
	private static <P extends MITweaksCustomPacket> void create(String id, Class<P> packetClass, StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec)
	{
		REGISTRY.create(id, packetClass, packetCodec);
	}
}

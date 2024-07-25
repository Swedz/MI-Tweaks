package net.swedz.mi_tweaks;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.swedz.mi_tweaks.packets.BasePacket;
import net.swedz.mi_tweaks.packets.UpdateBlueprintsLearnedPacket;

import java.util.Map;
import java.util.Set;

public final class MITweaksPackets
{
	public static final class Registry
	{
		private static final Set<PacketRegistration<BasePacket>>                                    PACKET_REGISTRATIONS = Sets.newHashSet();
		private static final Map<Class<? extends BasePacket>, CustomPacketPayload.Type<BasePacket>> PACKET_TYPES         = Maps.newHashMap();
		
		private record PacketRegistration<P extends BasePacket>(
				CustomPacketPayload.Type<P> packetType,
				Class<P> packetClass,
				StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec
		)
		{
		}
		
		private static void init(RegisterPayloadHandlersEvent event)
		{
			PayloadRegistrar registrar = event.registrar(MITweaks.ID);
			
			for(PacketRegistration<BasePacket> packetRegistration : Registry.PACKET_REGISTRATIONS)
			{
				registrar.playBidirectional(packetRegistration.packetType(), packetRegistration.packetCodec(), (packet, context) ->
						packet.handle(new BasePacket.Context(packetRegistration.packetClass(), context)));
			}
		}
		
		public static CustomPacketPayload.Type<BasePacket> getType(Class<? extends BasePacket> packetClass)
		{
			return PACKET_TYPES.get(packetClass);
		}
	}
	
	public static void init(RegisterPayloadHandlersEvent event)
	{
		Registry.init(event);
	}
	
	static
	{
		register("update_blueprints_learned", UpdateBlueprintsLearnedPacket.class, UpdateBlueprintsLearnedPacket.STREAM_CODEC);
	}
	
	private static <P extends BasePacket> void register(String path, Class<P> packetClass, StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec)
	{
		CustomPacketPayload.Type type = new CustomPacketPayload.Type<>(MITweaks.id(path));
		Registry.PACKET_REGISTRATIONS.add(new Registry.PacketRegistration<>(type, packetClass, packetCodec));
		Registry.PACKET_TYPES.put(packetClass, type);
	}
}

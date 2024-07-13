package net.swedz.mi_tweaks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.swedz.mi_tweaks.packets.BasePacket;
import net.swedz.mi_tweaks.packets.UpdateBlueprintsLearnedPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MITweaksPackets
{
	public static final class Registry
	{
		private static final Map<Class<? extends BasePacket>, ResourceLocation> PACKET_IDS = new HashMap<>();
		private static final List<Registration<?>>                              PACKETS    = new ArrayList<>();
		
		private record Registration<P extends BasePacket>(
				ResourceLocation resourceLocation, Class<P> clazz,
				FriendlyByteBuf.Reader<P> packetConstructor
		)
		{
		}
		
		public static ResourceLocation getId(BasePacket packet)
		{
			return PACKET_IDS.get(packet.getClass());
		}
	}
	
	static
	{
		register("update_blueprints_learned", UpdateBlueprintsLearnedPacket.class, UpdateBlueprintsLearnedPacket::new);
	}
	
	public static void init(RegisterPayloadHandlerEvent event)
	{
		IPayloadRegistrar registrar = event.registrar(MITweaks.ID);
		
		for(Registry.Registration<?> registration : Registry.PACKETS)
		{
			registrar.play(registration.resourceLocation, registration.packetConstructor, (packet, context) ->
					context.workHandler().execute(packet::handle));
		}
	}
	
	private static <P extends BasePacket> void register(String path, Class<P> clazz, FriendlyByteBuf.Reader<P> packetConstructor)
	{
		Registry.PACKET_IDS.put(clazz, MITweaks.id(path));
		Registry.PACKETS.add(new Registry.Registration<>(MITweaks.id(path), clazz, packetConstructor));
	}
}

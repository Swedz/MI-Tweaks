package net.swedz.mi_tweaks.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;
import net.swedz.mi_tweaks.blueprint.BlueprintsLearned;
import net.swedz.mi_tweaks.network.MITweaksCustomPacket;
import net.swedz.tesseract.neoforge.packet.PacketContext;

import java.util.HashSet;
import java.util.Set;

public record UpdateBlueprintsLearnedPacket(Set<ResourceLocation> machineIds) implements MITweaksCustomPacket
{
	public static final StreamCodec<ByteBuf, UpdateBlueprintsLearnedPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(HashSet::new, ResourceLocation.STREAM_CODEC),
			UpdateBlueprintsLearnedPacket::machineIds,
			UpdateBlueprintsLearnedPacket::new
	);
	
	public UpdateBlueprintsLearnedPacket(BlueprintsLearned blueprintsLearned)
	{
		this(blueprintsLearned.get());
	}
	
	@Override
	public void handle(PacketContext context)
	{
		context.assetClientbound();
		
		Player player = context.getPlayer();
		
		BlueprintsLearned blueprintsLearned = player.getData(MITweaksOtherRegistries.BLUEPRINTS_LEARNED);
		blueprintsLearned.mergeFrom(machineIds);
	}
}

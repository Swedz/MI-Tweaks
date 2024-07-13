package net.swedz.mi_tweaks.packets;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;
import net.swedz.mi_tweaks.blueprint.BlueprintsLearned;

import java.util.Set;

public record UpdateBlueprintsLearnedPacket(Set<ResourceLocation> machineIds) implements BasePacket
{
	public UpdateBlueprintsLearnedPacket(BlueprintsLearned blueprintsLearned)
	{
		this(blueprintsLearned.get());
	}
	
	public UpdateBlueprintsLearnedPacket(FriendlyByteBuf buf)
	{
		this(Sets.newHashSet());
		
		int count = buf.readInt();
		for(int i = 0; i < count; i++)
		{
			machineIds.add(buf.readResourceLocation());
		}
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(machineIds.size());
		for(ResourceLocation machineId : machineIds)
		{
			buf.writeResourceLocation(machineId);
		}
	}
	
	@Override
	public void handle()
	{
		Player player = Minecraft.getInstance().player;
		
		if(player == null)
		{
			throw new IllegalArgumentException("Cannot handle packet on server: " + this.getClass());
		}
		
		BlueprintsLearned blueprintsLearned = player.getData(MITweaksOtherRegistries.BLUEPRINTS_LEARNED);
		blueprintsLearned.mergeFrom(machineIds);
	}
}

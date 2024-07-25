package net.swedz.mi_tweaks.blueprint;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Collections;
import java.util.Set;

public final class BlueprintsLearned implements INBTSerializable<ListTag>
{
	private Set<ResourceLocation> machinesLearned = Sets.newHashSet();
	
	private ResourceLocation getId(Block machineBlock)
	{
		return BuiltInRegistries.BLOCK.getKey(machineBlock);
	}
	
	public Set<ResourceLocation> get()
	{
		return Collections.unmodifiableSet(machinesLearned);
	}
	
	public void learn(Block machineBlock)
	{
		machinesLearned.add(this.getId(machineBlock));
	}
	
	public void unlearn(Block machineBlock)
	{
		machinesLearned.remove(this.getId(machineBlock));
	}
	
	public boolean hasLearned(Block machineBlock)
	{
		return machinesLearned.contains(this.getId(machineBlock));
	}
	
	public void mergeFrom(Set<ResourceLocation> machineIds)
	{
		machinesLearned = machineIds;
	}
	
	@Override
	public ListTag serializeNBT(HolderLookup.Provider registries)
	{
		ListTag list = new ListTag();
		for(ResourceLocation machineId : machinesLearned)
		{
			list.add(StringTag.valueOf(machineId.toString()));
		}
		return list;
	}
	
	@Override
	public void deserializeNBT(HolderLookup.Provider registries, ListTag list)
	{
		Set<ResourceLocation> machinesLearned = Sets.newHashSet();
		for(Tag tag : list)
		{
			if(tag instanceof StringTag stringTag)
			{
				machinesLearned.add(ResourceLocation.parse(stringTag.getAsString()));
			}
		}
		this.machinesLearned = machinesLearned;
	}
}

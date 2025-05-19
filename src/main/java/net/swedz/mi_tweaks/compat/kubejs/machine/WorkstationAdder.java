package net.swedz.mi_tweaks.compat.kubejs.machine;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class WorkstationAdder
{
	private final List<ResourceLocation> workstations = Lists.newArrayList();
	
	public WorkstationAdder add(ResourceLocation... workstations)
	{
		this.workstations.addAll(Lists.newArrayList(workstations));
		return this;
	}
	
	public List<ResourceLocation> get()
	{
		return workstations;
	}
}

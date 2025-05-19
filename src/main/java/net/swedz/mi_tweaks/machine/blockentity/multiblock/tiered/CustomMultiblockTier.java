package net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered;

import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.compat.kubejs.machine.WorkstationAdder;

import java.util.List;
import java.util.function.Consumer;

public record CustomMultiblockTier(
		String id,
		MachineRecipeType recipeType,
		ShapeTemplate shape,
		Consumer<WorkstationAdder> workstations,
		long maxBaseEu
)
{
	public String getTranslationKey()
	{
		return "custom_multiblock_tier.%s.%s".formatted(MITweaks.ID, id);
	}
	
	public Component getDisplayName()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	public List<ResourceLocation> getWorkstations()
	{
		var adder = new WorkstationAdder();
		workstations.accept(adder);
		return adder.get();
	}
}

package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;

public interface RecipeTypeHelper
{
	default MachineRecipeType getRecipeType(ResourceLocation id)
	{
		return MIMachineRecipeTypes.getRecipeTypes().stream()
				.filter((type) -> type.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Could not find recipe type with id " + id));
	}
}

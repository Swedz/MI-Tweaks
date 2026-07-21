package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;

public interface RecipeTypeHelper
{
	default MachineRecipeType getRecipeType(ResourceLocation id)
	{
		var recipeType = MIHookTracker.getRecipeType(id);
		if(recipeType == null)
		{
			throw new IllegalArgumentException("Could not find machine recipe type with id " + id);
		}
		return recipeType;
	}
}

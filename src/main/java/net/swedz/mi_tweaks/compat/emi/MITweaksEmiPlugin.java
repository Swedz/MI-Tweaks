package net.swedz.mi_tweaks.compat.emi;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.swedz.mi_tweaks.blueprintrequirement.CopyBlueprintRecipe;
import net.swedz.mi_tweaks.compat.emi.recipe.EmiCopyBlueprintRecipe;

@EmiEntrypoint
public final class MITweaksEmiPlugin implements EmiPlugin
{
	@Override
	public void register(EmiRegistry registry)
	{
		registry.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream().map(RecipeHolder::value).forEach((recipe) ->
		{
			if(recipe instanceof CopyBlueprintRecipe blueprint)
			{
				registry.addRecipe(new EmiCopyBlueprintRecipe(EmiPort.getId(recipe)));
			}
		});
	}
}

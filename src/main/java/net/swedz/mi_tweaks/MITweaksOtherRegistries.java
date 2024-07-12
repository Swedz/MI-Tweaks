package net.swedz.mi_tweaks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.mi_tweaks.blueprintrequirement.CopyBlueprintRecipe;

public final class MITweaksOtherRegistries
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MITweaks.ID);
	
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<CopyBlueprintRecipe>> COPY_BLUEPRINT_SERIALIZER = RECIPE_SERIALIZERS.register("copy_blueprint", () -> new SimpleCraftingRecipeSerializer<>(CopyBlueprintRecipe::new));
	
	public static void init(IEventBus bus)
	{
		RECIPE_SERIALIZERS.register(bus);
	}
}

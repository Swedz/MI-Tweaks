package net.swedz.mi_tweaks.compat.mi.custom;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksSortOrder;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.registry.SortOrder;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

@MIHookEntrypoint
public final class CustomMITweaksMIHookRegistry implements MIHookRegistry
{
	@Override
	public String modId()
	{
		return MITweaks.config().machineNamespace();
	}
	
	@Override
	public boolean shouldInitialize()
	{
		return !MITweaks.config().machineNamespace().equals(MITweaks.ID);
	}
	
	@Override
	public DeferredRegister.Blocks blockRegistry()
	{
		return MITweaksMIRegistries.BLOCKS;
	}
	
	@Override
	public DeferredRegister<BlockEntityType<?>> blockEntityRegistry()
	{
		return MITweaksMIRegistries.BLOCK_ENTITIES;
	}
	
	@Override
	public DeferredRegister.Items itemRegistry()
	{
		return MITweaksMIRegistries.ITEMS;
	}
	
	@Override
	public DeferredRegister<RecipeSerializer<?>> recipeSerializerRegistry()
	{
		return null;
	}
	
	@Override
	public DeferredRegister<RecipeType<?>> recipeTypeRegistry()
	{
		return null;
	}
	
	@Override
	public void onBlockRegister(BlockHolder blockHolder)
	{
		MITweaksMIRegistries.include(blockHolder);
	}
	
	@Override
	public void onBlockEntityRegister(BlockEntityType<?> blockEntityType)
	{
	}
	
	@Override
	public void onItemRegister(ItemHolder itemHolder)
	{
		MITweaksMIRegistries.include(itemHolder);
	}
	
	@Override
	public void onMachineRecipeTypeRegister(MachineRecipeType machineRecipeType)
	{
	}
	
	@Override
	public SortOrder sortOrderMachines()
	{
		return MITweaksSortOrder.MACHINES;
	}
}

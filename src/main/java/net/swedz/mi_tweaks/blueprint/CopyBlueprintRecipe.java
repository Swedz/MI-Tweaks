package net.swedz.mi_tweaks.blueprint;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.MITweaksOtherRegistries;

public final class CopyBlueprintRecipe extends CustomRecipe
{
	public CopyBlueprintRecipe(CraftingBookCategory category)
	{
		super(category);
	}
	
	@Override
	public boolean matches(CraftingInput craftingInput, Level level)
	{
		ItemStack blueprint = ItemStack.EMPTY;
		ItemStack paper = ItemStack.EMPTY;
		
		for(int i = 0; i < craftingInput.size(); i++)
		{
			ItemStack itemstack = craftingInput.getItem(i);
			if(!itemstack.isEmpty())
			{
				if(itemstack.is(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
				{
					if(!blueprint.isEmpty())
					{
						return false;
					}
					blueprint = itemstack;
				}
				else if(itemstack.is(Items.PAPER))
				{
					if(!paper.isEmpty())
					{
						return false;
					}
					paper = itemstack;
				}
			}
		}
		return !blueprint.isEmpty() && !paper.isEmpty();
	}
	
	@Override
	public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider registries)
	{
		ItemStack blueprint = ItemStack.EMPTY;
		
		for(int i = 0; i < craftingInput.size(); i++)
		{
			ItemStack itemstack = craftingInput.getItem(i);
			if(!itemstack.isEmpty() && itemstack.is(MITweaksItems.MACHINE_BLUEPRINT.asItem()))
			{
				blueprint = itemstack;
				break;
			}
		}
		
		if(blueprint.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		
		ItemStack result = blueprint.copy();
		result.setCount(1);
		return result;
	}
	
	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return pWidth > 1 || pHeight > 1;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput)
	{
		NonNullList<ItemStack> remaining = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
		
		for(int i = 0; i < remaining.size(); ++i)
		{
			ItemStack itemstack = craftingInput.getItem(i);
			if(itemstack.getItem() == MITweaksItems.MACHINE_BLUEPRINT.asItem())
			{
				remaining.set(i, itemstack.copy());
			}
			else
			{
				remaining.set(i, itemstack.getCraftingRemainingItem());
			}
		}
		
		return remaining;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MITweaksOtherRegistries.COPY_BLUEPRINT_SERIALIZER.get();
	}
}

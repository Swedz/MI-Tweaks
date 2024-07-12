package net.swedz.mi_tweaks.blueprintrequirement;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
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
	public boolean matches(CraftingContainer container, Level level)
	{
		ItemStack blueprint = ItemStack.EMPTY;
		ItemStack paper = ItemStack.EMPTY;
		
		for(int i = 0; i < container.getContainerSize(); i++)
		{
			ItemStack itemstack = container.getItem(i);
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
	public ItemStack assemble(CraftingContainer container, RegistryAccess pRegistryAccess)
	{
		ItemStack blueprint = ItemStack.EMPTY;
		
		for(int i = 0; i < container.getContainerSize(); i++)
		{
			ItemStack itemstack = container.getItem(i);
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
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container)
	{
		NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		
		for(int i = 0; i < remaining.size(); ++i)
		{
			ItemStack itemstack = container.getItem(i);
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

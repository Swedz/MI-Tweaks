package net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.ReiSlotLocking;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.swedz.tesseract.neoforge.compat.mi.helper.CommonGuiComponents;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.BasicMultiblockMachineBlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class TieredCraftingMultiblockBlockEntity extends BasicMultiblockMachineBlockEntity implements CrafterComponent.Behavior
{
	protected final CustomMultiblockTier[] tiers;
	
	protected final long maxRecipeEu;
	
	protected final CrafterComponent crafter;
	
	private OperatingState operatingState = OperatingState.NOT_MATCHED;
	
	public TieredCraftingMultiblockBlockEntity(BEP bep, ResourceLocation name,
											   CustomMultiblockTier[] tiers, long maxRecipeEu)
	{
		super(
				bep,
				new MachineGuiParameters.Builder(name, false).backgroundHeight(200).build(),
				Arrays.stream(tiers).map(CustomMultiblockTier::shape).toArray(ShapeTemplate[]::new)
		);
		
		this.tiers = tiers;
		
		this.maxRecipeEu = maxRecipeEu;
		
		this.crafter = new CrafterComponent(this, inventory, this);
		
		this.registerComponents(crafter);
		
		this.registerGuiComponent(new ReiSlotLocking(crafter::lockRecipe, () -> operatingState != OperatingState.NOT_MATCHED));
		
		List<Component> tierTranslations = Arrays.stream(tiers).map(CustomMultiblockTier::getDisplayName).toList();
		this.registerGuiComponent(CommonGuiComponents.rangedShapeSelection(this, activeShape, tierTranslations, true));
	}
	
	public CustomMultiblockTier getActiveTier()
	{
		return tiers[activeShape.getActiveShapeIndex()];
	}
	
	@Override
	public MachineRecipeType recipeType()
	{
		return this.getActiveTier().recipeType();
	}
	
	@Override
	public long getMaxRecipeEu()
	{
		return maxRecipeEu;
	}
	
	@Override
	public boolean banRecipe(MachineRecipe recipe)
	{
		return CrafterComponent.Behavior.super.banRecipe(recipe) ||
			   recipe.eu > this.getActiveTier().maxBaseEu();
	}
	
	@Override
	public ServerLevel getCrafterWorld()
	{
		return (ServerLevel) level;
	}
	
	@Override
	public UUID getOwnerUuid()
	{
		return placedBy.placerId;
	}
	
	@Override
	protected void onRematch(ShapeMatcher shapeMatcher)
	{
		super.onRematch(shapeMatcher);
		
		operatingState = OperatingState.NOT_MATCHED;
		if(shapeMatcher.isMatchSuccessful())
		{
			operatingState = OperatingState.TRYING_TO_RESUME;
		}
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(!level.isClientSide())
		{
			boolean newActive = false;
			
			if(operatingState == OperatingState.TRYING_TO_RESUME)
			{
				if(crafter.tryContinueRecipe())
				{
					operatingState = OperatingState.NORMAL_OPERATION;
				}
			}
			
			if(operatingState == OperatingState.NORMAL_OPERATION)
			{
				if(crafter.tickRecipe())
				{
					newActive = true;
				}
			}
			else
			{
				crafter.decreaseEfficiencyTicks();
			}
			
			isActive.updateActive(newActive, this);
		}
	}
	
	private enum OperatingState
	{
		/**
		 * Shape is not matched, don't do anything.
		 */
		NOT_MATCHED,
		/**
		 * Trying to resume a recipe but the output might not fit anymore.
		 * We wait until the output fits again before resuming normal operation.
		 */
		TRYING_TO_RESUME,
		/**
		 * Normal operation.
		 */
		NORMAL_OPERATION
	}
}

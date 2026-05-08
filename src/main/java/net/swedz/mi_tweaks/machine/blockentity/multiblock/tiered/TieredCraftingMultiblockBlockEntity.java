package net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.ReiSlotLocking;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformers;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.MultipliedCrafterComponent;
import net.swedz.tesseract.neoforge.compat.mi.helper.CommonGuiComponents;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.BasicMultiblockMachineBlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class TieredCraftingMultiblockBlockEntity extends BasicMultiblockMachineBlockEntity implements ModularCrafterAccessBehavior
{
	protected final CustomMultiblockTier[] tiers;
	
	protected final MultipliedCrafterComponent crafter;
	
	private OperatingState operatingState = OperatingState.NOT_MATCHED;
	
	public TieredCraftingMultiblockBlockEntity(BEP bep, ResourceLocation name,
											   CustomMultiblockTier[] tiers)
	{
		super(
				bep,
				new MachineGuiParameters.Builder(name, false).backgroundHeight(200).build(),
				Arrays.stream(tiers).map(CustomMultiblockTier::shape).toArray(ShapeTemplate[]::new)
		);
		
		this.tiers = tiers;
		
		this.crafter = new MultipliedCrafterComponent(
				this,
				inventory,
				this,
				() -> this.getActiveTier().recipeType(),
				() -> this.getActiveTier().multiplier(),
				() -> EuCostTransformers.percentage(() -> this.getActiveTier().euCostMultiplier())
		);
		
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
	public long getBaseMaxRecipeEu()
	{
		return this.getActiveTier().maxBaseEu();
	}
	
	@Override
	public boolean isRecipeBanned(long recipeEu)
	{
		return recipeEu > this.getActiveTier().maxBaseEu();
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

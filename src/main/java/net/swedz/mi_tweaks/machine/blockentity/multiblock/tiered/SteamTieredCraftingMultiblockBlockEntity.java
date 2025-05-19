package net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.guicomponents.CraftingMultiblockGui;
import aztech.modern_industrialization.machines.helper.SteamHelper;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.util.Simulation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class SteamTieredCraftingMultiblockBlockEntity extends TieredCraftingMultiblockBlockEntity
{
	private final OverclockComponent overclock;
	
	private boolean steelTier;
	
	public SteamTieredCraftingMultiblockBlockEntity(BEP bep, ResourceLocation name,
													CustomMultiblockTier[] tiers, long maxRecipeEu,
													List<OverclockComponent.Catalyst> overclockCatalysts)
	{
		super(bep, name, tiers, maxRecipeEu);
		
		overclock = new OverclockComponent(overclockCatalysts);
		
		this.registerComponents(overclock);
		
		this.registerGuiComponent(new CraftingMultiblockGui.Server(() -> shapeValid.shapeValid, crafter::getProgress, crafter, overclock::getTicks));
	}
	
	@Override
	protected void onRematch(ShapeMatcher shapeMatcher)
	{
		super.onRematch(shapeMatcher);
		
		if(shapeMatcher.isMatchSuccessful())
		{
			steelTier = false;
			for(var hatch : shapeMatcher.getMatchedHatches())
			{
				if(hatch.upgradesToSteel())
				{
					steelTier = true;
					break;
				}
			}
		}
	}
	
	@Override
	public long getBaseRecipeEu()
	{
		return steelTier ? 4 : 2;
	}
	
	@Override
	public long consumeEu(long max, Simulation simulation)
	{
		return SteamHelper.consumeSteamEu(inventory.getFluidInputs(), max, simulation);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		overclock.tick(this);
	}
	
	@Override
	public List<Component> getTooltips()
	{
		return overclock.getTooltips();
	}
	
	@Override
	protected ItemInteractionResult useItemOn(Player player, InteractionHand hand, Direction face)
	{
		var result = super.useItemOn(player, hand, face);
		if(!result.consumesAction())
		{
			result = overclock.onUse(this, player, hand);
		}
		return result;
	}
}

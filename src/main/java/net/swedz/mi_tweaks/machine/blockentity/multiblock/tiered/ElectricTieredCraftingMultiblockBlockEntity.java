package net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered;

import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyListComponentHolder;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.EnergyComponent;
import aztech.modern_industrialization.machines.components.LubricantHelper;
import aztech.modern_industrialization.machines.components.OverdriveComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import aztech.modern_industrialization.machines.guicomponents.CraftingMultiblockGui;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.util.Simulation;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class ElectricTieredCraftingMultiblockBlockEntity extends TieredCraftingMultiblockBlockEntity implements EnergyListComponentHolder
{
	private final RedstoneControlComponent redstoneControl;
	private final UpgradeComponent         upgrades;
	private final OverdriveComponent       overdrive;
	
	private final List<EnergyComponent> energyInputs = Lists.newArrayList();
	
	public ElectricTieredCraftingMultiblockBlockEntity(BEP bep, ResourceLocation name,
													   CustomMultiblockTier[] tiers, long maxRecipeEu)
	{
		super(bep, name, tiers, maxRecipeEu);
		
		redstoneControl = new RedstoneControlComponent();
		upgrades = new UpgradeComponent();
		overdrive = new OverdriveComponent();
		
		this.registerComponents(redstoneControl, upgrades, overdrive);
		this.registerGuiComponent(new SlotPanel.Server(this)
				.withRedstoneControl(redstoneControl)
				.withUpgrades(upgrades)
				.withOverdrive(overdrive));
		
		registerGuiComponent(new CraftingMultiblockGui.Server(() -> shapeValid.shapeValid, crafter::getProgress, crafter, () -> 0));
	}
	
	@Override
	protected void onRematch(ShapeMatcher shapeMatcher)
	{
		super.onRematch(shapeMatcher);
		
		if(shapeMatcher.isMatchSuccessful())
		{
			energyInputs.clear();
			for(var hatch : shapeMatcher.getMatchedHatches())
			{
				hatch.appendEnergyInputs(energyInputs);
			}
		}
	}
	
	@Override
	public long consumeEu(long max, Simulation simulation)
	{
		long total = 0;
		for(var energyComponent : energyInputs)
		{
			total += energyComponent.consumeEu(max - total, simulation);
		}
		return total;
	}
	
	@Override
	public long getBaseRecipeEu()
	{
		return MachineTier.MULTIBLOCK.getBaseEu();
	}
	
	@Override
	public List<? extends EnergyAccess> getEnergyComponents()
	{
		return energyInputs;
	}
	
	@Override
	public long getMaxRecipeEu()
	{
		return super.getMaxRecipeEu() + upgrades.getAddMaxEUPerTick();
	}
	
	@Override
	public boolean isEnabled()
	{
		return redstoneControl.doAllowNormalOperation(this);
	}
	
	@Override
	public boolean isOverdriving()
	{
		return overdrive.shouldOverdrive();
	}
	
	@Override
	protected ItemInteractionResult useItemOn(Player player, InteractionHand hand, Direction face)
	{
		var result = super.useItemOn(player, hand, face);
		if(!result.consumesAction())
		{
			result = LubricantHelper.onUse(crafter, player, hand);
		}
		if(!result.consumesAction())
		{
			result = components.mapOrDefault(UpgradeComponent.class, (c) -> c.onUse(this, player, hand), result);
		}
		if(!result.consumesAction())
		{
			result = redstoneControl.onUse(this, player, hand);
		}
		if(!result.consumesAction())
		{
			result = components.mapOrDefault(OverdriveComponent.class, (c) -> c.onUse(this, player, hand), result);
		}
		return result;
	}
}

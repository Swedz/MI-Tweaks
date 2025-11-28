package net.swedz.mi_tweaks.machine.blockentity;

import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyComponentHolder;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CasingComponent;
import aztech.modern_industrialization.machines.components.EnergyComponent;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.models.MachineModelClientData;
import aztech.modern_industrialization.util.Simulation;
import aztech.modern_industrialization.util.Tickable;
import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.compat.mi.helper.transfer.LongEnergyTransferCache;

public final class FluxTransformerBlockEntity extends MachineBlockEntity implements Tickable, EnergyComponentHolder
{
	private final RedstoneControlComponent redstoneControl;
	private final CasingComponent          casing;
	
	private final EnergyComponent    energy;
	private final MIEnergyStorage    insertable;
	private final ILongEnergyStorage extractable;
	
	private final LongEnergyTransferCache transferEnergy;
	
	public FluxTransformerBlockEntity(BEP bep)
	{
		super(
				bep,
				new MachineGuiParameters.Builder(MITweaks.id("flux_transformer"), false).build(),
				new OrientationComponent.Params(true, false, false)
		);
		
		redstoneControl = new RedstoneControlComponent();
		casing = new CasingComponent();
		
		energy = new EnergyComponent(this, () -> MITweaks.config().fluxTransformer().capacity());
		insertable = energy.buildInsertable(casing::canInsertEu);
		extractable = new ILongEnergyStorage()
		{
			@Override
			public boolean canExtract()
			{
				return redstoneControl.doAllowNormalOperation(FluxTransformerBlockEntity.this);
			}
			
			@Override
			public boolean canReceive()
			{
				return false;
			}
			
			@Override
			public long receive(long receive, boolean simulate)
			{
				return 0;
			}
			
			@Override
			public long extract(long extractFE, boolean simulate)
			{
				extractFE = Math.min(extractFE, MITweaks.config().fluxTransformer().maxExtract());
				long extractEU = (long) (extractFE / MITweaks.config().fluxTransformer().conversionRate());
				long extractedEU = energy.consumeEu(extractEU, simulate ? Simulation.SIMULATE : Simulation.ACT);
				return (long) (extractedEU * MITweaks.config().fluxTransformer().conversionRate());
			}
			
			@Override
			public long getAmount()
			{
				return (long) (energy.getEu() * MITweaks.config().fluxTransformer().conversionRate());
			}
			
			@Override
			public long getCapacity()
			{
				return (long) (energy.getCapacity() * MITweaks.config().fluxTransformer().conversionRate());
			}
		};
		
		transferEnergy = new LongEnergyTransferCache(() -> extractable);
		
		this.registerComponents(redstoneControl, casing, energy);
		
		var energyBarParams = new EnergyBar.Params(76, 39);
		this.registerGuiComponent(new EnergyBar(energyBarParams, energy::getEu, energy::getCapacity));
		
		this.registerGuiComponent(new SlotPanel(this)
				.withRedstoneControl(redstoneControl)
				.withCasing(casing));
	}
	
	@Override
	public EnergyAccess getEnergyComponent()
	{
		return energy;
	}
	
	@Override
	public MIInventory getInventory()
	{
		return MIInventory.EMPTY;
	}
	
	@Override
	public MachineModelClientData getMachineModelData()
	{
		MachineModelClientData data = new MachineModelClientData(casing.getCasing());
		orientation.writeModelData(data);
		return data;
	}
	
	@Override
	public void tick()
	{
		if(level.isClientSide())
		{
			return;
		}
		
		if(redstoneControl.doAllowNormalOperation(this))
		{
			if(transferEnergy.autoExtract(level, worldPosition, orientation.outputDirection, MITweaks.config().fluxTransformer().maxExtract()))
			{
				this.setChanged();
			}
		}
	}
	
	@Override
	protected ItemInteractionResult useItemOn(Player player, InteractionHand hand, Direction face)
	{
		ItemInteractionResult result = super.useItemOn(player, hand, face);
		if(!result.consumesAction())
		{
			result = redstoneControl.onUse(this, player, hand);
		}
		if(!result.consumesAction())
		{
			result = casing.onUse(this, player, hand);
		}
		return result;
	}
	
	public static void registerEnergyApi(BlockEntityType<?> bet)
	{
		CapabilitiesListeners.register(MITweaks.ID, (event) ->
		{
			event.registerBlockEntity(EnergyApi.SIDED, bet, (be, direction) ->
			{
				FluxTransformerBlockEntity machine = (FluxTransformerBlockEntity) be;
				return machine.orientation.outputDirection == direction ? null : machine.insertable;
			});
			
			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, bet, (be, direction) ->
			{
				FluxTransformerBlockEntity machine = (FluxTransformerBlockEntity) be;
				return machine.orientation.outputDirection == direction ? machine.extractable : null;
			});
		});
	}
}

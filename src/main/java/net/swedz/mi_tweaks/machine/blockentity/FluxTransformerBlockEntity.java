package net.swedz.mi_tweaks.machine.blockentity;

import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyComponentHolder;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.EnergyComponent;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.models.MachineModelClientData;
import aztech.modern_industrialization.util.Simulation;
import aztech.modern_industrialization.util.Tickable;
import dev.technici4n.grandpower.api.EnergyStorageUtil;
import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;

public final class FluxTransformerBlockEntity extends MachineBlockEntity implements Tickable, EnergyComponentHolder
{
	private final RedstoneControlComponent redstoneControl;
	
	private final EnergyComponent    energy;
	private final MIEnergyStorage    insertable;
	private final ILongEnergyStorage extractable;
	
	public FluxTransformerBlockEntity(BEP bep)
	{
		super(
				bep,
				new MachineGuiParameters.Builder(MITweaks.id("flux_transformer"), false).build(),
				new OrientationComponent.Params(true, false, false)
		);
		
		redstoneControl = new RedstoneControlComponent();
		
		energy = new EnergyComponent(this, () -> MITweaksConfig.fluxTransformerCapacity);
		insertable = energy.buildInsertable((tier) -> tier == MITweaksConfig.fluxTransformerCableTier);
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
				extractFE = Math.min(extractFE, MITweaksConfig.fluxTransformerMaxExtract);
				long extractEU = (long) (extractFE / MITweaksConfig.fluxTransformerConversionRate);
				long extractedEU = energy.consumeEu(extractEU, simulate ? Simulation.SIMULATE : Simulation.ACT);
				return (long) (extractedEU * MITweaksConfig.fluxTransformerConversionRate);
			}
			
			@Override
			public long getAmount()
			{
				return (long) (energy.getEu() * MITweaksConfig.fluxTransformerConversionRate);
			}
			
			@Override
			public long getCapacity()
			{
				return (long) (energy.getCapacity() * MITweaksConfig.fluxTransformerConversionRate);
			}
		};
		
		this.registerComponents(redstoneControl, energy);
		
		EnergyBar.Parameters energyBarParams = new EnergyBar.Parameters(76, 39);
		this.registerGuiComponent(new EnergyBar.Server(energyBarParams, energy::getEu, energy::getCapacity));
		
		this.registerGuiComponent(new SlotPanel.Server(this)
				.withRedstoneControl(redstoneControl));
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
	protected MachineModelClientData getMachineModelData()
	{
		MachineModelClientData data = new MachineModelClientData();
		orientation.writeModelData(data);
		return data;
	}
	
	private void autoOutputEnergy()
	{
		if(level.isClientSide())
		{
			throw new IllegalStateException("Cannot call autoOutputEnergy() on the client");
		}
		
		IEnergyStorage target = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition.relative(orientation.outputDirection), orientation.outputDirection.getOpposite());
		if(target != null && target.canReceive())
		{
			if(EnergyStorageUtil.move(extractable, ILongEnergyStorage.of(target), MITweaksConfig.fluxTransformerMaxExtract) > 0)
			{
				this.setChanged();
			}
		}
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
			this.autoOutputEnergy();
		}
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

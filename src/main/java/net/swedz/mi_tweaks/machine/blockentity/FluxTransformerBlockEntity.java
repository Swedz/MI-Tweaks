package net.swedz.mi_tweaks.machine.blockentity;

import aztech.modern_industrialization.api.energy.CableTier;
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
import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;

public final class FluxTransformerBlockEntity extends MachineBlockEntity implements EnergyComponentHolder
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
		
		// TODO configurable energy storage
		energy = new EnergyComponent(this, () -> 200 * CableTier.LV.getEu());
		insertable = energy.buildInsertable((tier) -> true);
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
			public long receive(long maxReceive, boolean simulate)
			{
				return 0;
			}
			
			@Override
			public long extract(long maxExtract, boolean simulate)
			{
				// TODO configurable conversion rate
				return energy.consumeEu(maxExtract / 2, simulate ? Simulation.SIMULATE : Simulation.ACT) * 2;
			}
			
			@Override
			public long getAmount()
			{
				return energy.getEu() * 4;
			}
			
			@Override
			public long getCapacity()
			{
				return energy.getCapacity() * 4;
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

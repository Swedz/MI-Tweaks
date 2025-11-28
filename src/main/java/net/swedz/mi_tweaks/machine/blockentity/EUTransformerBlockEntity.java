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
import net.swedz.tesseract.neoforge.compat.mi.helper.transfer.MIEnergyTransferCache;

public final class EUTransformerBlockEntity extends MachineBlockEntity implements Tickable, EnergyComponentHolder
{
	private final RedstoneControlComponent redstoneControl;
	private final CasingComponent          casing;
	
	private final EnergyComponent    energy;
	private final ILongEnergyStorage insertable;
	private final MIEnergyStorage    extractable;
	
	private final MIEnergyTransferCache transferEnergy;
	
	public EUTransformerBlockEntity(BEP bep)
	{
		super(
				bep,
				new MachineGuiParameters.Builder(MITweaks.id("eu_transformer"), false).build(),
				new OrientationComponent.Params(true, false, false)
		);
		
		redstoneControl = new RedstoneControlComponent();
		casing = new CasingComponent();
		
		energy = new EnergyComponent(this, () -> MITweaks.config().euTransformer().capacity());
		insertable = new ILongEnergyStorage()
		{
			@Override
			public boolean canExtract()
			{
				return false;
			}
			
			@Override
			public boolean canReceive()
			{
				return true;
			}
			
			@Override
			public long receive(long receiveFE, boolean simulate)
			{
				receiveFE = Math.min(receiveFE, MITweaks.config().euTransformer().maxInsert());
				long receiveEU = (long) (receiveFE * MITweaks.config().euTransformer().conversionRate());
				return energy.insertEu(receiveEU, simulate ? Simulation.SIMULATE : Simulation.ACT);
			}
			
			@Override
			public long extract(long extractEU, boolean simulate)
			{
				return 0;
			}
			
			@Override
			public long getAmount()
			{
				return energy.getEu();
			}
			
			@Override
			public long getCapacity()
			{
				return energy.getCapacity();
			}
		};
		extractable = energy.buildExtractable(casing::canInsertEu);
		
		transferEnergy = new MIEnergyTransferCache(() -> extractable);
		
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
			if(transferEnergy.autoExtract(level, worldPosition, orientation.outputDirection, casing.getCableTier()))
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
				EUTransformerBlockEntity machine = (EUTransformerBlockEntity) be;
				return machine.orientation.outputDirection == direction ? machine.extractable : null;
			});
			
			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, bet, (be, direction) ->
			{
				EUTransformerBlockEntity machine = (EUTransformerBlockEntity) be;
				return machine.orientation.outputDirection == direction ? null : machine.insertable;
			});
		});
	}
}

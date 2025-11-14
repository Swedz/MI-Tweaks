package net.swedz.mi_tweaks.mixin;

import aztech.modern_industrialization.machines.blockentities.multiblocks.SteamBoilerMultiblockBlockEntity;
import aztech.modern_industrialization.machines.components.IsActiveComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import net.swedz.mi_tweaks.MITweaks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = SteamBoilerMultiblockBlockEntity.class,
		remap = false
)
public class RedstoneLocksTemperatureBoilerMultiblockMixin
{
	@Shadow
	@Final
	private RedstoneControlComponent redstoneControl;
	
	@Shadow
	@Final
	private IsActiveComponent isActiveComponent;
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/RedstoneControlComponent;doAllowNormalOperation(Laztech/modern_industrialization/machines/MachineBlockEntity;)Z"
			),
			cancellable = true
	)
	private void tick(CallbackInfo callback)
	{
		var machine = (SteamBoilerMultiblockBlockEntity) (Object) this;
		if(MITweaks.config().tweaks().lockBoilerTemperatureWithRedstone() &&
		   !redstoneControl.doAllowNormalOperation(machine))
		{
			isActiveComponent.updateActive(false, machine);
			machine.setChanged();
			callback.cancel();
		}
	}
}

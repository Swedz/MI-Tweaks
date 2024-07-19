package net.swedz.mi_tweaks.mixin.constantefficiency;

import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import net.swedz.mi_tweaks.compat.mi.MITweaksMIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.MachineMIHookContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = SlotPanel.Server.class,
		remap = false
)
public class ConstantEfficiencyPreventUpgradeSlotMixin
{
	@Shadow
	@Final
	private MachineBlockEntity machine;
	
	@Inject(
			method = "withUpgrades",
			at = @At("HEAD"),
			cancellable = true
	)
	private void withUpgrades(UpgradeComponent upgradeComponent, CallbackInfoReturnable<SlotPanel.Server> callback)
	{
		if(MITweaksMIHookEfficiency.HACK.preventsUpgrades(new MachineMIHookContext(machine)))
		{
			callback.setReturnValue((SlotPanel.Server) (Object) this);
		}
	}
}

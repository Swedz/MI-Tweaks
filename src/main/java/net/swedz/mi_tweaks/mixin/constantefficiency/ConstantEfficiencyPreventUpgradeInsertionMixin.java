package net.swedz.mi_tweaks.mixin.constantefficiency;

import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.swedz.mi_tweaks.MITweaksConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
		value = UpgradeComponent.class,
		remap = false
)
public class ConstantEfficiencyPreventUpgradeInsertionMixin
{
	@Inject(
			method = "onUse",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onUse(MachineBlockEntity be, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callback)
	{
		if(MITweaksConfig.efficiencyHackMode.preventsUpgrades())
		{
			callback.setReturnValue(InteractionResult.PASS);
		}
	}
}

package net.swedz.mi_tweaks.mixin.constantefficiency;

import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.AbstractModularCrafterComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(
		value = AbstractModularCrafterComponent.class,
		remap = false
)
public abstract class ConstantEfficiencyModularCrafterComponentMixin
{
	@Shadow
	protected int efficiencyTicks;
	
	@Shadow
	protected int maxEfficiencyTicks;
	
	@Shadow
	protected long recipeMaxEu;
	
	@Shadow
	public abstract boolean hasActiveRecipe();
	
	@Inject(
			method = "decreaseEfficiencyTicks",
			at = @At("HEAD"),
			cancellable = true
	)
	private void decreaseEfficiencyTicks(CallbackInfo callback)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			callback.cancel();
		}
	}
	
	@Inject(
			method = "increaseEfficiencyTicks",
			at = @At("HEAD"),
			cancellable = true
	)
	private void increaseEfficiencyTicks(int increment, CallbackInfo callback)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			callback.cancel();
		}
	}
	
	@Inject(
			method = "tickRecipe",
			at = @At("HEAD")
	)
	private void tickRecipe(CallbackInfoReturnable<Boolean> callback)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			efficiencyTicks = this.hasActiveRecipe() ? maxEfficiencyTicks : 0;
		}
	}
	
	@Inject(
			method = "tickRecipe",
			at = @At(
					value = "INVOKE",
					target = "Lnet/swedz/tesseract/neoforge/compat/mi/component/craft/AbstractModularCrafterComponent;clearActiveRecipeIfPossible()V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void tickRecipeReset(CallbackInfoReturnable<Boolean> cir, boolean isActive, boolean isEnabled, boolean recipeStarted, long eu, boolean finishedRecipe)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency() && eu < recipeMaxEu)
		{
			efficiencyTicks = 0;
		}
	}
	
	@Inject(
			method = "readNbt",
			at = @At("RETURN")
	)
	private void readNbt(net.minecraft.nbt.CompoundTag tag, boolean isUpgradingMachine, CallbackInfo callback)
	{
		if(MITweaksConfig.machineEfficiencyHack.forceMaxEfficiency())
		{
			efficiencyTicks = this.hasActiveRecipe() ? maxEfficiencyTicks : 0;
		}
	}
}

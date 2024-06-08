package net.swedz.mi_tweaks.mixin.machinecabletier;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.components.CasingComponent;
import net.swedz.mi_tweaks.api.CableTierHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CasingComponent.class)
public class CasingComponentCableTierGetterMixin implements CableTierHolder
{
	@Shadow
	private CableTier currentTier;
	
	@Unique
	@Override
	public CableTier getCableTier()
	{
		return currentTier;
	}
}

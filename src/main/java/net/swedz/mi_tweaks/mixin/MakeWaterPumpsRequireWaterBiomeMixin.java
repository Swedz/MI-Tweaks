package net.swedz.mi_tweaks.mixin;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.AbstractWaterPumpBlockEntity;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.swedz.mi_tweaks.MITweaksConfig;
import net.swedz.mi_tweaks.guicomponent.waterpumpenvironment.WaterPumpEnvironmentGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = AbstractWaterPumpBlockEntity.class,
		remap = false
)
public abstract class MakeWaterPumpsRequireWaterBiomeMixin extends MachineBlockEntity
{
	public MakeWaterPumpsRequireWaterBiomeMixin(BEP bep, MachineGuiParameters guiParams, OrientationComponent.Params orientationParams)
	{
		super(bep, guiParams, orientationParams);
	}
	
	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void init(BEP bep, String blockName, CallbackInfo callback)
	{
		if(MITweaksConfig.requireWaterBiomeForPump)
		{
			this.registerGuiComponent(new WaterPumpEnvironmentGui.Server(
					new WaterPumpEnvironmentGui.Parameters(57, 29),
					() -> this.isWaterBiome(level.getBiome(worldPosition))
			));
		}
	}
	
	@Unique
	private boolean isWaterBiome(Holder<Biome> biome)
	{
		return biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_RIVER);
	}
	
	@Invoker("consumeEu")
	protected abstract long invokeConsumeEu(long amount);
	
	@Redirect(
			method = "tick",
			at = @At(value = "INVOKE", target = "Laztech/modern_industrialization/machines/blockentities/AbstractWaterPumpBlockEntity;consumeEu(J)J")
	)
	private long redirectConsumeEu(AbstractWaterPumpBlockEntity instance, long max)
	{
		return !MITweaksConfig.requireWaterBiomeForPump || this.isWaterBiome(level.getBiome(worldPosition)) ? this.invokeConsumeEu(max) : 0;
	}
}

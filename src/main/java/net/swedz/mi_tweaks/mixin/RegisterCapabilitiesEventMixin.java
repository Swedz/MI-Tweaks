package net.swedz.mi_tweaks.mixin;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.swedz.mi_tweaks.feature.ExternalBlockCableTiers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegisterCapabilitiesEvent.class)
public abstract class RegisterCapabilitiesEventMixin {
	@Unique
	private static MIEnergyStorage miTweaks$wrapEnergyStorage(IEnergyStorage wrapped, CableTier tier) {
		if (wrapped == null) {
			return null;
		}

		return new MIEnergyStorage() {
			@Override
			public boolean canConnect(CableTier cableTier) {
				return cableTier.equals(tier);
			}

			@Override
			public long receive(long maxReceive, boolean simulate) {
				return wrapped.receiveEnergy((int) maxReceive, simulate);
			}

			@Override
			public long extract(long maxExtract, boolean simulate) {
				return wrapped.extractEnergy((int) maxExtract, simulate);
			}

			@Override
			public long getAmount() {
				return wrapped.getEnergyStored();
			}

			@Override
			public long getCapacity() {
				return wrapped.getMaxEnergyStored();
			}

			@Override
			public boolean canExtract() {
				return wrapped.canExtract();
			}

			@Override
			public boolean canReceive() {
				return wrapped.canReceive();
			}
		};
	}

	@Shadow
	public abstract <T, C> void registerBlock(BlockCapability<T, C> capability, IBlockCapabilityProvider<T, C> provider, Block... blocks);

	@Shadow
	public abstract <T, C, BE extends BlockEntity> void registerBlockEntity(BlockCapability<T, C> capability, BlockEntityType<BE> blockEntityType, ICapabilityProvider<? super BE, C, T> provider);

	@Inject(method = "registerBlock", at = @At(value = "HEAD"))
	private void onRegisterBlock(
			BlockCapability<?, ?> capability,
			IBlockCapabilityProvider<IEnergyStorage, @Nullable Direction> provider,
			Block[] blocks,
			CallbackInfo ci
	) {
		if (!ExternalBlockCableTiers.utilized || capability != Capabilities.EnergyStorage.BLOCK) {
			return;
		}

		for (Block block : blocks) {
			CableTier customTier = ExternalBlockCableTiers.BLOCKS.get(block);
			if (customTier == null) {
				continue;
			}

			IBlockCapabilityProvider<MIEnergyStorage, @Nullable Direction> adaptedProvider = (level, pos, state, be, side) -> {
				IEnergyStorage storage = provider.getCapability(level, pos, state, be, side);
				return miTweaks$wrapEnergyStorage(storage, customTier);
			};

			registerBlock(EnergyApi.SIDED, adaptedProvider, block);
		}
	}

	@Inject(method = "registerBlockEntity", at = @At(value = "HEAD"))
	private <T extends BlockEntity> void onRegisterBlockEntity(
			BlockCapability<?, ?> capability,
			BlockEntityType<T> blockEntityType,
			ICapabilityProvider<? super T, @Nullable Direction, IEnergyStorage> provider,
			CallbackInfo ci
	) {
		if (!ExternalBlockCableTiers.utilized || capability != Capabilities.EnergyStorage.BLOCK) {
			return;
		}

		CableTier customTier = ExternalBlockCableTiers.BLOCK_ENTITIES.get(blockEntityType);
		if (customTier == null) {
			return;
		}

		ICapabilityProvider<? super T, @Nullable Direction, MIEnergyStorage> adaptedProvider = (be, facing) -> {
			IEnergyStorage storage = provider.getCapability(be, facing);
			return miTweaks$wrapEnergyStorage(storage, customTier);
		};

		registerBlockEntity(EnergyApi.SIDED, blockEntityType, adaptedProvider);
	}
}
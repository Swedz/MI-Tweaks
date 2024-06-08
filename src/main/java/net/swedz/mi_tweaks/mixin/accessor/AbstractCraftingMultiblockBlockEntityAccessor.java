package net.swedz.mi_tweaks.mixin.accessor;

import aztech.modern_industrialization.machines.blockentities.multiblocks.AbstractCraftingMultiblockBlockEntity;
import aztech.modern_industrialization.machines.components.ActiveShapeComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = AbstractCraftingMultiblockBlockEntity.class,
		remap = false
)
public interface AbstractCraftingMultiblockBlockEntityAccessor
{
	@Accessor
	ActiveShapeComponent getActiveShape();
}

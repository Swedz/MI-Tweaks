package net.swedz.mi_tweaks.mixin.client.accessor;

import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = GuiComponentClient.class,
		remap = false
)
public interface GuiComponentClientAccessor
{
	@Accessor
	Object getData();
}

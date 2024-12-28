package net.swedz.mi_tweaks.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.swedz.mi_tweaks.datamap.WaterExplosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class WaterExplosiveMixin
{
	@Inject(
			method = "tick",
			at = @At("HEAD"),
			cancellable = true
	)
	private void tick(CallbackInfo callback)
	{
		ItemEntity entity = (ItemEntity) (Object) this;
		if(entity.isInWater())
		{
			Level level = entity.level();
			if(level != null)
			{
				var stack = entity.getItem();
				var item = stack.getItem();
				var explosive = WaterExplosive.getFor(item);
				if(explosive != null)
				{
					level.explode(null, entity.getX(), entity.getY(), entity.getZ(), explosive.strength(), explosive.fire(), Level.ExplosionInteraction.TNT);
					entity.discard();
					callback.cancel();
				}
			}
		}
	}
}

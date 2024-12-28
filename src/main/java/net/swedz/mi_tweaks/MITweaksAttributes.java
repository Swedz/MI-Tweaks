package net.swedz.mi_tweaks;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.mi_tweaks.attribute.HeatProtectionAttribute;

public final class MITweaksAttributes
{
	private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MITweaks.ID);
	
	public static final Holder<Attribute> HEAT_PROTECTION = ATTRIBUTES.register(
			"heat_protection",
			HeatProtectionAttribute::new
	);
	
	public static void init(IEventBus bus)
	{
		ATTRIBUTES.register(bus);
		bus.addListener(EntityAttributeModificationEvent.class, MITweaksAttributes::registerForEntities);
	}
	
	private static void registerForEntities(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, HEAT_PROTECTION);
	}
}

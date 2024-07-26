package net.swedz.mi_tweaks;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.swedz.mi_tweaks.datagen.client.LanguageDatagenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MITweaks.ID)
public final class MITweaks
{
	public static final String ID   = "mi_tweaks";
	public static final String NAME = "MI Tweaks";
	
	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public MITweaks(IEventBus bus, ModContainer container)
	{
		container.registerConfig(ModConfig.Type.STARTUP, MITweaksConfig.SPEC);
		MITweaksConfig.loadConfig();
		bus.addListener(FMLCommonSetupEvent.class, (event) -> MITweaksConfig.loadConfig());
		
		MITweaksDataComponents.init(bus);
		MITweaksItems.init(bus);
		MITweaksOtherRegistries.init(bus);
		
		bus.addListener(RegisterPayloadHandlersEvent.class, MITweaksPackets::init);
		
		bus.addListener(GatherDataEvent.class, (event) ->
				event.getGenerator().addProvider(event.includeClient(), new LanguageDatagenProvider(event)));
	}
}

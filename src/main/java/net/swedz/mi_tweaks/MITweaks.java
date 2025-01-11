package net.swedz.mi_tweaks;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.swedz.mi_tweaks.network.MITweaksPackets;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.config.ConfigManager;
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
		setupConfig(bus, container);
		
		MITweaksAttributes.init(bus);
		MITweaksComponents.init(bus);
		MITweaksItems.init(bus);
		MITweaksBlocks.init(bus);
		MITweaksOtherRegistries.init(bus);
		
		bus.addListener(RegisterCapabilitiesEvent.class, (event) -> CapabilitiesListeners.triggerAll(ID, event));
		bus.addListener(RegisterPayloadHandlersEvent.class, MITweaksPackets::init);
		
		bus.addListener(RegisterDataMapTypesEvent.class, MITweaksDataMaps::init);
	}
	
	private static MITweaksConfig CONFIG;
	
	public static MITweaksConfig config()
	{
		Assert.notNull(CONFIG, "Config not yet loaded");
		return CONFIG;
	}
	
	private static void setupConfig(IEventBus bus, ModContainer container)
	{
		var manager = new ConfigManager()
				.includeDefaultValueComments();
		manager.codecs()
				.register(MITweaksConfig.MachineList.class, MITweaksConfig.MachineList.CODEC);
		CONFIG = manager
				.build(MITweaksConfig.class)
				.register(container, ModConfig.Type.STARTUP)
				.load()
				.listenToLoad(bus)
				.config();
	}
}

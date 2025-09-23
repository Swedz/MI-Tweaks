package net.swedz.mi_tweaks;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.swedz.mi_tweaks.compat.mi.custom.MITweaksMIRegistries;
import net.swedz.mi_tweaks.network.MITweaksPackets;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.compat.mi.TesseractMI;
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
		
		TesseractMI.init(ID);
		if(!config().machineNamespace().equals(MITweaks.ID))
		{
			TesseractMI.init(config().machineNamespace());
		}
		MITweaksAttributes.init(bus);
		MITweaksComponents.init(bus);
		MITweaksItems.init(bus);
		MITweaksBlocks.init(bus);
		MITweaksMIRegistries.init(bus);
		MITweaksOtherRegistries.init(bus);
		
		bus.addListener(RegisterCapabilitiesEvent.class, (event) -> CapabilitiesListeners.triggerAll(ID, event));
		bus.addListener(RegisterPayloadHandlersEvent.class, MITweaksPackets::init);
		
		bus.addListener(RegisterDataMapTypesEvent.class, MITweaksDataMaps::init);
	}
	
	private static MITweaksConfig CONFIG;
	
	public static MITweaksConfig config()
	{
		if(CONFIG == null)
		{
			var container = ModList.get().getModContainerById(MITweaks.ID).orElseThrow();
			setupConfig(container.getEventBus(), container);
		}
		return CONFIG;
	}
	
	private static void setupConfig(IEventBus bus, ModContainer container)
	{
		if(CONFIG != null)
		{
			return;
		}
		var manager = new ConfigManager()
				.includeDefaultValueComments();
		manager.codecs()
				.register(MITweaksConfig.Efficiency.CableTierMaxOverclockOverrides.class, MITweaksConfig.Efficiency.CableTierMaxOverclockOverrides.CODEC)
				.register(MITweaksConfig.MachineList.class, MITweaksConfig.MachineList.CODEC);
		CONFIG = manager
				.build(MITweaksConfig.class)
				.register(container, ModConfig.Type.STARTUP)
				.load()
				.listenToLoad(bus)
				.config();
	}
	
	public static ResourceLocation machineId(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(config().machineNamespace(), path);
	}
}

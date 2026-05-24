package net.swedz.mi_tweaks;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.util.TextHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.swedz.mi_tweaks.compat.mi.custom.MITweaksMIRegistries;
import net.swedz.mi_tweaks.datagen.client.provider.LanguageDatagenProvider;
import net.swedz.mi_tweaks.machine.processcondition.SurroundingArea;
import net.swedz.mi_tweaks.network.MITweaksPackets;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.compat.mi.TesseractMI;
import net.swedz.tesseract.neoforge.compat.mi.tooltip.MIParser;
import net.swedz.tesseract.config.ConfigManager;
import net.swedz.tesseract.neoforge.config.ModConfigFileAccess;
import net.swedz.tesseract.neoforge.lang.LangManager;
import net.swedz.tesseract.neoforge.tooltip.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static aztech.modern_industrialization.MITooltips.*;

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
		setupText();
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
		var file = new ModConfigFileAccess(container, ModConfig.Type.STARTUP);
		file.codecs()
				.register(MITweaksConfig.Efficiency.CableTierMaxOverclockOverrides.class, MITweaksConfig.Efficiency.CableTierMaxOverclockOverrides.CODEC)
				.register(MITweaksConfig.MachineList.class, MITweaksConfig.MachineList.CODEC);
		var instance = new ConfigManager(file)
				.build(MITweaksConfig.class)
				.load();
		bus.addListener(FMLCommonSetupEvent.class, (event) -> instance.load(false));
		CONFIG = instance.config();
	}
	
	private static MITweaksText TEXT;
	
	public static MITweaksText text()
	{
		Assert.notNull(TEXT, "Text not yet loaded");
		return TEXT;
	}
	
	private static void setupText()
	{
		var instance = new LangManager(ID)
				.builtinColorStyles()
				.style("tooltip", () -> DEFAULT_STYLE)
				.style("tooltip_subtext", () -> DEFAULT_STYLE.withItalic(true))
				.style("highlighted", () -> HIGHLIGHT_STYLE)
				
				.builtinParsers()
				.parser(SurroundingArea.class, () -> SurroundingArea::text)
				
				.parser("percentage", float.class, () -> (value) -> Parser.FLOAT_PERCENTAGE.parse(value, 0))
				.parser("percentage.1", float.class, () -> (value) -> Parser.FLOAT_PERCENTAGE.parse(value, 1))
				
				.parser("eu_per_tick", long.class, () -> (value) ->
				{
					var amount = TextHelper.getAmountGeneric(value);
					return MIText.EuT.text(amount.digit(), amount.unit());
				})
				
				.parser("short", CableTier.class, () -> MIParser.CABLE_TIER_SHORT)
				
				.parser(ElectricBlastFurnaceBlockEntity.Tier.class, () -> ElectricBlastFurnaceBlockEntity.Tier::getDisplayName)
				.parser(MachineTier.class, () -> (tier) -> (switch (tier)
				{
					case BRONZE -> TEXT.machineTierBronze();
					case STEEL -> TEXT.machineTierSteel();
					case LV -> TEXT.machineTierSingleblockElectric();
					case MULTIBLOCK -> TEXT.machineTierMultiblockElectric();
					case UNLIMITED -> TEXT.machineTierUnlimited();
				}))
				
				.build(MITweaksText.class)
				.load();
		LanguageDatagenProvider.include(instance);
		TEXT = instance.lang();
	}
	
	public static ResourceLocation machineId(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(config().machineNamespace(), path);
	}
}

package net.swedz.mi_tweaks;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.compat.mi.MITweaksMIHookListener;
import net.swedz.mi_tweaks.compat.mi.MITweaksMIHookRegistry;
import net.swedz.mi_tweaks.datagen.client.LanguageDatagenProvider;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MITweaks.ID)
public final class MITweaks
{
	public static final String ID   = "mi_tweaks";
	public static final String NAME = "MI Tweaks";
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(ID, path);
	}
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public MITweaks(IEventBus bus)
	{
		this.loadConfig();
		
		MIHooks.register(ID, new MITweaksMIHookRegistry(), new MITweaksMIHookListener());
		
		bus.addListener(GatherDataEvent.class, (event) ->
				event.getGenerator().addProvider(event.includeClient(), new LanguageDatagenProvider(event)));
	}
	
	private void loadConfig()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MITweaksConfig.SPEC);
		
		CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("extended_industrialization-common.toml"))
				.preserveInsertionOrder()
				.autoreload()
				.writingMode(WritingMode.REPLACE)
				.sync()
				.build();
		configData.load();
		MITweaksConfig.SPEC.setConfig(configData);
		MITweaksConfig.loadConfig();
		
		LOGGER.info("Forcefully early-loaded config");
	}
}

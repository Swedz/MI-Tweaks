package net.swedz.mi_tweaks.datagen.client.provider;

import com.google.common.collect.Sets;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.mi_tweaks.MITweaksTags;
import net.swedz.tesseract.neoforge.lang.LangInstance;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Set;

public final class LanguageDatagenProvider extends LanguageProvider
{
	private static final Set<LangInstance<?>> INSTANCES = Sets.newHashSet();
	
	public static void include(LangInstance<?> instance)
	{
		INSTANCES.add(instance);
	}
	
	public LanguageDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), MITweaks.ID, "en_us");
	}
	
	@Override
	protected void addTranslations()
	{
		for(var instance : INSTANCES)
		{
			instance.datagen(this);
		}
		
		for(ItemHolder item : MITweaksItems.values())
		{
			this.add(item.asItem(), item.identifier().englishName());
		}
		
		this.add(MITweaksItems.MACHINE_BLUEPRINT.asItem().getDescriptionId() + ".blank", "Blank Machine Blueprint");
		
		this.add(MITweaksTags.FE_CABLES, "FE Cables");
		
		this.add("itemGroup.%s.%s".formatted(MITweaks.ID, MITweaks.ID), MITweaks.NAME);
	}
}

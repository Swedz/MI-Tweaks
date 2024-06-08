package net.swedz.mi_tweaks.datagen.client;

import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksText;

public final class LanguageDatagenProvider extends LanguageProvider
{
	public LanguageDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), MITweaks.ID, "en_us");
	}
	
	@Override
	protected void addTranslations()
	{
		for(MITweaksText text : MITweaksText.values())
		{
			this.add(text.getTranslationKey(), text.englishText());
		}
	}
}

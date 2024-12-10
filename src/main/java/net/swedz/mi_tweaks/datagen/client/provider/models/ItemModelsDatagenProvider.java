package net.swedz.mi_tweaks.datagen.client.provider.models;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksItems;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

public final class ItemModelsDatagenProvider extends ItemModelProvider
{
	public ItemModelsDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), MITweaks.ID, event.getExistingFileHelper());
	}
	
	@Override
	protected void registerModels()
	{
		for(ItemHolder item : MITweaksItems.values())
		{
			if(item.hasModelProvider())
			{
				item.modelProvider().accept(this);
			}
		}
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}

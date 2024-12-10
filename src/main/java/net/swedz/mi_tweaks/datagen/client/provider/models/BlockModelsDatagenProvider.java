package net.swedz.mi_tweaks.datagen.client.provider.models;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksBlocks;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;

public final class BlockModelsDatagenProvider extends BlockStateProvider
{
	public BlockModelsDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), MITweaks.ID, event.getExistingFileHelper());
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		for(BlockHolder<?> block : MITweaksBlocks.values())
		{
			if(block.hasModelProvider())
			{
				block.modelProvider().accept(this);
			}
		}
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}

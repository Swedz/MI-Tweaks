package net.swedz.mi_tweaks.datagen.server.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksBlocks;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;

import java.util.Comparator;

public final class BlockTagDatagenProvider extends BlockTagsProvider
{
	public BlockTagDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), event.getLookupProvider(), MITweaks.ID, event.getExistingFileHelper());
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		for(BlockHolder<?> block : MITweaksBlocks.values().stream().sorted(Comparator.comparing((block) -> block.identifier().id())).toList())
		{
			for(TagKey<Block> tag : block.tags())
			{
				this.tag(tag).add(block.get());
			}
		}
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}

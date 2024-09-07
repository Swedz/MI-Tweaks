package net.swedz.mi_tweaks;

import net.swedz.tesseract.neoforge.registry.SortOrder;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface MITweaksSortOrder
{
	SortOrder MACHINES   = new SortOrder(0);
	SortOrder BLUEPRINTS = new SortOrder(1);
}

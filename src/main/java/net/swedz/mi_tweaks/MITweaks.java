package net.swedz.mi_tweaks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MITweaks.ID)
public final class MITweaks
{
	public static final String ID = "mi_tweaks";
	public static final String NAME = "MI Tweaks";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public MITweaks(IEventBus bus)
	{
	}
}

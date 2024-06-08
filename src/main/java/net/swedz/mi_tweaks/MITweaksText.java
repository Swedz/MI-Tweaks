package net.swedz.mi_tweaks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum MITweaksText
{
	MACHINE_MENU_VOLTAGE_PREFIX("(%s) "),
	MACHINE_VOLTAGE_RECIPES("Allows machines to run %s recipes"),
	MACHINE_VOLTAGE_RUNS_AT("Runs recipes at %s"),
	RECIPE_REQUIRES_COIL("Requires coil: %s"),
	RECIPE_REQUIRES_VOLTAGE("Requires voltage: %s"),
	WATER_PUMP_ENVIRONMENT_1("Invalid Pump Environment"),
	WATER_PUMP_ENVIRONMENT_2("Must be in Ocean or River biome.");
	
	private final String englishText;
	
	MITweaksText(String englishText)
	{
		this.englishText = englishText;
	}
	
	public String englishText()
	{
		return englishText;
	}
	
	public String getTranslationKey()
	{
		return "text.%s.%s".formatted(MITweaks.ID, this.name().toLowerCase());
	}
	
	public MutableComponent text()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	public MutableComponent text(Object... args)
	{
		return Component.translatable(this.getTranslationKey(), args);
	}
}

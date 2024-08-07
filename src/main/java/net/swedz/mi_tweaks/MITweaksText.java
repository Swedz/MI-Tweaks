package net.swedz.mi_tweaks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum MITweaksText
{
	BLUEPRINT_MACHINE("%s"),
	BLUEPRINT_LEARN("Press %s to learn this blueprint"),
	BLUEPRINT_LEARNED("You have learned the blueprints for %s"),
	BLUEPRINT_MISSING_INVENTORY("You do not have the blueprints for this machine"),
	BLUEPRINT_MISSING_LEARN("You have not learned the blueprints for this machine"),
	ENERGY_BAR_CURRENT_CONSUMPTION("Current consumption : %d EU/t"),
	LEARNING_DISABLED_BUT_REQUIRING_LEARNING("WARNING: Your config is set to require learning for some machine blueprint requirement options but you do not have learning enabled. Be sure to enable learning or else you may be unable to use machines."),
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

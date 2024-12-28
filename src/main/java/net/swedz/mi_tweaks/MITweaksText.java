package net.swedz.mi_tweaks;

import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

public enum MITweaksText implements TranslatableTextEnum
{
	ATTRIBUTE_HEAT_PROTECTION("Heat Protection"),
	ATTRIBUTE_VALUE_GIVES("Gives"),
	BLUEPRINT_MACHINE("%s"),
	BLUEPRINT_LEARN("Press %s to learn this blueprint"),
	BLUEPRINT_LEARNED("You have learned the blueprints for %s"),
	BLUEPRINT_MISSING_INVENTORY("You do not have the blueprints for this machine"),
	BLUEPRINT_MISSING_LEARN("You have not learned the blueprints for this machine"),
	ENERGY_BAR_CURRENT_CONSUMPTION("Current consumption : %d EU/t"),
	FLUX_TRANSFORMER_HELP("Converts EU to FE at a rate of %d FE per EU"),
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
	
	@Override
	public String englishText()
	{
		return englishText;
	}
	
	@Override
	public String getTranslationKey()
	{
		return "text.%s.%s".formatted(MITweaks.ID, this.name().toLowerCase());
	}
}

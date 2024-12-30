package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;

public interface BarHelper
{
	default ProgressBar.Parameters progressBar(int renderX, int renderY, String type)
	{
		return new ProgressBar.Parameters(renderX, renderY, type);
	}
	
	default RecipeEfficiencyBar.Parameters efficiencyBar(int renderX, int renderY)
	{
		return new RecipeEfficiencyBar.Parameters(renderX, renderY);
	}
	
	default EnergyBar.Parameters energyBar(int renderX, int renderY)
	{
		return new EnergyBar.Parameters(renderX, renderY);
	}
}

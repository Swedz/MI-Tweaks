package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;

public interface BarHelper
{
	default ProgressBar.Params progressBar(int renderX, int renderY, String type)
	{
		return new ProgressBar.Params(renderX, renderY, type);
	}
	
	default RecipeEfficiencyBar.Params efficiencyBar(int renderX, int renderY)
	{
		return new RecipeEfficiencyBar.Params(renderX, renderY);
	}
	
	default EnergyBar.Params energyBar(int renderX, int renderY)
	{
		return new EnergyBar.Params(renderX, renderY);
	}
}

package net.swedz.mi_tweaks.compat.mi;

import aztech.modern_industrialization.machines.GuiComponentsClient;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessConditions;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.MITweaksTooltips;
import net.swedz.mi_tweaks.guicomponent.exposecabletier.ExposeCableTierGui;
import net.swedz.mi_tweaks.guicomponent.exposecabletier.ExposeCableTierGuiClient;
import net.swedz.mi_tweaks.machineprocesscondition.EBFCoilProcessCondition;
import net.swedz.mi_tweaks.machineprocesscondition.VoltageProcessCondition;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ClientGuiComponentsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;

public final class MITweaksMIHookListener implements MIHookListener
{
	@Override
	public void clientGuiComponents(ClientGuiComponentsMIHookContext hook)
	{
		GuiComponentsClient.register(ExposeCableTierGui.ID, ExposeCableTierGuiClient::new);
	}
	
	@Override
	public void machineProcessConditions(MachineProcessConditionsMIHookContext hook)
	{
		MachineProcessConditions.register(MITweaks.id("voltage"), VoltageProcessCondition.CODEC);
		MachineProcessConditions.register(MITweaks.id("ebf_coil"), EBFCoilProcessCondition.CODEC);
	}
	
	@Override
	public void tooltips()
	{
		MITweaksTooltips.init();
	}
}

package net.swedz.mi_tweaks;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import aztech.modern_industrialization.machines.init.MachineTier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.swedz.mi_tweaks.machine.processcondition.SurroundingArea;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.Parsed;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;

public interface MITweaksText
{
	@LangKey(text = "Heat Protection")
	MutableComponent attributeHeatProtection();
	
	@LangKey(text = "Gives")
	MutableComponent attributeValueGives();
	
	@LangKey(text = "Press %s to learn this blueprint")
	@WithStyle("tooltip")
	MutableComponent blueprintLearn(@Parsed("keybind") @WithStyle("highlighted") String keybind);
	
	@LangKey(text = "You have learned the blueprints for %s")
	@WithStyle("green")
	MutableComponent blueprintLearned(Block machine);
	
	@LangKey(text = "%s")
	@WithStyle("tooltip")
	MutableComponent blueprintMachine(Block machine);
	
	@LangKey(text = "You do not have the blueprints for this machine")
	@WithStyle("red")
	MutableComponent blueprintMissingInventory();
	
	@LangKey(text = "You have not learned the blueprints for this machine")
	@WithStyle("red")
	MutableComponent blueprintMissingLearn();
	
	@LangKey(text = "Current consumption : %s EU/t")
	MutableComponent energyBarCurrentConsumption(long eu);
	
	@LangKey(text = "Converts FE to EU at a rate of %s EU per FE")
	@WithStyle("tooltip")
	MutableComponent euTransformerHelp(@WithStyle("highlighted") double eu);
	
	@LangKey(text = "Converts EU to FE at a rate of %s FE per EU")
	@WithStyle("tooltip")
	MutableComponent fluxTransformerHelp(@WithStyle("highlighted") double fe);
	
	@LangKey(text = "WARNING: Your config is set to require learning for some machine blueprint requirement options but you do not have learning enabled. Be sure to enable learning or else you may be unable to use machines.")
	@WithStyle("gold")
	MutableComponent learningDisabledButRequiringLearning();
	
	@LangKey(text = "Allows machines to run up to %s")
	@WithStyle("tooltip")
	MutableComponent machineHullAndHatchMaxOverclock(@Parsed("eu_per_tick") @WithStyle("highlighted") long euPerTick);
	
	@LangKey(text = "(%s) ")
	MutableComponent machineMenuVoltagePrefix(@Parsed("short") CableTier tier);
	
	@LangKey(text = "Bronze")
	MutableComponent machineTierBronze();
	
	@LangKey(text = "Electric (Multiblock)")
	MutableComponent machineTierMultiblockElectric();
	
	@LangKey(text = "Electric (Singleblock)")
	MutableComponent machineTierSingleblockElectric();
	
	@LangKey(text = "Steel")
	MutableComponent machineTierSteel();
	
	@LangKey(text = "Unlimited")
	MutableComponent machineTierUnlimited();
	
	@LangKey(text = "Allows machines to run %s recipes")
	@WithStyle("tooltip")
	MutableComponent machineVoltageRecipes(@Parsed("short") @WithStyle("highlighted") CableTier tier);
	
	@LangKey(text = "Requires coil: %s")
	MutableComponent recipeRequiresCoil(ElectricBlastFurnaceBlockEntity.Tier tier);
	
	@LangKey(text = "Requires tier: %s")
	MutableComponent recipeRequiresMachineTier(MachineTier tier);
	
	@LangKey(text = "Requires %s %s(s) %swithin %s blocks")
	MutableComponent recipeRequiresNearbyEntity(int count, EntityType<?> entityType, SurroundingArea area, int range);
	
	@LangKey(text = "Requires %s water %swithin %s blocks")
	MutableComponent recipeRequiresOpenWater(@Parsed("percentage.1") float fillPercentage, SurroundingArea area, int range);
	
	@LangKey(text = "above ")
	MutableComponent recipeRequiresSurroundingAreaAbove();
	
	@LangKey(text = "")
	MutableComponent recipeRequiresSurroundingAreaAll();
	
	@LangKey(text = "at and above ")
	MutableComponent recipeRequiresSurroundingAreaAtAndAbove();
	
	@LangKey(text = "at and below ")
	MutableComponent recipeRequiresSurroundingAreaAtAndBelow();
	
	@LangKey(text = "below ")
	MutableComponent recipeRequiresSurroundingAreaBelow();
	
	@LangKey(text = "Requires voltage: %s")
	MutableComponent recipeRequiresVoltage(@Parsed("short") CableTier tier);
	
	@LangKey(text = "Invalid Pump Environment")
	@WithStyle("red")
	MutableComponent waterPumpEnvironment1();
	
	@LangKey(text = "Must be in Ocean or River biome.")
	@WithStyle("red")
	MutableComponent waterPumpEnvironment2();
}

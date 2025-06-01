package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.compat.kubejs.machine.ExtraMachineConfig;
import aztech.modern_industrialization.compat.kubejs.machine.ShapeTemplateHelper;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.init.MultiblockMachines;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.resources.ResourceLocation;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformers;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied.ElectricMultipliedCraftingMultiblockBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied.SteamMultipliedCraftingMultiblockBlockEntity;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class RegisterBatchMultiblocksEventJS implements KubeEvent, ShapeTemplateHelper, RecipeTypeHelper, BarHelper
{
	private final MultiblockMachinesMIHookContext hook;
	
	public RegisterBatchMultiblocksEventJS(MultiblockMachinesMIHookContext hook)
	{
		this.hook = hook;
	}
	
	private void create(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			Consumer<WorkstationAdder> workstations,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			Function<BEP, MachineBlockEntity> factory
	)
	{
		ResourceLocation id = MITweaks.id(name);
		MachineCasing casing = MachineCasings.get(controllerCasingId);
		
		hook.register(englishName, name, overlayFolder, casing, frontOverlay, topOverlay, sideOverlay, factory);
		
		ReiMachineRecipes.registerMultiblockShape(id, shape);
		
		WorkstationAdder workstationAdder = new WorkstationAdder();
		workstations.accept(workstationAdder);
		for(ResourceLocation workstation : workstationAdder.get())
		{
			ReiMachineRecipes.registerWorkstation(workstation, id);
		}
	}
	
	public void steam(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			Consumer<WorkstationAdder> workstations,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier
	)
	{
		this.create(
				englishName, name, recipeType, shape,
				workstations,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new SteamMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), new ShapeTemplate[]{shape},
						OverclockComponent.getDefaultCatalysts(),
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				)
		);
	}
	
	public void electric(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			Consumer<WorkstationAdder> workstations,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier
	)
	{
		this.create(
				englishName, name, recipeType, shape,
				workstations,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new ElectricMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), new ShapeTemplate[]{shape},
						MachineTier.LV,
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				)
		);
	}
	
	private void createStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			Function<BEP, MachineBlockEntity> factory,
			List<Consumer<MultiblockMachines.Rei>> reiConfigs
	)
	{
		ResourceLocation id = MITweaks.id(name);
		MachineCasing casing = MachineCasings.get(controllerCasingId);
		
		hook.register(englishName, name, overlayFolder, casing, frontOverlay, topOverlay, sideOverlay, factory);
		
		ReiMachineRecipes.registerMultiblockShape(id, shape);
		
		var rei = new MultiblockMachines.Rei(englishName, id, recipeType, progressBar)
				.items(itemInputPositions, itemOutputPositions)
				.fluids(fluidInputPositions, fluidOutputPositions);
		for(var reiConfig : reiConfigs)
		{
			reiConfig.accept(rei);
		}
		rei.register();
	}
	
	public void steamStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier,
			
			Consumer<ExtraMachineConfig.CraftingMultiBlock> extraConfig
	)
	{
		var config = new ExtraMachineConfig.CraftingMultiBlock();
		config.reiConfigs.add((rei) -> rei.steam(true).extraTest((recipe) -> recipe.eu <= 4));
		extraConfig.accept(config);
		
		this.createStandalone(
				englishName, name, recipeType, shape,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new SteamMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), new ShapeTemplate[]{shape},
						config.steamOverclockCatalysts,
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				),
				config.reiConfigs
		);
	}
	
	public void steamStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier
	)
	{
		this.steamStandalone(
				englishName, name, recipeType, shape,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				batchSize, euCostMultiplier,
				(config) ->
				{
				}
		);
	}
	
	public void electricStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier,
			
			Consumer<ExtraMachineConfig.CraftingMultiBlock> extraConfig
	)
	{
		var config = new ExtraMachineConfig.CraftingMultiBlock();
		extraConfig.accept(config);
		
		this.createStandalone(
				englishName, name, recipeType, shape,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new ElectricMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), new ShapeTemplate[]{shape},
						MachineTier.LV,
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				),
				config.reiConfigs
		);
	}
	
	public void electricStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier
	)
	{
		this.electricStandalone(
				englishName, name, recipeType, shape,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				batchSize, euCostMultiplier,
				(config) ->
				{
				}
		);
	}
}

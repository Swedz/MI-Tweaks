package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.compat.kubejs.machine.ShapeTemplateHelper;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformers;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied.ElectricMultipliedCraftingMultiblockBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied.SteamMultipliedCraftingMultiblockBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineGuiConfiguration;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;

import java.util.function.Consumer;

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
			
			MachineBlockEntityFactory factory
	)
	{
		var casing = MachineCasings.get(controllerCasingId);
		
		var workstationAdder = new WorkstationAdder();
		workstations.accept(workstationAdder);
		
		var builder = hook.builder(name, englishName, factory)
				.builtinModel(casing, overlayFolder, (m) -> m.front(frontOverlay).top(topOverlay).side(sideOverlay))
				.registerMachine()
				.registerMultiblockShape(shape);
		for(var workstation : workstationAdder.get())
		{
			builder.registerAsWorkstationFor(workstation);
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
						bep, MITweaks.machineId(name), new ShapeTemplate[]{shape},
						OverclockComponent.getDefaultCatalysts(),
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				)
		);
	}
	
	public void electric(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			Consumer<WorkstationAdder> workstations,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier, boolean multiblockTier
	)
	{
		this.create(
				englishName, name, recipeType, shape,
				workstations,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new ElectricMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.machineId(name), new ShapeTemplate[]{shape},
						multiblockTier ? MachineTier.MULTIBLOCK : MachineTier.LV,
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
		this.electric(
				englishName, name, recipeType, shape,
				workstations,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				batchSize, euCostMultiplier, false
		);
	}
	
	private void createStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape, SteamMode steamMode,
			
			ProgressBar.Params progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			MachineBlockEntityFactory factory,
			boolean recipeCategoryIsMultiblock,
			Consumer<MachineGuiConfiguration> guiConfig
	)
	{
		var casing = MachineCasings.get(controllerCasingId);
		
		hook.builder(name, englishName, factory)
				.builtinModel(casing, overlayFolder, (m) -> m.front(frontOverlay).top(topOverlay).side(sideOverlay))
				.gui(recipeCategoryIsMultiblock, steamMode, recipeType, (gui) ->
				{
					gui = gui
							.progressBar(progressBar.renderX(), progressBar.renderY(), progressBar.progressBarType())
							.slots((slots) -> slots
									.append(itemInputPositions, itemOutputPositions, fluidInputPositions, fluidOutputPositions));
					guiConfig.accept(gui);
				})
				.registerMachine()
				.registerMultiblockShape(shape)
				.registerRecipeCategory();
	}
	
	public void steamStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Params progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier
	)
	{
		this.createStandalone(
				englishName, name, recipeType, shape, SteamMode.STEAM_ONLY,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new SteamMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.machineId(name), new ShapeTemplate[]{shape},
						OverclockComponent.getDefaultCatalysts(),
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				),
				true,
				(gui) -> gui
						.predicate((recipe) -> recipe.eu <= 4)
		);
	}
	
	public void electricStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Params progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int batchSize, float euCostMultiplier, boolean multiblockTier
	)
	{
		this.createStandalone(
				englishName, name, recipeType, shape, SteamMode.ELECTRIC_ONLY,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new ElectricMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.machineId(name), new ShapeTemplate[]{shape},
						multiblockTier ? MachineTier.MULTIBLOCK : MachineTier.LV,
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				),
				multiblockTier,
				(gui) ->
				{
				}
		);
	}
	
	public void electricStandalone(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Params progressBar,
			
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
				batchSize, euCostMultiplier, false
		);
	}
}

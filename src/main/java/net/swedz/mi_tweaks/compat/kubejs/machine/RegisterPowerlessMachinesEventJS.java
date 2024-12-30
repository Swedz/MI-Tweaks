package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.compat.kubejs.machine.ShapeTemplateHelper;
import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.resources.ResourceLocation;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.tesseract.neoforge.compat.mi.helper.MachineInventoryHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.powerless.PowerlessMachineBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.powerless.PowerlessMultiblockMachineBlockEntity;

import java.util.function.Consumer;

public final class RegisterPowerlessMachinesEventJS implements KubeEvent, ShapeTemplateHelper, RecipeTypeHelper, BarHelper
{
	private final SingleBlockSpecialMachinesMIHookContext hook;
	
	public RegisterPowerlessMachinesEventJS(SingleBlockSpecialMachinesMIHookContext hook)
	{
		this.hook = hook;
	}
	
	public void singleblock(
			String englishName, String name, MachineRecipeType recipeType,
			
			int backgroundHeight, ProgressBar.Parameters progressBar,
			
			int itemInputs, int itemOutputs, int fluidInputs, int fluidOutputs, int bucketCapacity,
			Consumer<SlotPositions.Builder> itemSlotPositions, Consumer<SlotPositions.Builder> fluidSlotPositions,
			
			String casingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int baseRecipeEU, boolean hasRedstoneControl
	)
	{
		ResourceLocation id = MITweaks.id(name);
		MachineCasing casing = MachineCasings.get(casingId);
		
		MachineGuiParameters.Builder guiParamsBuilder = new MachineGuiParameters.Builder(id, true);
		if(backgroundHeight >= 1)
		{
			guiParamsBuilder.backgroundHeight(backgroundHeight);
		}
		MachineGuiParameters guiParams = guiParamsBuilder.build();
		
		var itemPositions = new SlotPositions.Builder().buildWithConsumer(itemSlotPositions);
		var fluidPositions = new SlotPositions.Builder().buildWithConsumer(fluidSlotPositions);
		
		hook.register(
				englishName, name,
				overlayFolder, casing, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new PowerlessMachineBlockEntity(
						bep, guiParams, progressBar,
						MachineInventoryHelper.buildInventoryComponent(itemInputs, itemOutputs, fluidInputs, fluidOutputs, itemPositions, fluidPositions, 0, bucketCapacity),
						recipeType, baseRecipeEU, hasRedstoneControl
				),
				(bet) ->
				{
					if(itemInputs + itemOutputs > 0)
					{
						MachineBlockEntity.registerItemApi(bet);
					}
					if(fluidInputs + fluidOutputs > 0)
					{
						MachineBlockEntity.registerFluidApi(bet);
					}
				}
		);
		
		MachineCategoryParams category = new MachineCategoryParams(
				englishName, id,
				itemPositions.sublist(0, itemInputs),
				itemPositions.sublist(itemInputs, itemInputs + itemOutputs),
				fluidPositions.sublist(0, fluidInputs),
				fluidPositions.sublist(fluidInputs, fluidInputs + fluidOutputs),
				progressBar, recipeType, (recipe) -> true, false, SteamMode.BOTH
		);
		category.workstations.add(id);
		ReiMachineRecipes.registerCategory(id, category);
		ReiMachineRecipes.registerRecipeCategoryForMachine(id, category.category);
		ReiMachineRecipes.registerMachineClickArea(id, progressBar.toRectangle());
	}
	
	public void multiblock(
			String englishName, String name, MachineRecipeType recipeType, ShapeTemplate shape,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			int baseRecipeEU, boolean hasRedstoneControl
	)
	{
		ResourceLocation id = MITweaks.id(name);
		MachineCasing casing = MachineCasings.get(controllerCasingId);
		
		MachineGuiParameters guiParams = new MachineGuiParameters.Builder(id, false).backgroundHeight(200).build();
		
		hook.register(
				englishName, name,
				overlayFolder, casing, frontOverlay, topOverlay, sideOverlay,
				(bep) -> new PowerlessMultiblockMachineBlockEntity(
						bep, guiParams, shape,
						recipeType, baseRecipeEU, hasRedstoneControl
				)
		);
		ReiMachineRecipes.registerMultiblockShape(id, shape);
		
		MachineCategoryParams category = new MachineCategoryParams(
				englishName, id,
				new SlotPositions.Builder().buildWithConsumer(itemInputPositions),
				new SlotPositions.Builder().buildWithConsumer(itemOutputPositions),
				new SlotPositions.Builder().buildWithConsumer(fluidInputPositions),
				new SlotPositions.Builder().buildWithConsumer(fluidOutputPositions),
				progressBar, recipeType, (recipe) -> true, true, SteamMode.BOTH
		);
		category.workstations.add(id);
		ReiMachineRecipes.registerCategory(id, category);
		ReiMachineRecipes.registerRecipeCategoryForMachine(id, category.category);
		ReiMachineRecipes.registerMachineClickArea(id, progressBar.toRectangle());
	}
}

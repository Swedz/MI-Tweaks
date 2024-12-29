package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.compat.kubejs.machine.ShapeTemplateHelper;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import com.google.common.collect.Lists;
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

public class RegisterBatchMultiblocksEventJS implements KubeEvent, ShapeTemplateHelper
{
	private final MultiblockMachinesMIHookContext hook;
	
	public RegisterBatchMultiblocksEventJS(MultiblockMachinesMIHookContext hook)
	{
		this.hook = hook;
	}
	
	public MachineRecipeType getRecipeType(ResourceLocation id)
	{
		return MIMachineRecipeTypes.getRecipeTypes().stream()
				.filter((type) -> type.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Could not find recipe type with id " + id));
	}
	
	private void create(
			String englishName, String name, MachineRecipeType recipeType, Consumer<WorkstationAdder> workstations, ShapeTemplate shape,
			ResourceLocation controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			int batchSize, float euCostMultiplier,
			Function<BEP, MachineBlockEntity> factory
	)
	{
		ResourceLocation id = MITweaks.id(name);
		MachineCasing casing = MachineCasings.get(controllerCasingId);
		
		hook.register(englishName, name, overlayFolder, casing, frontOverlay, topOverlay, sideOverlay, factory);
		
		ReiMachineRecipes.registerMultiblockShape(id, shape);
		
		WorkstationAdder workstationAdder = new WorkstationAdder();
		workstations.accept(workstationAdder);
		for(ResourceLocation workstation : workstationAdder.workstations)
		{
			ReiMachineRecipes.registerWorkstation(workstation, id);
		}
	}
	
	public void steam(
			String englishName, String name, MachineRecipeType recipeType, Consumer<WorkstationAdder> workstations, ShapeTemplate shape,
			ResourceLocation controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			int batchSize, float euCostMultiplier
	)
	{
		this.create(
				englishName, name, recipeType, workstations, shape,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				batchSize, euCostMultiplier,
				(bep) -> new SteamMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), new ShapeTemplate[]{shape},
						OverclockComponent.getDefaultCatalysts(),
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				)
		);
	}
	
	public void electric(
			String englishName, String name, MachineRecipeType recipeType, Consumer<WorkstationAdder> workstations, ShapeTemplate shape,
			ResourceLocation controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			int batchSize, float euCostMultiplier
	)
	{
		this.create(
				englishName, name, recipeType, workstations, shape,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				batchSize, euCostMultiplier,
				(bep) -> new ElectricMultipliedCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), new ShapeTemplate[]{shape},
						MachineTier.LV,
						recipeType, batchSize, EuCostTransformers.percentage(() -> euCostMultiplier)
				)
		);
	}
	
	public static final class WorkstationAdder
	{
		private final List<ResourceLocation> workstations = Lists.newArrayList();
		
		public WorkstationAdder add(ResourceLocation... workstations)
		{
			this.workstations.addAll(Lists.newArrayList(workstations));
			return this;
		}
	}
}

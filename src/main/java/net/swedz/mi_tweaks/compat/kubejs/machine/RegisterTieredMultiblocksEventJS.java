package net.swedz.mi_tweaks.compat.kubejs.machine;

import aztech.modern_industrialization.compat.kubejs.machine.ShapeTemplateHelper;
import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import com.google.common.collect.Lists;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.resources.ResourceLocation;
import net.swedz.mi_tweaks.MITweaks;
import net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered.CustomMultiblockTier;
import net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered.ElectricTieredCraftingMultiblockBlockEntity;
import net.swedz.mi_tweaks.machine.blockentity.multiblock.tiered.SteamTieredCraftingMultiblockBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class RegisterTieredMultiblocksEventJS implements KubeEvent, ShapeTemplateHelper, RecipeTypeHelper, BarHelper
{
	private final MultiblockMachinesMIHookContext hook;
	
	public RegisterTieredMultiblocksEventJS(MultiblockMachinesMIHookContext hook)
	{
		this.hook = hook;
	}
	
	public CustomMultiblockTier createTier(String id, MachineRecipeType recipeType, ShapeTemplate shape,
										   Consumer<WorkstationAdder> workstations,
										   long maxBaseEu)
	{
		return new CustomMultiblockTier(id, recipeType, shape, workstations, maxBaseEu);
	}
	
	public CustomMultiblockTier createTier(String id, MachineRecipeType recipeType, ShapeTemplate shape,
										   Consumer<WorkstationAdder> workstations)
	{
		return this.createTier(id, recipeType, shape, workstations, MachineTier.MULTIBLOCK.getMaxEu());
	}
	
	private void create(
			String englishName, String name,
			
			Consumer<TierAdder> tiers,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			SteamMode steamMode,
			
			BiFunction<BEP, CustomMultiblockTier[], MachineBlockEntity> factory
	)
	{
		ResourceLocation machineId = MITweaks.id(name);
		MachineCasing casing = MachineCasings.get(controllerCasingId);
		
		var tierAdder = new TierAdder();
		tiers.accept(tierAdder);
		
		hook.register(englishName, name, overlayFolder, casing, frontOverlay, topOverlay, sideOverlay, (bep) -> factory.apply(bep, tierAdder.get().toArray(CustomMultiblockTier[]::new)));
		
		for(int index = 0; index < tierAdder.get().size(); index++)
		{
			var tier = tierAdder.get().get(index);
			long previousMax = index == 0 ? 0 : tierAdder.get().get(index - 1).maxBaseEu();
			long currentMax = tier.maxBaseEu();
			
			ResourceLocation categoryId = MITweaks.id(tier.id());
			
			ReiMachineRecipes.registerMultiblockShape(machineId, tier.shape(), tier.id());
			
			ReiMachineRecipes.registerCategory(categoryId, new MachineCategoryParams(
					englishName, categoryId,
					new SlotPositions.Builder().buildWithConsumer(itemInputPositions),
					new SlotPositions.Builder().buildWithConsumer(itemOutputPositions),
					new SlotPositions.Builder().buildWithConsumer(fluidInputPositions),
					new SlotPositions.Builder().buildWithConsumer(fluidOutputPositions),
					progressBar, tier.recipeType(), (recipe) -> recipe.eu > previousMax && recipe.eu <= tier.maxBaseEu(), true, steamMode
			));
			ReiMachineRecipes.registerRecipeCategoryForMachine(machineId, categoryId);
			ReiMachineRecipes.registerWorkstation(categoryId, machineId);
			for(var workstation : tier.getWorkstations())
			{
				ReiMachineRecipes.registerWorkstation(categoryId, workstation);
			}
		}
		ReiMachineRecipes.registerMachineClickArea(machineId, progressBar.toRectangle());
	}
	
	public void steam(
			String englishName, String name,
			
			Consumer<TierAdder> tiers,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			long maxRecipeEu
	)
	{
		this.create(
				englishName, name,
				tiers,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				SteamMode.STEAM_ONLY,
				(bep, t) -> new SteamTieredCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), t,
						maxRecipeEu,
						OverclockComponent.getDefaultCatalysts()
				)
		);
	}
	
	public void steam(
			String englishName, String name,
			
			Consumer<TierAdder> tiers,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay
	)
	{
		this.steam(
				englishName, name,
				tiers,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				MachineTier.MULTIBLOCK.getMaxEu()
		);
	}
	
	public void electric(
			String englishName, String name,
			
			Consumer<TierAdder> tiers,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
			
			long maxRecipeEu
	)
	{
		this.create(
				englishName, name,
				tiers,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				SteamMode.ELECTRIC_ONLY,
				(bep, t) -> new ElectricTieredCraftingMultiblockBlockEntity(
						bep, MITweaks.id(name), t,
						maxRecipeEu
				)
		);
	}
	
	public void electric(
			String englishName, String name,
			
			Consumer<TierAdder> tiers,
			
			ProgressBar.Parameters progressBar,
			
			Consumer<SlotPositions.Builder> itemInputPositions, Consumer<SlotPositions.Builder> itemOutputPositions,
			Consumer<SlotPositions.Builder> fluidInputPositions, Consumer<SlotPositions.Builder> fluidOutputPositions,
			
			String controllerCasingId, String overlayFolder, boolean frontOverlay, boolean topOverlay, boolean sideOverlay
	)
	{
		this.electric(
				englishName, name,
				tiers,
				progressBar,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				controllerCasingId, overlayFolder, frontOverlay, topOverlay, sideOverlay,
				MachineTier.MULTIBLOCK.getMaxEu()
		);
	}
	
	public static final class TierAdder
	{
		private final List<CustomMultiblockTier> tiers = Lists.newArrayList();
		
		public TierAdder add(CustomMultiblockTier... tiers)
		{
			this.tiers.addAll(Lists.newArrayList(tiers));
			return this;
		}
		
		public List<CustomMultiblockTier> get()
		{
			return tiers;
		}
	}
}

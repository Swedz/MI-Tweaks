package net.swedz.mi_tweaks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.swedz.mi_tweaks.blueprint.BlueprintsLearned;
import net.swedz.mi_tweaks.blueprint.CopyBlueprintRecipe;
import net.swedz.mi_tweaks.items.MachineBlueprintItem;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Comparator;
import java.util.function.Supplier;

public final class MITweaksOtherRegistries
{
	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MITweaks.ID);
	
	public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<CopyBlueprintRecipe>> COPY_BLUEPRINT_SERIALIZER = RECIPE_SERIALIZERS.register("copy_blueprint", () -> new SimpleCraftingRecipeSerializer<>(CopyBlueprintRecipe::new));
	
	private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MITweaks.ID);
	
	public static final Supplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register(MITweaks.ID, () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.%s.%s".formatted(MITweaks.ID, MITweaks.ID)))
			.icon(() -> MITweaksItems.MACHINE_BLUEPRINT.asItem().getDefaultInstance())
			.displayItems((params, output) ->
			{
				Comparator<ItemHolder> compareBySortOrder = Comparator.comparing(ItemHolder::sortOrder);
				Comparator<ItemHolder> compareByName = Comparator.comparing((i) -> i.identifier().id());
				MITweaksItems.values().stream()
						.sorted(compareBySortOrder.thenComparing(compareByName))
						.forEach((item) ->
						{
							output.accept(item);
							
							if(item.asItem() instanceof MachineBlueprintItem)
							{
								MITweaksConfig.machineBlueprintsMachines.stream()
										.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
										.forEach((machineBlock) ->
										{
											ItemStack blueprintItem = MITweaksItems.MACHINE_BLUEPRINT.asItem().getDefaultInstance();
											MachineBlueprintItem.setMachineBlock(blueprintItem, machineBlock);
											output.accept(blueprintItem);
										});
							}
						});
			})
			.build());
	
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MITweaks.ID);
	
	public static final Supplier<AttachmentType<BlueprintsLearned>> BLUEPRINTS_LEARNED = ATTACHMENT_TYPES.register(
			"blueprints_learned",
			() -> AttachmentType.serializable(BlueprintsLearned::new)
					.copyOnDeath()
					.build()
	);
	
	public static void init(IEventBus bus)
	{
		RECIPE_SERIALIZERS.register(bus);
		CREATIVE_MODE_TABS.register(bus);
		ATTACHMENT_TYPES.register(bus);
	}
}

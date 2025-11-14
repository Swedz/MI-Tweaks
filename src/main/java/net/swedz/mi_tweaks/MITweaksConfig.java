package net.swedz.mi_tweaks;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.machines.MachineBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.swedz.mi_tweaks.constantefficiency.hack.MachineEfficiencyHackOption;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.serialization.MICodecs;
import net.swedz.tesseract.neoforge.config.annotation.ConfigComment;
import net.swedz.tesseract.neoforge.config.annotation.ConfigKey;
import net.swedz.tesseract.neoforge.config.annotation.Range;
import net.swedz.tesseract.neoforge.config.annotation.SubSection;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface MITweaksConfig
{
	@ConfigKey
	default String machineNamespace()
	{
		return MITweaks.ID;
	}
	
	@ConfigKey
	@SubSection
	Tweaks tweaks();
	
	interface Tweaks
	{
		@ConfigKey
		@ConfigComment("Whether water pumps require a water biome (river or ocean) to operate")
		default boolean requireWaterBiomeForPump()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment("Whether the voltage of a machine should be displayed. This includes displaying voltage of hatches and hulls")
		default boolean displayMachineVoltage()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment("Whether efficiency should be locked when a redstone module locks a machine, rather than just the crafting operation")
		default boolean lockEfficiencyWithRedstone()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment("Whether temperature and fuel consumption should be locked when a redstone module locks a multiblock boiler, rather than losing fuel and heat")
		default boolean lockBoilerTemperatureWithRedstone()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment("Whether wrenches should render multiblock shapes in world. If false, then only blueprints will be able to render multiblock shapes in world")
		default boolean wrenchesRenderMultiblockShapes()
		{
			return true;
		}
		
		@ConfigKey
		@ConfigComment("Whether the tooltip on the energy bar should display the current energy consumption of the machine")
		default boolean displayEnergyConsumptionOnEnergyBar()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment("The duration in ticks of burn time to apply when holding an item tagged mi_tweaks:very_hot")
		@Range.Integer(min = 1, max = Integer.MAX_VALUE)
		default int veryHotItemsBurnTime()
		{
			return 5 * 20;
		}
		
		@ConfigKey
		@ConfigComment("Whether item fuels should be blocked in multiblock boilers")
		default boolean disableItemFuelInMultiblockBoilers()
		{
			return false;
		}
	}
	
	@ConfigKey
	@SubSection
	FluxTransformer fluxTransformer();
	
	interface FluxTransformer
	{
		@ConfigKey
		@ConfigComment("The EU capacity of the Flux Transformer")
		@Range.Long(min = 1, max = Long.MAX_VALUE)
		default long capacity()
		{
			return 200 * CableTier.HV.getEu();
		}
		
		@ConfigKey
		@ConfigComment("The max FE extractable at a time for the Flux Transformer")
		@Range.Long(min = 1, max = Long.MAX_VALUE)
		default long maxExtract()
		{
			return Long.MAX_VALUE;
		}
		
		@ConfigKey
		@ConfigComment("The multiplier to apply on the EU to get FE")
		@Range.Double(min = 0.1, max = Double.MAX_VALUE)
		default double conversionRate()
		{
			return 1;
		}
	}
	
	@ConfigKey
	@SubSection
	EUTransformer euTransformer();
	
	interface EUTransformer
	{
		@ConfigKey
		@ConfigComment("The EU capacity of the EU Transformer")
		@Range.Long(min = 1, max = Long.MAX_VALUE)
		default long capacity()
		{
			return 200 * CableTier.HV.getEu();
		}
		
		@ConfigKey
		@ConfigComment("The max FE insertable at a time for the EU Transformer")
		@Range.Long(min = 1, max = Long.MAX_VALUE)
		default long maxInsert()
		{
			return Long.MAX_VALUE;
		}
		
		@ConfigKey
		@ConfigComment("The multiplier to apply on the FE to get EU")
		@Range.Double(min = 0.1, max = Double.MAX_VALUE)
		default double conversionRate()
		{
			return 1;
		}
	}
	
	@ConfigKey
	@SubSection
	Efficiency efficiency();
	
	interface Efficiency
	{
		@ConfigKey
		@ConfigComment({
				"The machine efficiency hack mode to use. Only applies to electric machines",
				"DISABLED = No change will be made to MI's efficiency behavior",
				"ALWAYS_BASE = The efficiency will always be the base machine eu (or recipe eu if it's greater) + upgrades",
				"ALWAYS_MAX = The efficiency will always be forced to max"
		})
		default MachineEfficiencyHackOption hack()
		{
			return MachineEfficiencyHackOption.DISABLED;
		}
		
		@ConfigKey
		@ConfigComment("Whether efficiency bar and multiblock efficiency data should be hidden or not")
		default boolean hide()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment({
				"Whether the casing max overclock overrides (as per `casing_max_overclock_overrides`) should be used",
				"This only applies to electric machines. Also applies to multiblocks but uses the highest tier energy hatch's casing",
				"Note that when this is enabled, it will override any behavior relating to the max efficiency for the `hack` mode (for example, as in `USE_VOLTAGE`)"
		})
		default boolean useCasingMaxOverclockOverrides()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment({
				"The base max EU/t a machine can run at for a given casing",
				"Range: > 0"
		})
		default CableTierMaxOverclockOverrides casingMaxOverclockOverrides()
		{
			return new CableTierMaxOverclockOverrides(Map.of(
					CableTier.LV, 32L,
					CableTier.MV, 128L,
					CableTier.HV, 512L,
					CableTier.EV, 2048L,
					CableTier.SUPERCONDUCTOR, 8192L
			));
		}
		
		final class CableTierMaxOverclockOverrides
		{
			public static final Codec<CableTierMaxOverclockOverrides> CODEC = Codec.unboundedMap(
					MICodecs.CABLE_TIER,
					CodecHelper.longRange(1, Long.MAX_VALUE)
			).xmap(CableTierMaxOverclockOverrides::new, (value) -> value.overrides);
			
			private final Map<CableTier, Long> overrides;
			
			private CableTierMaxOverclockOverrides(Map<CableTier, Long> overrides)
			{
				this.overrides = overrides;
			}
			
			public long get(CableTier cableTier)
			{
				Assert.notNull(cableTier);
				
				return overrides.getOrDefault(cableTier, 32L);
			}
		}
	}
	
	@ConfigKey
	@SubSection
	MachineBlueprints machineBlueprints();
	
	interface MachineBlueprints
	{
		@ConfigKey
		@ConfigComment("Whether the learning system for blueprints is enabled or not. If true, then blueprints can be right-clicked to become learned")
		default boolean learning()
		{
			return false;
		}
		
		@ConfigKey
		@ConfigComment({
				"The list of machine ids (accepts regex) that require blueprints to place",
				"This is only used if any type of machine blueprint requirement is enabled"
		})
		default MachineList machines()
		{
			return new MachineList(List.of());
		}
		
		@ConfigKey
		@ConfigComment({
				"This section's options use the following values:",
				"DISABLED = Machine blueprints are not required at all",
				"INVENTORY = The machine blueprint must be in the inventory of the player",
				"LEARN = Once a machine blueprint is in the inventory of the player, it becomes 'learned' and is not required in the inventory",
				"INVENTORY_OR_LEARN = The blueprint must be in the inventory of the player or it needs to have been learned"
		})
		@SubSection
		Required required();
		
		interface Required
		{
			@ConfigKey
			@ConfigComment("The machine blueprint requirement mode to use for displaying the tooltip warning")
			default MachineBlueprintRequiredMode tooltip()
			{
				return MachineBlueprintRequiredMode.DISABLED;
			}
			
			@ConfigKey
			@ConfigComment("The machine blueprint requirement mode to use for placing machines")
			default MachineBlueprintRequiredMode placing()
			{
				return MachineBlueprintRequiredMode.DISABLED;
			}
			
			@ConfigKey
			@ConfigComment("The machine blueprint requirement mode to use for rendering hatch positions when holding hatches")
			default MachineBlueprintRequiredMode renderingHatches()
			{
				return MachineBlueprintRequiredMode.DISABLED;
			}
		}
	}
	
	final class MachineList
	{
		public static final Codec<MachineList> CODEC = Codec.list(Codec.STRING).xmap(MachineList::new, (list) -> list.config);
		
		private final List<String> config;
		private final List<ResourceLocation> machineIds;
		private final List<Block> machineBlocks;
		
		private MachineList(List<String> config)
		{
			this.config = Collections.unmodifiableList(config);
			this.machineIds = config.stream()
					.flatMap(MachineList::getMatchingMachineBlocks)
					.map(BuiltInRegistries.BLOCK::getKey)
					.toList();
			this.machineBlocks = machineIds.stream()
					.map(BuiltInRegistries.BLOCK::get)
					.toList();
		}
		
		public int size()
		{
			return machineIds.size();
		}
		
		public boolean isEmpty()
		{
			return machineIds.isEmpty();
		}
		
		public boolean contains(Block machineBlock)
		{
			return machineIds.contains(BuiltInRegistries.BLOCK.getKey(machineBlock));
		}
		
		public Block get(int index)
		{
			return BuiltInRegistries.BLOCK.get(machineIds.get(index));
		}
		
		public Stream<Block> stream()
		{
			return machineBlocks.stream();
		}
		
		private static Stream<Block> getMatchingMachineBlocks(String regex)
		{
			Pattern pattern = Pattern.compile(regex);
			return BuiltInRegistries.BLOCK.stream()
					.filter((block) ->
							block instanceof MachineBlock &&
							pattern.matcher(BuiltInRegistries.BLOCK.getKey(block).toString()).matches());
		}
	}
	
	enum MachineBlueprintRequiredMode
	{
		DISABLED(null, false),
		INVENTORY(() -> MITweaks.text().blueprintMissingInventory(), false),
		LEARN(() -> MITweaks.text().blueprintMissingLearn(), true),
		INVENTORY_OR_LEARN(() -> MITweaks.text().blueprintMissingInventory(), true);
		
		private final Supplier<MutableComponent> tooltip;
		private final boolean                    learning;
		
		MachineBlueprintRequiredMode(Supplier<MutableComponent> tooltip, boolean learning)
		{
			this.tooltip = tooltip;
			this.learning = learning;
		}
		
		public boolean isDisabled()
		{
			return this == DISABLED;
		}
		
		public boolean isEnabled()
		{
			return !this.isDisabled();
		}
		
		public boolean isLearning()
		{
			return learning;
		}
		
		public MutableComponent tooltip()
		{
			if(tooltip == null)
			{
				throw new UnsupportedOperationException("There is no tooltip for this machine blueprint requirement mode");
			}
			return tooltip.get();
		}
	}
}

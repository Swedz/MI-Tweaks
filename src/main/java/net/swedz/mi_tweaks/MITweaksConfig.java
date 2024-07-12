package net.swedz.mi_tweaks;

import aztech.modern_industrialization.machines.MachineBlock;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = MITweaks.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MITweaksConfig
{
	private static final ModConfigSpec.Builder BUILDER;
	
	private static final ModConfigSpec.BooleanValue                            REQUIRE_WATER_BIOME_FOR_PUMP;
	private static final ModConfigSpec.BooleanValue                            DISPLAY_MACHINE_VOLTAGE;
	private static final ModConfigSpec.BooleanValue                            LOCK_EFFICIENCY_WITH_REDSTONE;
	private static final ModConfigSpec.BooleanValue                            WRENCHES_RENDER_MULTIBLOCK_SHAPES;
	private static final ModConfigSpec.EnumValue<MachineEfficiencyHack>        EFFICIENCY_HACK;
	private static final ModConfigSpec.BooleanValue                            HIDE_MACHINE_EFFICIENCY;
	private static final ModConfigSpec.ConfigValue<List<? extends String>>     MACHINE_BLUEPRINTS_MACHINES;
	private static final ModConfigSpec.EnumValue<MachineBlueprintRequiredMode> MACHINE_BLUEPRINTS_REQUIRED_TOOLTIP;
	private static final ModConfigSpec.EnumValue<MachineBlueprintRequiredMode> MACHINE_BLUEPRINTS_REQUIRED_FOR_PLACING;
	private static final ModConfigSpec.EnumValue<MachineBlueprintRequiredMode> MACHINE_BLUEPRINTS_REQUIRED_FOR_RENDERING_HATCHES;
	
	public static final ModConfigSpec SPEC;
	
	static
	{
		BUILDER = new ModConfigSpec.Builder();
		
		{
			BUILDER.push("tweaks");
			REQUIRE_WATER_BIOME_FOR_PUMP = BUILDER
					.comment("Whether water pumps require a water biome (river or ocean) to operate")
					.define("require_water_biome_for_pump", false);
			DISPLAY_MACHINE_VOLTAGE = BUILDER
					.comment("Whether the voltage of a machine should be displayed. This includes displaying voltage of hatches and hulls")
					.define("display_machine_voltage", false);
			LOCK_EFFICIENCY_WITH_REDSTONE = BUILDER
					.comment("Whether efficiency should be locked when a redstone module locks a machine, rather than just the crafting operation")
					.define("lock_efficiency_with_redstone", false);
			WRENCHES_RENDER_MULTIBLOCK_SHAPES = BUILDER
					.comment("Whether wrenches should render multiblock shapes in world. If false, then only blueprints will be able to render multiblock shapes in world")
					.define("wrenches_render_multiblock_shapes", true);
			BUILDER.pop();
		}
		
		{
			BUILDER.push("efficiency");
			EFFICIENCY_HACK = BUILDER
					.comment(
							"The machine efficiency hack mode to use. Only applies to electric machines",
							"DISABLED = No change will be made to MI's efficiency behavior",
							"ALWAYS_MAX = The efficiency bar will always be forced to max",
							"USE_VOLTAGE = The speed of machines will be determined by their voltage (WARNING! This is designed specifically for pack creators, and existing recipes may not be accessible by all voltages, most notably EBF recipes. Use at your own risk. It is recommended when using this mode to modify recipes with higher EU costs to use the voltage recipe condition or the ebf coil recipe condition)"
					)
					.defineEnum("hack", MachineEfficiencyHack.DISABLED);
			HIDE_MACHINE_EFFICIENCY = BUILDER
					.comment("Whether efficiency bar and multiblock efficiency data should be hidden or not")
					.define("hide", false);
			BUILDER.pop();
		}
		
		{
			BUILDER.push("machine_blueprints");
			MACHINE_BLUEPRINTS_MACHINES = BUILDER
					.comment(
							"The list of machine ids (accepts regex) that require blueprints to place",
							"This is only used if any type of machine blueprint requirement is enabled"
					)
					.defineListAllowEmpty("machines", Lists.newArrayList(), MITweaksConfig::validateMachineId);
			{
				BUILDER.comment(
						"This section's options use the following values:",
						"DISABLED = Machine blueprints are not required at all",
						"INVENTORY = The machine blueprint must be in the inventory of the player",
						"LEARN = Once a machine blueprint is in the inventory of the player, it becomes 'learned' and is not required in the inventory"
				);
				BUILDER.push("required");
				MACHINE_BLUEPRINTS_REQUIRED_TOOLTIP = BUILDER
						.comment("The machine blueprint requirement mode to use for displaying the tooltip warning")
						.defineEnum("tooltip", MachineBlueprintRequiredMode.DISABLED);
				MACHINE_BLUEPRINTS_REQUIRED_FOR_PLACING = BUILDER
						.comment("The machine blueprint requirement mode to use for placing machines")
						.defineEnum("placing", MachineBlueprintRequiredMode.DISABLED);
				MACHINE_BLUEPRINTS_REQUIRED_FOR_RENDERING_HATCHES = BUILDER
						.comment("The machine blueprint requirement mode to use for rendering hatch positions when holding hatches")
						.defineEnum("rendering_hatches", MachineBlueprintRequiredMode.DISABLED);
				BUILDER.pop();
			}
			BUILDER.pop();
		}
		
		SPEC = BUILDER.build();
	}
	
	private static Stream<Block> getMatchingMachineBlocks(String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		return BuiltInRegistries.BLOCK.stream()
				.filter((block) ->
						block instanceof MachineBlock &&
						pattern.matcher(BuiltInRegistries.BLOCK.getKey(block).toString()).matches());
	}
	
	private static boolean validateMachineId(Object element)
	{
		return element instanceof String regex &&
			   getMatchingMachineBlocks(regex).findAny().isPresent();
	}
	
	public static boolean                      requireWaterBiomeForPump;
	public static boolean                      displayMachineVoltage;
	public static boolean                      lockEfficiencyWithRedstone;
	public static boolean                      wrenchesRenderMultiblockShapes;
	public static MachineEfficiencyHack        efficiencyHack;
	public static boolean                      hideMachineEfficiency;
	public static List<Block>                  machineBlueprintsMachines;
	public static MachineBlueprintRequiredMode machineBlueprintsRequiredTooltip;
	public static MachineBlueprintRequiredMode machineBlueprintsRequiredForPlacing;
	public static MachineBlueprintRequiredMode machineBlueprintsRequiredForRenderingHatches;
	
	public static void loadConfig()
	{
		requireWaterBiomeForPump = REQUIRE_WATER_BIOME_FOR_PUMP.get();
		displayMachineVoltage = DISPLAY_MACHINE_VOLTAGE.get();
		lockEfficiencyWithRedstone = LOCK_EFFICIENCY_WITH_REDSTONE.get();
		wrenchesRenderMultiblockShapes = WRENCHES_RENDER_MULTIBLOCK_SHAPES.get();
		efficiencyHack = EFFICIENCY_HACK.get();
		hideMachineEfficiency = HIDE_MACHINE_EFFICIENCY.get();
		machineBlueprintsMachines = MACHINE_BLUEPRINTS_MACHINES.get().stream()
				.flatMap(MITweaksConfig::getMatchingMachineBlocks)
				.toList();
		machineBlueprintsRequiredTooltip = MACHINE_BLUEPRINTS_REQUIRED_TOOLTIP.get();
		machineBlueprintsRequiredForPlacing = MACHINE_BLUEPRINTS_REQUIRED_FOR_PLACING.get();
		machineBlueprintsRequiredForRenderingHatches = MACHINE_BLUEPRINTS_REQUIRED_FOR_RENDERING_HATCHES.get();
	}
	
	@SubscribeEvent
	static void onConfigLoad(ModConfigEvent event)
	{
		loadConfig();
	}
	
	public enum MachineEfficiencyHack
	{
		DISABLED(false, false, false),
		ALWAYS_MAX(true, false, false),
		USE_VOLTAGE(true, true, true);
		
		private final boolean forceMaxEfficiency;
		private final boolean useVoltageForEfficiency;
		private final boolean preventsUpgrades;
		
		MachineEfficiencyHack(boolean forceMaxEfficiency, boolean useVoltageForEfficiency, boolean preventsUpgrades)
		{
			this.forceMaxEfficiency = forceMaxEfficiency;
			this.useVoltageForEfficiency = useVoltageForEfficiency;
			this.preventsUpgrades = preventsUpgrades;
		}
		
		public boolean forceMaxEfficiency()
		{
			return forceMaxEfficiency;
		}
		
		public boolean useVoltageForEfficiency()
		{
			return useVoltageForEfficiency;
		}
		
		public boolean preventsUpgrades()
		{
			return preventsUpgrades;
		}
	}
	
	public enum MachineBlueprintRequiredMode
	{
		DISABLED(null),
		INVENTORY(MITweaksText.MACHINE_BLUEPRINT_MISSING_INVENTORY),
		LEARN(MITweaksText.MACHINE_BLUEPRINT_MISSING_LEARN);
		
		private final MITweaksText tooltip;
		
		MachineBlueprintRequiredMode(MITweaksText tooltip)
		{
			this.tooltip = tooltip;
		}
		
		public boolean isDisabled()
		{
			return this == DISABLED;
		}
		
		public boolean isEnabled()
		{
			return !this.isDisabled();
		}
		
		public MITweaksText tooltip()
		{
			if(tooltip == null)
			{
				throw new UnsupportedOperationException("There is no tooltip for this machine blueprint requirement mode");
			}
			return tooltip;
		}
	}
}

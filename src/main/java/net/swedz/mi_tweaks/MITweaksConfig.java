package net.swedz.mi_tweaks;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = MITweaks.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MITweaksConfig
{
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	
	private static final ModConfigSpec.BooleanValue REQUIRE_WATER_BIOME_FOR_PUMP = BUILDER
			.comment("Whether water pumps require a water biome (river or ocean) to operate")
			.define("tweaks.require_water_biome_for_pump", false);
	
	private static final ModConfigSpec.BooleanValue DISPLAY_MACHINE_VOLTAGE = BUILDER
			.comment("Whether the voltage of a machine should be displayed. This includes displaying voltage of hatches and hulls")
			.define("tweaks.display_machine_voltage", false);
	
	private static final ModConfigSpec.BooleanValue LOCK_EFFICIENCY_WITH_REDSTONE = BUILDER
			.comment("Whether efficiency should be locked when a redstone module locks a machine, rather than just the crafting operation")
			.define("tweaks.lock_efficiency_with_redstone", false);
	
	private static final ModConfigSpec.BooleanValue WRENCHES_RENDER_MULTIBLOCK_SHAPES = BUILDER
			.comment("Whether wrenches should render multiblock shapes in world. If false, then only blueprints will be able to render multiblock shapes in world")
			.define("tweaks.wrenches_render_multiblock_shapes", true);
	
	private static final ModConfigSpec.EnumValue<MachineEfficiencyHack> EFFICIENCY_HACK_MODE = BUILDER
			.comment(
					"The machine efficiency hack mode to use. Only applies to electric machines",
					"DISABLED = No change will be made to MI's efficiency behavior",
					"ALWAYS_MAX = The efficiency bar will always be forced to max",
					"USE_VOLTAGE = The speed of machines will be determined by their voltage (WARNING! This is designed specifically for pack creators, and existing recipes may not be accessible by all voltages, most notably EBF recipes. Use at your own risk. It is recommended when using this mode to modify recipes with higher EU costs to use the voltage recipe condition or the ebf coil recipe condition)"
			)
			.defineEnum("efficiency.hack_mode", MachineEfficiencyHack.DISABLED);
	
	private static final ModConfigSpec.BooleanValue HIDE_MACHINE_EFFICIENCY = BUILDER
			.comment("Whether efficiency bar and multiblock efficiency data should be hidden or not")
			.define("efficiency.hide", false);
	
	private static final ModConfigSpec.EnumValue<MachineBlueprintMode> MACHINE_BLUEPRINTS_MODE = BUILDER
			.comment(
					"The mode to use for machine blueprints",
					"This is only used if any type of machine blueprint requirement is enabled (for example, the 'required_for_placing' option)",
					"LEARN = Once a machine blueprint is in the inventory of a player, it becomes 'learned' and is not required in the inventory for machines",
					"INVENTORY = The machine blueprint must be in the inventory of a player for machines"
			)
			.defineEnum("machine_blueprints.mode", MachineBlueprintMode.LEARN);
	
	private static final ModConfigSpec.ConfigValue<List<? extends String>> MACHINE_BLUEPRINTS_MACHINES = BUILDER
			.comment(
					"The list of machine ids (accepts regex) that require blueprints to place",
					"This is only used if any type of machine blueprint requirement is enabled (for example, the 'required_for_placing' option)"
			)
			.defineListAllowEmpty("machine_blueprints.machines", Lists.newArrayList(), MITweaksConfig::validateMachineId);
	
	private static final ModConfigSpec.BooleanValue MACHINE_BLUEPRINTS_REQUIRED_FOR_PLACING = BUILDER
			.comment("Whether machine blueprints are required to place a machine")
			.define("machine_blueprints.required_for_placing", false);
	
	private static final ModConfigSpec.BooleanValue MACHINE_BLUEPRINTS_REQUIRED_FOR_CRAFTING = BUILDER
			.comment("Whether machine blueprints are required to craft a machine (NOTE: This will only prevent players from crafting machines in crafting grids themselves)")
			.define("machine_blueprints.required_for_crafting", false);
	
	private static Stream<Block> getMatchingMachineBlocks(String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		return BuiltInRegistries.BLOCK.stream()
				.filter((block) ->
						pattern.matcher(BuiltInRegistries.BLOCK.getKey(block).toString()).matches());
	}
	
	private static boolean validateMachineId(Object element)
	{
		return element instanceof String regex &&
			   getMatchingMachineBlocks(regex).findAny().isPresent();
	}
	
	public static final ModConfigSpec SPEC = BUILDER.build();
	
	public static boolean               requireWaterBiomeForPump;
	public static boolean               displayMachineVoltage;
	public static boolean               lockEfficiencyWithRedstone;
	public static boolean               wrenchesRenderMultiblockShapes;
	public static MachineEfficiencyHack efficiencyHackMode;
	public static boolean               hideMachineEfficiency;
	public static MachineBlueprintMode  machineBlueprintsMode;
	public static Set<Block>            machineBlueprintsMachines;
	public static boolean               machineBlueprintsRequiredForPlacing;
	public static boolean               machineBlueprintsRequiredForCrafting;
	
	public static void loadConfig()
	{
		requireWaterBiomeForPump = REQUIRE_WATER_BIOME_FOR_PUMP.get();
		displayMachineVoltage = DISPLAY_MACHINE_VOLTAGE.get();
		lockEfficiencyWithRedstone = LOCK_EFFICIENCY_WITH_REDSTONE.get();
		wrenchesRenderMultiblockShapes = WRENCHES_RENDER_MULTIBLOCK_SHAPES.get();
		efficiencyHackMode = EFFICIENCY_HACK_MODE.get();
		hideMachineEfficiency = HIDE_MACHINE_EFFICIENCY.get();
		machineBlueprintsMode = MACHINE_BLUEPRINTS_MODE.get();
		machineBlueprintsMachines = MACHINE_BLUEPRINTS_MACHINES.get().stream()
				.flatMap(MITweaksConfig::getMatchingMachineBlocks)
				.collect(Collectors.toUnmodifiableSet());
		machineBlueprintsRequiredForPlacing = MACHINE_BLUEPRINTS_REQUIRED_FOR_PLACING.get();
		machineBlueprintsRequiredForCrafting = MACHINE_BLUEPRINTS_REQUIRED_FOR_CRAFTING.get();
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
	
	public enum MachineBlueprintMode
	{
		LEARN,
		INVENTORY
	}
}

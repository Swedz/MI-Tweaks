# Adding Powerless Machines

Adding powerless machines through KubeJS is very similar to how you add machines using MI's KubeJS integration (see
[here](https://github.com/AztechMC/Modern-Industrialization/blob/-/docs/ADDING_MACHINES.md) for documentation). How
multiblock shapes are made won't be thoroughly covered here since that is covered in MI's documentation.

Powerless machines are machines that do not require steam or electricity to operate. They purely take inputs and create
outputs. These machines use recipes exactly the same how every other MI crafting machine uses recipes.

As you may already know, the EU field is required as part of MI machine recipes. This is still the case for powerless
machine recipes. The only difference here is that you define the "base recipe EU" of your machine and that is used for
how much EU a recipe will progress each tick. For the sake of simplicity, I recommend setting all of your recipes for
your powerless machines to consume 1 EU/t and the base recipe EU of your machine to 1 EU.

## Creating Machines

For information on how to put together the assets for your machines, see [here](MACHINE_ASSETS_AND_DATA.md).

The below example is very rudimentary and are simply powerless versions of the machines shown how to be made in the MI
documentation. This includes both an example of a singleblock and multiblock powerless machine.

```js
// Create your recipe types as normal...
let CIRCUIT_ASSEMBLER;
let PYROLYSE_OVEN;

MIMachineEvents.registerRecipeTypes((event) =>
{
	CIRCUIT_ASSEMBLER = event.register("circuit_assembler")
		.withItemInputs()
		.withItemOutputs();
	PYROLYSE_OVEN = event.register("pyrolyse_oven")
		.withItemInputs()
		.withItemOutputs()
		.withFluidInputs()
		.withFluidOutputs();
});

MITweaksMachineEvents.registerPowerlessMachines((event) =>
{
	event.singleblock(
		// English name, internal name
		"Circuit Assembler", "circuit_assembler",
		// Recipe type,
		CIRCUIT_ASSEMBLER,
		// Background height (or -1 for default value), progress bar
		186, event.progressBar(105, 45, "circuit"),
		// Number of slots: item inputs, item outputs, fluid inputs, fluid outputs
		9, 3, 0, 0,
		// Capacity for fluid slots
		16,
		// Item slot positions
		(items) => items.addSlots(42, 27, 3, 3).addSlots(139, 27, 1, 3),
		// Fluid slot positions
		(fluids) =>
		{
		},
		// Casing of the machine, overlay folder, front overlay?, top overlay?, side overlay?
		"steel", "circuit_assembler", true, true, false,
		// Base recipe EU, allow redstone control module?
		1, false
	);

	// Create your multiblock shape...
	// This is the same as how you do it for other multiblock machines in MI's KubeJS plugin
	const pyrolyseHatch = event.hatchOf("item_input", "item_output", "fluid_input", "fluid_output", "energy_input");
	const heatproofMember = event.memberOfBlock("modern_industrialization:heatproof_machine_casing");
	const cupronickelCoilMember = event.memberOfBlock("modern_industrialization:cupronickel_coil");
	const pyrolyseShape = event.layeredShape("heatproof_machine_casing",
		[
			["HHH", "HHH", "HHH"],
			["CCC", "C C", "CCC"],
			["CCC", "C C", "CCC"],
			["HHH", "H#H", "HHH"],
		])
		.key("H", heatproofMember, pyrolyseHatch)
		.key("C", cupronickelCoilMember, event.noHatch())
		.build();
	event.multiblock(
		// English name, internal name
		"Pyrolyse Oven", "pyrolyse_oven",
		// Recipe type, multiblock shape
		PYROLYSE_OVEN, pyrolyseShape,
		// REI progress bar
		event.progressBar(77, 33, "arrow"),
		// REI item inputs, item outputs, fluid inputs, fluid outputs
		(itemInputs) => itemInputs.addSlots(56, 35, 1, 2),
		(itemOutputs) => itemOutputs.addSlot(102, 35),
		(fluidInputs) => fluidInputs.addSlot(36, 35),
		(fluidOutputs) => fluidOutputs.addSlot(122, 35),
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"heatproof_machine_casing", "pyrolyse_oven", true, false, false,
		// Base recipe EU, allow redstone control module?
		1, true
	);
});
```
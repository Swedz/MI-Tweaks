# Adding Batch Multiblocks

Adding batch multiblocks through KubeJS is very similar to how you add multiblocks using MI's KubeJS integration (see
[here](https://github.com/AztechMC/Modern-Industrialization/blob/-/docs/ADDING_MACHINES.md) for documentation). How
multiblock shapes are made won't be thoroughly covered here since that is covered in MI's documentation.

## Creating Machines

You can create both steam and electric batch crafting machines.

For information on how to put together the assets for your machines, see [here](MACHINE_ASSETS_AND_DATA.md).

### Dependent Machines

The main difference here is that you need to retrieve the recipe type and pass that to the creator, you can add
workstations to add this machine to, and you must provide the batch size and EU cost multiplier for this machine. An EU
cost multiplier of 1 means that the EU cost when using the batching machine is the same as the normal machine - and then
using a multiplier of 0.5 means that the EU cost will be halved when using the batching machine.

Steam batch machines can be defined like so:

```js
MITweaksMachineEvents.registerBatchMultiblocks((event) =>
{
	// Create your multiblock shape ...
	// This is the same as how you do it for other multiblock machines in MI's KubeJS plugin
	const hatch = event.hatchOf("item_input", "item_output", "fluid_input");
	const bronzePlatedBricks = event.memberOfBlock("modern_industrialization:bronze_plated_bricks");
	const bronzePipe = event.memberOfBlock("modern_industrialization:bronze_machine_casing_pipe");
	const shape = event.layeredShape("modern_industrialization:bronze_plated_bricks",
		[
			["HHH", "HHH", "HHH"],
			["PPP", "P P", "PPP"],
			["HHH", "H#H", "HHH"]
		])
		.key("H", bronzePlatedBricks, hatch)
		.key("P", bronzePipe, event.noHatch())
		.build();

	event.steam(
		// English name, internal name
		"Large Steam Cutting Machine", "large_steam_cutting_machine",
		// Recipe type, multiblock shape
		event.getRecipeType("modern_industrialization:cutting_machine"), shape,
		// The workstations (in EMI/JEI/REI) to add this machine to
		// In some cases you may want to add it to multiple workstations (if there are bronze/steel tiers of this machine)
		(workstations) => workstations.add(
			"modern_industrialization:bronze_cutting_machine",
			"modern_industrialization:steel_cutting_machine"
		),
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"bronze_plated_bricks", "cutting_machine", true, false, false,
		// Batch size, EU cost multiplier
		8, 0.75
	);
});
```

And then electric batch machines:

```js
MITweaksMachineEvents.registerBatchMultiblocks((event) =>
{
	// Create your multiblock shape ...
	// This is the same as how you do it for other multiblock machines in MI's KubeJS plugin
	const hatch = event.hatchOf("item_input", "item_output", "fluid_input", "fluid_output", "energy_input");
	const steelPlatedBricks = event.memberOfBlock("extended_industrialization:steel_plated_bricks");
	const shape = event.layeredShape("extended_industrialization:steel_plated_bricks",
		[
			["HHH", "MMM", "HHH"],
			["HHH", "M M", "HHH"],
			["HHH", "M#M", "HHH"]
		])
		.key("H", steelPlatedBricks, hatch)
		.key("M", steelPlatedBricks, event.noHatch())
		.build();

	event.electric(
		// English name, internal name
		"Large Chemical Reactor", "large_chemical_reactor",
		// Recipe type, multiblock shape
		event.getRecipeType("modern_industrialization:chemical_reactor"), shape,
		// The workstations (in EMI/JEI/REI) to add this machine to
		// In some cases you may want to add it to multiple workstations (if there are bronze/steel tiers of this machine)
		(workstations) => workstations.add("modern_industrialization:chemical_reactor"),
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"extended_industrialization:steel_plated_bricks", "chemical_reactor", true, false, false,
		// Batch size, EU cost multiplier
		16, 0.75,
		// Should the machine use 128 EU/t as the max base EU (like normal multiblocks)? If false, it will use 32 EU/t (like single block electric machines). Optional, defaults to false
		false
	);
});
```

### Standalone Machines

Note that when creating a batch multiblock, if there is not already an existing machine using the chosen recipe type,
you must use the standalone functions instead of the normal ones. Here is how you would define a standalone batch
multiblock:

```js
// Create your recipe types as normal...
// Or you can use an existing recipe type if you'd like as shown above
let PYROLYSE_OVEN;

MIMachineEvents.registerRecipeTypes((event) =>
{
	PYROLYSE_OVEN = event.register("pyrolyse_oven")
		.withItemInputs()
		.withItemOutputs()
		.withFluidInputs()
		.withFluidOutputs();
});

MITweaksMachineEvents.registerBatchMultiblocks((event) =>
{
	// Create your multiblock shape ...
	// This is the same as how you do it for other multiblock machines in MI's KubeJS plugin
	const pyrolyseHatch = event.hatchOf("item_input", "item_output", "fluid_input", "fluid_output", "energy_input");
	const heatproofMember = event.memberOfBlock("modern_industrialization:heatproof_machine_casing");
	const cupronickelCoilMember = event.memberOfBlock("modern_industrialization:cupronickel_coil");
	const shape = event.layeredShape("heatproof_machine_casing",
		[
			["HHH", "HHH", "HHH"],
			["CCC", "C C", "CCC"],
			["CCC", "C C", "CCC"],
			["HHH", "H#H", "HHH"],
		])
		.key("H", heatproofMember, pyrolyseHatch)
		.key("C", cupronickelCoilMember, event.noHatch())
		.build();

	// You can also use steamStandalone(...)
	event.electricStandalone(
		// English name, internal name
		"Pyrolyse Oven", "pyrolyse_oven",
		// Recipe type, multiblock shape
		PYROLYSE_OVEN, shape,
		// REI progress bar
		event.progressBar(77, 33, "arrow"),
		// REI item inputs, item outputs, fluid inputs, fluid outputs
		(itemInputs) => itemInputs.addSlots(56, 35, 1, 2),
		(itemOutputs) => itemOutputs.addSlot(102, 35),
		(fluidInputs) => fluidInputs.addSlot(36, 35),
		(fluidOutputs) => fluidOutputs.addSlot(122, 35),
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"heatproof_machine_casing", "pyrolyse_oven", true, false, false,
		// Batch size, EU cost multiplier
		16, 1,
		// Should the machine use 128 EU/t as the max base EU (like normal multiblocks)? If false, it will use 32 EU/t (like single block electric machines). Optional, defaults to false
		false,
		// Optional: Additional configuration
		(config) => {}
	);
});
```
# Adding Tiered Multiblocks

Adding tiered multiblock machines through KubeJS is very similar to how you add machines using MI's KubejS integration
(see [here](https://github.com/AztechMC/Modern-Industrialization/blob/-/docs/ADDING_MACHINES.md) for documentation).
How multiblock shapes are made won't be thoroughly covered here since that is covered in MI's documentation.

Tiered multiblocks are machines that have different shapes depending on the tier selected in the menu. Some examples of
this include the Electric Blast Furnace and Distillation Tower from MI. These machines use recipes exactly the same how
every other MI crafting machine uses recipes, aside from the fact that each tier can have its own recipe type defined.

## Creating Machines

For information on how to put together the assets for your machines, see [here](MACHINE_ASSETS_AND_DATA.md).

The below example is very rudimentary and is simply a tiered version of the pyrolyse oven example provided in MI's
documentation.

```js
// Create your recipe types as normal...
let PYROLYSE_OVEN;

MIMachineEvents.registerRecipeTypes((event) =>
{
	PYROLYSE_OVEN = event.register("pyrolyse_oven")
		.withItemInputs()
		.withItemOutputs()
		.withFluidInputs()
		.withFluidOutputs();
});

// Create a shape for a specific tier
function createPyrolyseTier(event, id, coilBlockId, maxBaseEu, multiplier, euCostMultiplier)
{
	// Build your multiblock shape as you normally would
	const pyrolyseHatch = event.hatchOf("item_input", "item_output", "fluid_input", "fluid_output", "energy_input");
	const heatproofMember = event.memberOfBlock("modern_industrialization:heatproof_machine_casing");
	const cupronickelCoilMember = event.memberOfBlock(coilBlockId);
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
	return event.createTier(
		// The id field MUST be globally unique. This is used to register the JEI/REI/EMI categories.
		id,
		// The recipe type supported by this tier, for this example we just use the same recipe type for all categories
		PYROLYSE_OVEN,
		// The multiblock shape to use for this tier
		shape,
		// Defines the additional workstations to display for this tier (aside from the machine block itself)
		// You can provide as many workstations as you would like for your tier
		// Here this just mimics what MI does with the EBF by adding the coil block as a workstation
		// Do note that when using the non-standalone functions to create a machine, the IDs added to this workstation
		//  list are the IDs of the machines to add this machine to its workstations. This is the same as how the batch
		//  multiblock registering works for workstations.
		(workstations) => workstations.add(coilBlockId),
		// The max EU/t recipe the tier can run
		// Optional: defaults to 128
		maxBaseEu,
		// The maximum amount of batches this tier can run in one cycle
		// Optional (if present, euCostMultiplier must also be present): defaults to 1
		multiplier,
		// The EU cost multiplier to apply
		// Optional: defaults to 1
		euCostMultiplier
	);
}

MITweaksMachineEvents.registerTieredMultiblocks((event) =>
{
	// Create your multiblock shapes
	const cupronickel = createPyrolyseTier(event, "pyrolyse_oven_cupronickel", "modern_industrialization:cupronickel_coil", 32, 1, 1);
	const kanthal = createPyrolyseTier(event, "pyrolyse_oven_kanthal", "modern_industrialization:kanthal_coil", 128, 10, 0.5);
	// Register the machine
	// You can also use `event.steamStandalone(...)` for a steam machine
	// If you want to make a machine that uses existing recipe types and don't want to make your own recipe
	//  categories, you can use `event.electric(...)` or `event.steam(...)` instead. When using these functions, you
	//  must exclude the REI params.
	event.electricStandalone(
		// English name, internal name
		"Pyrolyse Oven", "pyrolyse_oven",
		// The tiers for your machine
		(tiers) => tiers.add(cupronickel).add(kanthal),
		// REI progress bar
		event.progressBar(77, 33, "arrow"),
		// REI item inputs, item outputs, fluid inputs, fluid outputs
		(itemInputs) => itemInputs.addSlots(56, 35, 1, 2),
		(itemOutputs) => itemOutputs.addSlot(102, 35),
		(fluidInputs) => fluidInputs.addSlot(36, 35),
		(fluidOutputs) => fluidOutputs.addSlot(122, 35),
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"heatproof_machine_casing", "pyrolyse_oven", true, false, false
	);
});
```
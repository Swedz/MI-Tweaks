# Adding Batch Multiblocks

Adding batch multiblocks through KubeJS is very similar to how you add multiblocks using MI's KubeJS integration (see
[here](https://github.com/AztechMC/Modern-Industrialization/blob/-/docs/ADDING_MACHINES.md) for documentation). How
multiblock shapes are made won't be thoroughly covered here since that is covered in MI's documentation.

The main difference here is that you need to retrieve the recipe type and pass that to the creator, you can add
workstations to add this machine to, and you must provide the batch size and EU cost multiplier for this machine. An
EU cost multiplier of 1 means that the EU cost when using the batching machine is the same as the normal machine -
and then using a multiplier of 0.5 means that the EU cost will be halved when using the batching machine.

## Creating Machines

You can create both steam and electric batch crafting machines.

Steam batch machines can be defined like so:

```js
MITweaksMachineEvents.registerBatchMultiblocks(event =>
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
		// Recipe type to use, it must be an existing recipe type (it can be a custom recipe type)
		event.getRecipeType("modern_industrialization:cutting_machine"),
		// The workstations (in EMI/JEI/REI) to add this machine to
		// In some cases you may want to add it to multiple workstations (if there are bronze/steel tiers of this machine)
		(workstations) => workstations.add(
			"modern_industrialization:bronze_cutting_machine",
			"modern_industrialization:steel_cutting_machine"
		),
		// The multiblock shape
		shape,
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"modern_industrialization:bronze_plated_bricks", "cutting_machine", true, false, false,
		// Batch size, EU cost multiplier
		8, 0.75
	);
})
```

And then electric batch machines:

```js
MITweaksMachineEvents.registerBatchMultiblocks(event =>
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
		// Recipe type to use, it must be an existing recipe type (it can be a custom recipe type)
		event.getRecipeType("modern_industrialization:chemical_reactor"),
		// The workstations (in EMI/JEI/REI) to add this machine to
		// In some cases you may want to add it to multiple workstations (if there are bronze/steel tiers of this machine)
		(workstations) => workstations.add("modern_industrialization:chemical_reactor"),
		// The multiblock shape
		shape,
		// Casing of the controller, overlay folder, front overlay?, top overlay?, side overlay?
		"extended_industrialization:steel_plated_bricks", "chemical_reactor", true, false, false,
		// Batch size, EU cost multiplier
		16, 0.75
	);
})
```

## Machine models and language entries

MI Tweaks does not have a runtime datagen system like MI yet, so you have to make your item and block models and
language entries manually (sorry!). Below are all of the resource pack files you need to provide (easiest to add in
your `kubejs/assets` directory). Make sure to be extra mindful of the namespaces used where!

`assets/mi_tweaks/blockstates/<machine>.json`

```json
{
    "variants": {
        "": {
            "model": "mi_tweaks:block/<machine>"
        }
    }
}
```

`assets/<namespace>/lang/en_us.json` (your namespace can be anything for this)

```json
{
    "block.mi_tweaks.<machine>": "<machine name>"
}
```

`assets/mi_tweaks/models/block/<machine>.json`

```json
{
    "casing": "<casing>",
    "default_overlays": {
        "fluid_auto": "modern_industrialization:block/overlays/fluid_auto",
        "front": "mi_tweaks:block/machines/<machine>/overlay_front",
        "front_active": "mi_tweaks:block/machines/<machine>/overlay_front_active",
        "item_auto": "modern_industrialization:block/overlays/item_auto",
        "output": "modern_industrialization:block/overlays/output"
    },
    "loader": "modern_industrialization:machine"
}
```

`assets/mi_tweaks/models/item/<machine>.json`

```json
{
    "parent": "mi_tweaks:block/<machine>"
}
```

and then machine overlay textures in `assets/mi_tweaks/textures/block/machines/<machine>/...`
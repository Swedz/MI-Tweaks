# Recipe Conditions

There are some extra recipe conditions added to allow for further customization of progression.

Recipe process conditions can be included in any recipe type that is a `MachineRecipeType`. Recipe conditions can be
added to a recipe's json in an array with the key `process_conditions`.

## EBF Coil

This process condition allows you to define a coil required for the recipe. This is distinct from MI's builtin method of
doing this which uses EU/t cost, as it allows you to arbitrarily require a different tier than the EU/t cost.

```json
{
    "type": "mi_tweaks:ebf_coil",
    "coil": "modern_industrialization:kanthal_coil"
}
```

## Machine Tier

This process condition makes it so that the machine must be a certain machine tier in order to run the recipe. This
performs an exact equals check, so you can make recipes exclusive to bronze machines, for example. Below is a list of
acceptable machine tiers.

- `bronze`
- `steel`
- `singeblock_electric`
- `multiblock_electric`

```json
{
    "type": "mi_tweaks:machine_tier",
    "tier": "bronze"
}
```

## Open Water

This process condition allows you to require a machine to be in an area that is considered "open water". This is similar
to the system used by vanilla Minecraft's fishing system. For this condition, you must define a `relative` value and a
`range` value (1 -> 16). The `relative` value determines what blocks relative to the machine to be checked, and the
`range` value determines the cuboidal radius to check for water around the machine. The range used by vanilla
Minecraft's fishing hook is 2. Below is a list of acceptable relative values.

- `all`
- `at_and_below`
- `below`
- `at_and_above`
- `above`

Additionally, you may optionally set a `fill` value (0 -> 1) that determines the percentage of water blocks in the
specified area that are required to be water.

```json
{
    "type": "mi_tweaks:open_water",
    "relative": "below",
    "range": 2
}
```

## Nearby Entity

This process condition allows you to require a machine to have a specific entity type nearby it. The area checked by
this condition is determined identically to how the above "Open Water" condition works with the exception that it does
not have a `fill` field and instead uses a `count` field. The `count` field determines the amount of the given type of
entity is required *at minimum* to be within the specified range of the machine.

This will require that at least 1 sheep is within 10 blocks of the machine for it to run.

```json
{
    "type": "mi_tweaks:nearby_entity",
    "relative": "all",
    "range": 10,
    "entity": "minecraft:sheep",
    "count": 1
}
```

## Voltage

This process condition makes it so that the machine must have a certain hull (or higher) provided to it in order to run
the recipe.

When using this recipe condition, it is recommended to enable `display_machine_voltage` in the config.

```json
{
    "type": "mi_tweaks:voltage",
    "voltage": "hv"
}
```
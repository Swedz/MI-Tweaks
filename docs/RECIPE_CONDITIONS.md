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
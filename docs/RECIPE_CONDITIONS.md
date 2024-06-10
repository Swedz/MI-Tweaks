# Recipe Conditions
There are some new recipe conditions provided by MI Tweaks. Below is an outline of each.

Recipe process conditions can be included in any recipe type that is a `MachineRecipeType`. Recipe conditions can be added to a recipe's json in an array with the key `conditions`.

## EBF Coil
This process condition allows you to define a coil required for the recipe. This is distinct from MI's builtin method of doing this which uses EU/t cost, as it allows you to arbitrarily require a different tier than the EU/t cost.

```json
{
    "type": "mi_tweaks:ebf_coil",
    "coil": "modern_industrialization:kanthal_coil"
}
```

## Voltage
This process condition makes it so that the machine must have a certain hull (or higher) provided to it in order to run the recipe.

When using this recipe condition, it is recommended to enable `display_machine_voltage` in the config.

```json
{
    "type": "mi_tweaks:voltage",
    "voltage": "hv"
}
```
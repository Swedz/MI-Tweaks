# MI Tweaks
This mod is an addon for [Modern Industrialization](https://github.com/AztechMC/Modern-Industrialization) that adds some configurable tweaks to existing mechanics. This addon is designed specifically for modpack creators to further customize the MI experience in their pack. All tweaks are disabled by default.

## Extra Recipe Conditions
There are some extra recipe conditions added to allow for further customization of progression. For any information regarding these new recipe conditions, see the documentation [here](https://github.com/Swedz/MI-Tweaks/blob/master/docs/RECIPE_CONDITIONS.md).

## Voltage Integration
MI does not have much of a focus on voltage tiers of machines. By default, the voltage of a machine purely allows it to accept power from higher tiers of cables. MI Tweaks provides multiple options to change this (voltage recipe condition & voltage based machine speed).

For ease of use, there is a config option to have a machine display its voltage in its UI. The voltage of a machine is determined by the hull placed in its machine hull slot.

## Efficiency Behavior
MI Tweaks provides some configuration options for altering how efficiency in electric machines works. There are three different modes to choose from:

- **DISABLED** : No changes will be made to the efficiency behavior of machines
- **ALWAYS_MAX** : Machines will always run at max efficiency
- **USE_VOLTAGE** : Machines will run at a constant speed determined by its voltage

Not only can you change the behavior of efficiency, but there is also a config option for hiding the efficiency bar and information in machine UIs.

## Machine Blueprints
For the modpack creators who want to seamlessly implement a requirement to making machines other than the items themselves (for example, a research system of sorts), MI Tweaks provides Machine Blueprints.

The `machine_blueprints` config section provides quite a few options to customize how blueprints are required. By default, no machines require blueprints. The config provides options to prevent the placing of machines without the proper blueprint for said machine. There is *no* option to prevent crafting because there are too many ways to cover with this (there are blocks from mods and even vanilla in 1.21 that will craft items for you, which would not be able to be prevented without causing a mess).

A machine blueprint can be used to view the shape of multiblocks for the machine it is for when held similarly to how a wrench works by default.

## Other Tweaks
- Water Pumps require ocean/river biome
- Lock efficiency of crafting machines with redstone control module
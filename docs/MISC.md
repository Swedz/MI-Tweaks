# Miscellaneous Tweaks

MI Tweaks also provides a bunch of smaller options to further customize your gameplay. These are all simple enough that
they don't need a whole page to describe them. Of course, all of these features are disabled by default.

### Water Pumps require ocean/river biomes

When enabled, Water Pumps will only run while in a biome tagged `#minecraft:is_ocean` or `#minecraft:is_river`.

### Redstone Control Module locks efficiency

When enabled, electric machines that are turned off by a Redstone Control Module will not lose efficiency.

### Prevent Wrench from rendering multiblock shapes

This feature only makes sense to use when [Machine Blueprints](MACHINE_BLUEPRINTS.md) have been properly implemented.

### Include current energy consumption in the energy bar tooltip

This feature only makes sense to use when machine efficiency is hidden
(see [Efficiency Behavior](EFFICIENCY_BEHAVIOR.md) for more details).

### Water Explosive items

There exists a data map, `mi_tweaks:water_explosive` (located at
`data/mi_tweaks/data_maps/item/water_explosive.json`), that allows you to define what items will cause an explosion
upon touching water. This only works for item entities, not blocks placed in world. For an example of how to format
the data map file, see [here](examples/water_explosive.json).

The `strength` field determines the explosion strength - must be a value above 0 and less than or equal to 200. The
`fire` field is a boolean (true or false) - when true, fire will be placed as part of the explosion (like how beds in
the nether/end work).

### Very Hot items

There exists a tag, `#mi_tweaks:very_hot` (located at `data/mi_tweaks/tags/item/very_hot.json`) that items can be
added to. Whenever an item on this tag is in a player's inventory, it will set them on fire for a configurable
duration (see the config file). For an example of how to format the tag file, see [here](examples/very_hot.json).

There is also an attribute, `mi_tweaks:heat_protection` that can be added to items to grant the player protection
from this burning. If the player has any heat protection, they are immune to the burning of very hot items in their
inventory. Below is an example of how you could spawn in a stick that grants immunity to very hot items while in
your offhand. Note that the `id` field can be whatever resource location you want, but should be unique or else you
may get unexpected results (this is a vanilla thing).

```
/give @p stick[minecraft:attribute_modifiers=[{id:"example:heat_protection",type:"mi_tweaks:heat_protection",operation:"add_value",amount:1,slot:"offhand"}]]
```
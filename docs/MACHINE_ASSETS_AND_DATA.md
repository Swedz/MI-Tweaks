# Machine Asset Files

MI Tweaks does not have a runtime datagen system like MI at the moment, so you have to make your item and block models
and language entries manually (sorry!). Below are all of the resource pack files you need to provide (easiest to add in
your `kubejs` directory). Make sure to be extra mindful of the namespaces used!

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

`assets/<namespace>/lang/en_us.json` (the namespace can be anything for this)

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

# Machine Data Files

Similar to how you need to add models manually, you will need to add a loot table for your machine block yourself or
else it will not drop anything when broken. You also will need to add the block to the `#minecraft:mineable/pickaxe`
and `#minecraft:needs_stone_tool` tags (or whatever tags you want to use, but these are the ones used by all MI
machines).

`data/minecraft/tags/block/mineable/pickaxe.json`

```json
{
    "values": [
        "mi_tweaks:<machine>"
    ]
}
```

`data/minecraft/tags/block/needs_stone_tool.json`

```json
{
    "values": [
        "mi_tweaks:<machine>"
    ]
}
```

`data/mi_tweaks/loot_table/blocks/<machine>.json`

```json
{
    "type": "minecraft:block",
    "pools": [
        {
            "bonus_rolls": 0.0,
            "conditions": [
                {
                    "condition": "minecraft:survives_explosion"
                }
            ],
            "entries": [
                {
                    "type": "minecraft:item",
                    "name": "mi_tweaks:<machine>"
                }
            ],
            "rolls": 1.0
        }
    ],
    "random_sequence": "mi_tweaks:blocks/<machine>"
}
```
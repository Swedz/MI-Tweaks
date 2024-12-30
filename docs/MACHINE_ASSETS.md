# Machine Assets

MI Tweaks does not have a runtime datagen system like MI at the moment, so you have to make your item and block models
and language entries manually (sorry!). Below are all of the resource pack files you need to provide (easiest to add in
your `kubejs/assets` directory). Make sure to be extra mindful of the namespaces used!

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
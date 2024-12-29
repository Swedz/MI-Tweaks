# Flux Transformer

This block takes in EU (MI energy) of the voltage of the casing provided, stores it internally (204.8k EU by default),
and converts it to FE (Forge energy, common energy used by many other mods) at a configurable rate (1:1 by default) when
extracted. There is also a config option to limit the FE extract rate (2^63-1 by default).

Note that there are 2 separate recipes for the Flux Transformer. One is the standard recipe that uses a FE
Cable (`#c:fe_cables`) (recipe id is `mi_tweaks:flux_transformer`), and the other uses the Redstone Battery from MI in
its place when no items are in the FE Cables tag (recipe id is `mi_tweaks:flux_transformer_fallback`). For a list of
currently supported cables in the FE Cables tag, see [this file](../src/main/resources/data/c/tags/item/fe_cables.json).
If there are any cables that you think should be added to this tag, let me know and I can add them - or you can add them
yourself for your pack.
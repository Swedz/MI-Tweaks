# Machine Blueprints

For the modpack creators who want to seamlessly implement a requirement to making machines other than the items
themselves (for example, a research system of sorts), MI Tweaks provides Machine Blueprints. MI Tweaks does not provide
any method of obtaining blueprints in survival mode, so it is left entirely up to modpack creators to implement.

A machine blueprint can be used to view the shape of multiblocks for the machine it is for when held similarly to how a
wrench works by default. Additionally, blueprints can be used to build multiblocks. By right clicking a multiblock
controller with its respective blueprint, it will take the blocks from your inventory and place them for you.

The `machine_blueprints` config section provides quite a few options to customize how blueprints are required. By
default, no machines require blueprints. The config provides options to prevent the placing of machines without the
proper blueprint for said machine. There is *no* option to prevent crafting because there are too many ways to cover
with this (there are blocks from mods and even vanilla in 1.21 that will craft items for you, which would not be able to
be prevented without causing a mess).
# Efficiency Behavior

MI Tweaks provides some configuration options for altering how efficiency in electric machines works. There are three
different modes to choose from:

- `DISABLED` : No changes will be made to the efficiency behavior of machines
- `ALWAYS_BASE` : Machines will always run at the base machine EU or the base recipe EU if it's larger plus the EU
  provided by upgrades
- `ALWAYS_MAX` : Machines will always run at max efficiency
- `USE_VOLTAGE` : Machines will run at a constant speed determined by its voltage

Not only can you change the behavior of efficiency, but there is also a config option for hiding the efficiency bar and
information in machine UIs.
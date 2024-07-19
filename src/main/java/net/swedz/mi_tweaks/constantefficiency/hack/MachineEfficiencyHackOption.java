package net.swedz.mi_tweaks.constantefficiency.hack;

import java.util.function.Supplier;

public enum MachineEfficiencyHackOption
{
	DISABLED(DisabledMachineEfficiencyHack::new),
	ALWAYS_BASE(AlwaysBaseMachineEfficiencyHack::new),
	ALWAYS_MAX(AlwaysMaxMachineEfficiencyHack::new),
	USE_VOLTAGE(UseVoltageMachineEfficiencyHack::new);
	
	private final Supplier<MachineEfficiencyHack> creator;
	
	MachineEfficiencyHackOption(Supplier<MachineEfficiencyHack> creator)
	{
		this.creator = creator;
	}
	
	public MachineEfficiencyHack createInstance()
	{
		return creator.get();
	}
}

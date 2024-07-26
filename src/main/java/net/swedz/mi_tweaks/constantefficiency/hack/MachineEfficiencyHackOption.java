package net.swedz.mi_tweaks.constantefficiency.hack;

import java.util.function.Supplier;

public enum MachineEfficiencyHackOption
{
	DISABLED(DisabledMachineEfficiencyHack::new),
	ALWAYS_BASE(AlwaysBaseMachineEfficiencyHack::new),
	ALWAYS_MAX(AlwaysMaxMachineEfficiencyHack::new),
	USE_VOLTAGE(UseVoltageMachineEfficiencyHack::new);
	
	private final MachineEfficiencyHack instance;
	
	MachineEfficiencyHackOption(Supplier<MachineEfficiencyHack> creator)
	{
		this.instance = creator.get();
	}
	
	public MachineEfficiencyHack instance()
	{
		return instance;
	}
}

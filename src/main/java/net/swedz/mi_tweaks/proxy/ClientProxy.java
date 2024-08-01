package net.swedz.mi_tweaks.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class ClientProxy extends CommonProxy
{
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public Player getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}
}

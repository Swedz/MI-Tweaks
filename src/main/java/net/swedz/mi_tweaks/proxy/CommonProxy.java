package net.swedz.mi_tweaks.proxy;

import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;

public class CommonProxy
{
	public static final CommonProxy INSTANCE = init();
	
	private static CommonProxy init()
	{
		if(FMLEnvironment.dist.isClient())
		{
			try
			{
				Class<?> clientProxy = Class.forName("net.swedz.mi_tweaks.proxy.ClientProxy");
				return (CommonProxy) clientProxy.getConstructor().newInstance();
			}
			catch (Exception ex)
			{
				throw new RuntimeException("Failed to create MI Tweaks ClientProxy.", ex);
			}
		}
		else
		{
			return new ClientProxy();
		}
	}
	
	public boolean isClient()
	{
		return false;
	}
	
	public Player getClientPlayer()
	{
		throw new UnsupportedOperationException("Client player is not available on the server!");
	}
}

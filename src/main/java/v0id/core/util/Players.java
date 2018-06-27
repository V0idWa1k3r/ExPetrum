package v0id.core.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.core.VoidApi;

public class Players 
{
	public static String getClientPlayerUsername()
	{
		return VoidApi.proxy.getClientPlayerName();
	}
	
	public static EntityPlayerMP getPlayerByUsername(String uName)
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(uName);
	}
}

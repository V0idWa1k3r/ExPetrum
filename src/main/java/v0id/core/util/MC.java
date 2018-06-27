package v0id.core.util;

import javax.annotation.Nonnull;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MC 
{
	@Nonnull
	public static Side getSide()
	{
		return FMLCommonHandler.instance().getEffectiveSide();
	}
}

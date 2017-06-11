package v0id.exp.proxy;

import net.minecraft.nbt.NBTTagCompound;
import v0id.api.exp.fx.EnumParticle;

public class ExPProxyServer implements IExPProxy
{
	@Override
	public void handleSpecialAttackPacket(NBTTagCompound tag)
	{
		
	}

	@Override
	public void spawnParticle(EnumParticle particle, float[] positionMotion, float[] color, byte flags, int lifetime, float scale, short[] lmap)
	{

	}
}

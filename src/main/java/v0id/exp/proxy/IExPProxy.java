package v0id.exp.proxy;

import net.minecraft.nbt.NBTTagCompound;
import v0id.api.exp.fx.EnumParticle;
import v0id.exp.registry.ILifecycleListener;

public interface IExPProxy extends ILifecycleListener
{
	void handleSpecialAttackPacket(NBTTagCompound tag);

	void spawnParticle(EnumParticle particle, float[] positionMotion, float[] color, byte flags, int lifetime, float scale, short[] lmap);
}

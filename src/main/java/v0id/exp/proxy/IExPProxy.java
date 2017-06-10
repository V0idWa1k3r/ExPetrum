package v0id.exp.proxy;

import net.minecraft.nbt.NBTTagCompound;
import v0id.exp.registry.ILifecycleListener;

public interface IExPProxy extends ILifecycleListener
{
	void handleSpecialAttackPacket(NBTTagCompound tag);
}

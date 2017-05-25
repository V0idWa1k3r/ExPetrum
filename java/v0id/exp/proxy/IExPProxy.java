package v0id.exp.proxy;

import net.minecraft.nbt.NBTTagCompound;

public interface IExPProxy
{
	void handleSpecialAttackPacket(NBTTagCompound tag);
}

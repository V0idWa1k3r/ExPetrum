package v0id.exp.proxy;

import net.minecraft.nbt.NBTTagCompound;
import v0id.exp.combat.ClientCombatHandler;

public class ExPProxyClient implements IExPProxy
{
	@Override
	public void handleSpecialAttackPacket(NBTTagCompound tag)
	{
		ClientCombatHandler.handle(tag);
	}
}

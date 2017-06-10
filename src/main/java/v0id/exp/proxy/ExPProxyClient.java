package v0id.exp.proxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.exp.client.ClientRegistry;
import v0id.exp.combat.ClientCombatHandler;

public class ExPProxyClient implements IExPProxy
{
	@Override
	public void handleSpecialAttackPacket(NBTTagCompound tag)
	{
		ClientCombatHandler.handle(tag);
	}

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        ClientRegistry.instance = new ClientRegistry();
        ClientRegistry.instance.preInit(evt);
    }

    @Override
    public void init(FMLInitializationEvent evt)
    {
        ClientRegistry.instance.init(evt);
    }
}

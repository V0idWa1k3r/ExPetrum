package v0id.exp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.fx.EnumParticle;
import v0id.exp.client.ClientRegistry;
import v0id.exp.client.fx.ParticleEngine;
import v0id.exp.combat.ClientCombatHandler;

public class ExPProxyClient implements IExPProxy
{
    static
    {
        ExPMisc.defaultParticleEngineImpl = new ParticleEngine();
    }

	@Override
	public void handleSpecialAttackPacket(NBTTagCompound tag)
	{
		ClientCombatHandler.handle(tag);
	}

    @Override
    public void spawnParticle(EnumParticle particle, float[] positionMotion, float[] color, byte flags, int lifetime, float scale, short[] lmap)
    {
        ExPMisc.defaultParticleEngineImpl.spawnParticle(ExPMisc.defaultParticleEngineImpl.getFactoryFor(particle).createParticle(Minecraft.getMinecraft().world, positionMotion, color, flags, lifetime, scale, lmap));
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

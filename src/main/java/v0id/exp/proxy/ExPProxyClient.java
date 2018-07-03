package v0id.exp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.fx.EnumParticle;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.exp.client.fx.ParticleEngine;
import v0id.exp.client.registry.ClientRegistry;
import v0id.exp.client.render.hud.PlayerHUDRenderer;
import v0id.exp.combat.ClientCombatHandler;

public class ExPProxyClient implements IExPProxy
{
    static
    {
        ExPMisc.defaultParticleEngineImpl = new ParticleEngine();
        ClientRegistry.instance = new ClientRegistry();
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
    public void handleNewAge(EnumPlayerProgression age)
    {
        PlayerHUDRenderer.beginNewAge(age);
    }

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        ClientRegistry.instance.preInit(evt);
    }

    @Override
    public void init(FMLInitializationEvent evt)
    {
        ClientRegistry.instance.init(evt);
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt)
    {
        ClientRegistry.instance.postInit(evt);
    }
}

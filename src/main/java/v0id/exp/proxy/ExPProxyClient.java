package v0id.exp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.client.EnumParticle;
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
    public World getClientWorld()
    {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public IThreadListener getClientThreadListener()
    {
        return Minecraft.getMinecraft();
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public int getViewDistance()
    {
        return Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
    }

    @Override
    public int getGrassColor(IBlockAccess world, BlockPos pos)
    {
        return world.getBiome(pos).getGrassColorAtPos(pos);
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

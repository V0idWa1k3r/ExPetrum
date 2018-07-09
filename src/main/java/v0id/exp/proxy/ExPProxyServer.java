package v0id.exp.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import v0id.api.exp.client.EnumParticle;
import v0id.api.exp.player.EnumPlayerProgression;

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

	@Override
	public void handleNewAge(EnumPlayerProgression age)
	{
	}

	@Override
	public World getClientWorld()
	{
		return null;
	}

	@Override
	public IThreadListener getClientThreadListener()
	{
		return null;
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	@Override
	public int getViewDistance()
	{
		MinecraftServer server = DimensionManager.getWorld(0).getMinecraftServer();

		//Should always be true
		if (server instanceof DedicatedServer)
        {
            return ((DedicatedServer) server).getIntProperty("view-distance", 10);
        }

		return 10;
	}

    @Override
    public int getGrassColor(IBlockAccess world, BlockPos pos)
    {
        return -1;
    }
}

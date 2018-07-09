package v0id.exp.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import v0id.api.exp.client.EnumParticle;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.exp.registry.ILifecycleListener;

public interface IExPProxy extends ILifecycleListener
{
	void handleSpecialAttackPacket(NBTTagCompound tag);

	void spawnParticle(EnumParticle particle, float[] positionMotion, float[] color, byte flags, int lifetime, float scale, short[] lmap);

	void handleNewAge(EnumPlayerProgression age);

	World getClientWorld();

	IThreadListener getClientThreadListener();

	EntityPlayer getClientPlayer();

	int getViewDistance();

	int getGrassColor(IBlockAccess world, BlockPos pos);
}

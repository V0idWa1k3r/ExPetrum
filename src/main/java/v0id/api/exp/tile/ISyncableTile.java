package v0id.api.exp.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISyncableTile
{
    NBTTagCompound serializeData();

    void readData(NBTTagCompound tag);

    BlockPos getPos();

    World getWorld();
}

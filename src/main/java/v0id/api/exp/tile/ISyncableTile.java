package v0id.api.exp.tile;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncableTile
{
    NBTTagCompound serializeData();

    void readData(NBTTagCompound tag);
}

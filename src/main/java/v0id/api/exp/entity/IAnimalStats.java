package v0id.api.exp.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 */
public interface IAnimalStats extends INBTSerializable<NBTTagCompound>
{
    IAnimalStats mix(IAnimalStats other);
}

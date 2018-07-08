package v0id.api.exp.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRotaryHandler extends INBTSerializable<NBTTagCompound>
{
    float getSpeed();

    float getTorque();

    void setSpeed(float f);

    void setTorque(float f);
}

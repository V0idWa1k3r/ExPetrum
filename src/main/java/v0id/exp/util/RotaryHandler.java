package v0id.exp.util;

import net.minecraft.nbt.NBTTagCompound;
import v0id.api.exp.tile.IRotaryHandler;
import v0id.core.util.nbt.NBTChain;

public class RotaryHandler implements IRotaryHandler
{
    private float speed;
    private float torque;

    public RotaryHandler()
    {
    }

    @Override
    public float getSpeed()
    {
        return this.speed;
    }

    @Override
    public float getTorque()
    {
        return this.torque;
    }

    @Override
    public void setSpeed(float f)
    {
        this.speed = f;
    }

    @Override
    public void setTorque(float f)
    {
        this.torque = f;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return NBTChain.startChain().withFloat("speed", this.speed).withFloat("torque", this.torque).endChain();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.speed = nbt.getFloat("speed");
        this.torque = nbt.getFloat("torque");
    }

    public static RotaryHandler createDefault()
    {
        return new RotaryHandler();
    }
}

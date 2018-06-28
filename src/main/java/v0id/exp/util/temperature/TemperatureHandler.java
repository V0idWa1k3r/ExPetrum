package v0id.exp.util.temperature;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import v0id.api.exp.tile.ITemperatureHandler;

import javax.naming.OperationNotSupportedException;

public class TemperatureHandler implements ITemperatureHandler
{
    public final float maxTemperature;
    public float currentTemperature;

    public TemperatureHandler(float maxTemperature)
    {
        this.maxTemperature = maxTemperature;
    }

    public static TemperatureHandler createDefault()
    {
        return new TemperatureHandler(0);
    }

    public float clampTemperature(float temp)
    {
        return Math.max(0, Math.min(temp, this.maxTemperature));
    }

    @Override
    public float getCurrentTemperature()
    {
        return this.currentTemperature;
    }

    @Override
    public boolean setTemperature(float value, boolean simulate)
    {
        float tNew = this.clampTemperature(value);
        if (!simulate)
        {
            this.currentTemperature = tNew;
        }

        return true;
    }

    @Override
    public boolean incrementTemperature(float value, boolean simulate)
    {
        return this.setTemperature(this.getCurrentTemperature() + value, simulate);
    }

    @Override
    public float getMaxTemperature()
    {
        return this.maxTemperature;
    }

    @Override
    public void setMaxTemperature() throws OperationNotSupportedException
    {
        throw new OperationNotSupportedException();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setFloat("temp", this.currentTemperature);
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("temp", Constants.NBT.TAG_FLOAT))
        {
            this.currentTemperature = nbt.getFloat("temp");
        }
    }
}

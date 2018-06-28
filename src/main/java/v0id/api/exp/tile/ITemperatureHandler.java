package v0id.api.exp.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;

import javax.naming.OperationNotSupportedException;

public interface ITemperatureHandler extends INBTSerializable<NBTTagCompound>
{
    float getCurrentTemperature();

    boolean setTemperature(float value, boolean simulate);

    boolean incrementTemperature(float value, boolean simulate);

    float getMaxTemperature();

    void setMaxTemperature() throws OperationNotSupportedException;

    static ITemperatureHandler of(TileEntity tile)
    {
        return tile.getCapability(ExPTemperatureCapability.cap, null);
    }
}

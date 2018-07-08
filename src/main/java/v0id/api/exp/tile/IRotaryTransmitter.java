package v0id.api.exp.tile;

import net.minecraft.util.EnumFacing;

public interface IRotaryTransmitter
{
    void step(EnumFacing from, float speed, float torque, int length);
}

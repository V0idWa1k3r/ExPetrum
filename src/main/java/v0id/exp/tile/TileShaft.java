package v0id.exp.tile;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import v0id.api.exp.block.EnumShaftMaterial;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.api.exp.tile.IRotaryHandler;
import v0id.api.exp.tile.IRotaryTransmitter;
import v0id.api.exp.world.IExPWorld;

public class TileShaft extends TileEntity implements IRotaryTransmitter
{
    public EnumShaftMaterial material;
    public long lastStepped;

    @Override
    public void step(EnumFacing from, float speed, float torque, int length)
    {
        if (speed * torque > material.getMaxPower())
        {
            this.world.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, false);
            this.world.setBlockToAir(this.pos);
        }
        else
        {
            lastStepped = IExPWorld.of(this.world).today().getTime();
            EnumFacing to = from.getOpposite();
            BlockPos at = this.pos.offset(to);
            TileEntity tile = this.world.getTileEntity(at);
            if (tile != null)
            {
                IRotaryHandler cap = tile.getCapability(ExPRotaryCapability.cap, from);
                if (cap != null)
                {
                    cap.setSpeed(speed);
                    cap.setTorque(torque);
                }
                else
                {
                    if (length + 1 < this.material.getMaxLength() && tile instanceof IRotaryTransmitter)
                    {
                        EnumFacing.Axis axis = this.world.getBlockState(at).getValue(BlockRotatedPillar.AXIS);
                        if (axis == this.world.getBlockState(this.pos).getValue(BlockRotatedPillar.AXIS))
                        {
                            ((IRotaryTransmitter) tile).step(from, speed, torque, length + 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.material = EnumShaftMaterial.values()[compound.getByte("material")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setByte("material", (byte) this.material.ordinal());
        return ret;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.serializeNBT());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.serializeNBT();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.deserializeNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.deserializeNBT(tag);
    }

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
}

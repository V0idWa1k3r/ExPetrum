package v0id.exp.tile;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.api.exp.tile.IRotaryTransmitter;
import v0id.api.exp.tile.ISyncableTile;
import v0id.exp.net.ExPNetwork;
import v0id.exp.util.RotaryHandler;

import javax.annotation.Nullable;

public class TileGearbox extends TileEntity implements ITickable, ISyncableTile
{
    public EnumFacing input = EnumFacing.SOUTH;
    public RotaryHandler rotaryHandler = new RotaryHandler();

    public void sendUpdatePacket()
    {
        if (this.world != null && !this.world.isRemote)
        {
            ExPNetwork.sendTileData(this, true);
        }
    }

    @Override
    public void update()
    {
        if (!this.world.isBlockPowered(this.pos))
        {
            EnumFacing out = this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
            BlockPos at = this.pos.offset(out);
            if (this.world.getTileEntity(at) instanceof IRotaryTransmitter)
            {
                if (this.rotaryHandler.getSpeed() != 0 && this.rotaryHandler.getTorque() != 0)
                {
                    EnumFacing.Axis axis = this.world.getBlockState(at).getValue(BlockRotatedPillar.AXIS);
                    if (axis == out.getAxis())
                    {
                        ((IRotaryTransmitter) this.world.getTileEntity(at)).step(out.getOpposite(), this.rotaryHandler.getSpeed(), this.rotaryHandler.getTorque(), 0);
                    }
                }
            }
        }

        this.rotaryHandler.setSpeed(0);
        this.rotaryHandler.setTorque(0);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.input = EnumFacing.values()[compound.getByte("input")];
        this.rotaryHandler.deserializeNBT(compound.getCompoundTag("rotaryHandler"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setByte("input", (byte) this.input.ordinal());
        compound.setTag("rotaryHandler", this.rotaryHandler.serializeNBT());
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
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap ? facing == this.input : super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap && facing == this.input ? ExPRotaryCapability.cap.cast(this.rotaryHandler) : super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound serializeData()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setByte("input", (byte) this.input.ordinal());
        return ret;
    }

    @Override
    public void readData(NBTTagCompound tag)
    {
        this.input = EnumFacing.values()[tag.getByte("input")];
    }
}

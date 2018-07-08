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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.api.exp.tile.IRotaryTransmitter;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.util.RotaryHandler;

import javax.annotation.Nullable;

public class TileSplitterGearbox extends TileEntity implements ITickable
{
    public EnumFacing output0 = EnumFacing.WEST;
    public EnumFacing output1 = EnumFacing.EAST;
    public RotaryHandler rotaryHandler = new RotaryHandler();

    public void sendUpdatePacket()
    {
        if (this.world != null && !this.world.isRemote)
        {
            NBTTagCompound sent = new NBTTagCompound();
            sent.setTag("tileData", this.serializeNBT());
            sent.setTag("blockPosData", new DimBlockPos(this.getPos(), this.getWorld().provider.getDimension()).serializeNBT());
            VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64));
        }
    }

    @Override
    public void update()
    {
        EnumFacing[] faces = new EnumFacing[]{ output0, output1 };
        for (EnumFacing face : faces)
        {
            BlockPos at = this.pos.offset(face);
            if (this.world.getTileEntity(at) instanceof IRotaryTransmitter)
            {
                if (this.rotaryHandler.getSpeed() != 0 && this.rotaryHandler.getTorque() != 0)
                {
                    EnumFacing.Axis axis = this.world.getBlockState(at).getValue(BlockRotatedPillar.AXIS);
                    if (axis == face.getAxis())
                    {
                        ((IRotaryTransmitter) this.world.getTileEntity(at)).step(face.getOpposite(), this.rotaryHandler.getSpeed() / 2, this.rotaryHandler.getTorque() / 2, 0);

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
        this.output0 = EnumFacing.values()[compound.getByte("out_0")];
        this.output1 = EnumFacing.values()[compound.getByte("out_1")];
        this.rotaryHandler.deserializeNBT(compound.getCompoundTag("rotaryHandler"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setByte("out_0", (byte) this.output0.ordinal());
        ret.setByte("out_1", (byte) this.output1.ordinal());
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

    public EnumFacing getInput()
    {
        return this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap ? facing == this.getInput() : super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap && facing == this.getInput() ? ExPRotaryCapability.cap.cast(this.rotaryHandler) : super.getCapability(capability, facing);
    }
}

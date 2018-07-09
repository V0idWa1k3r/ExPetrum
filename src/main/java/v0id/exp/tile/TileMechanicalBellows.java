package v0id.exp.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.api.exp.tile.ITemperatureHolder;
import v0id.exp.util.RotaryHandler;

import javax.annotation.Nullable;

public class TileMechanicalBellows extends TileEntity implements ITickable
{
    public float progress;
    public float progressLast;
    public RotaryHandler rotaryHandler = new RotaryHandler();

    @Override
    public void update()
    {
        this.progressLast = this.progress;
        this.progress += this.rotaryHandler.getSpeed() / 8192F + this.rotaryHandler.getTorque() / 32768F;
        this.rotaryHandler.setSpeed(0);
        this.rotaryHandler.setTorque(0);
        if (this.progressLast < 1 && this.progress >= 1)
        {
            this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERDRAGON_FLAP, SoundCategory.BLOCKS, 1.0F, 0.1F);
            EnumFacing rotation = this.getFacing();
            BlockPos pos = this.pos.offset(rotation);
            this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERDRAGON_FLAP, SoundCategory.BLOCKS, 1.0F, 0.1F);
            if (this.world.getTileEntity(pos) instanceof ITemperatureHolder)
            {
                ((ITemperatureHolder)this.world.getTileEntity(pos)).acceptBellows(rotation.getOpposite(), true);
            }
        }

        if (this.progress >= 2)
        {
            this.progress = 0;
            this.progressLast = 0;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.progress = compound.getFloat("progress");
        this.progressLast = compound.getFloat("progressLast");
        this.rotaryHandler.deserializeNBT(compound.getCompoundTag("rotaryHandler"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setFloat("progress", this.progress);
        ret.setFloat("progressLast", this.progressLast);
        ret.setTag("rotaryHandler", this.rotaryHandler.serializeNBT());
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

    public EnumFacing getFacing()
    {
        return this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return (capability == ExPRotaryCapability.cap && facing == this.getFacing().getOpposite()) || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap && facing == this.getFacing().getOpposite() ? ExPRotaryCapability.cap.cast(this.rotaryHandler) : super.getCapability(capability, facing);
    }
}

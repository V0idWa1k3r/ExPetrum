package v0id.exp.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.item.IContainerTickable;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TilePotteryStation extends TileEntity implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(2)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            if (!TilePotteryStation.this.world.isRemote)
            {
                VoidNetwork.sendDataToAllAround(PacketType.TileData, this.serializeNBT(), new NetworkRegistry.TargetPoint(TilePotteryStation.this.world.provider.getDimension(), TilePotteryStation.this.pos.getX(), TilePotteryStation.this.pos.getY(), TilePotteryStation.this.pos.getZ(), 64));
            }
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return 8;
        }
    };

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack is = this.inventory.getStackInSlot(i);
            TemperatureUtils.tickItem(is, true);
            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }
        }
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
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        return ret;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : super.getCapability(capability, facing);
    }
}

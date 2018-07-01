package v0id.exp.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.item.IContainerTickable;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileCrate extends TileEntity implements ITickable
{
    public EnumTreeType type = EnumTreeType.KALOPANAX;
    public EnumFacing direction = EnumFacing.NORTH;
    public float lidAngle;
    public float prevLidAngle;
    public int currentPlayers;
    public ItemStackHandler inventory = new ItemStackHandler(9);

    @Override
    public void update()
    {
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack is = inventory.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof IContainerTickable)
                {
                    ((IContainerTickable) is.getItem()).onContainerTick(is, this.getWorld(), this.getPos(), this);
                }

                TemperatureUtils.tickItem(is, true);
            }
        }

        this.prevLidAngle = this.lidAngle;
        if (this.currentPlayers > 0)
        {
            this.lidAngle += 0.1F;
        }
        else
        {
            this.lidAngle -= 0.1F;
        }

        this.lidAngle = Math.max(0, Math.min(1, this.lidAngle));
    }

    public void openContainer()
    {
        ++this.currentPlayers;
    }

    public void closeContainer()
    {
        --this.currentPlayers;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.type = EnumTreeType.values()[compound.getByte("type")];
        this.direction = EnumFacing.values()[compound.getByte("facing")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setByte("type", (byte) this.type.ordinal());
        ret.setByte("facing", (byte) this.direction.ordinal());
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
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : super.getCapability(capability, facing);
    }
}

package v0id.exp.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.world.IExPWorld;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.item.ItemGeneric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSpinningWheel extends TileEntity
{
    public long lastActivated;
    public float progress;
    public ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return 1;
        }
    };

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

    public void click(EntityPlayer clicker)
    {
        ItemStack is = clicker.getHeldItem(EnumHand.MAIN_HAND);
        if (this.inventory.getStackInSlot(0).isEmpty())
        {
            if (is.getItem() instanceof ItemGeneric && is.getMetadata() == ItemGeneric.EnumGenericType.CARDED_WOOL.ordinal())
            {
                if (!this.world.isRemote)
                {
                    this.inventory.setStackInSlot(0, is.copy());
                    this.inventory.getStackInSlot(0).setCount(1);
                    is.shrink(1);
                    this.sendUpdatePacket();
                }
            }
        }
        else
        {
            if (this.inventory.getStackInSlot(0).getItem() instanceof ItemGeneric && this.inventory.getStackInSlot(0).getMetadata() == ItemGeneric.EnumGenericType.CARDED_WOOL.ordinal())
            {
                if (IExPWorld.of(this.getWorld()).today().getTime() - this.lastActivated >= 40)
                {
                    this.lastActivated = IExPWorld.of(this.getWorld()).today().getTime();
                    this.progress += 0.1F;
                    if (this.progress >= 1.0F)
                    {
                        this.inventory.setStackInSlot(0, new ItemStack(ExPItems.generic, 2, ItemGeneric.EnumGenericType.WOOL_YARN.ordinal()));
                        this.progress = 0;
                    }

                    if (!this.world.isRemote)
                    {
                        this.sendUpdatePacket();
                    }
                }
            }
            else
            {
                if (!this.inventory.getStackInSlot(0).isEmpty() && !this.world.isRemote)
                {
                    InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.inventory.getStackInSlot(0).copy());
                    this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    this.sendUpdatePacket();
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.lastActivated = compound.getLong("lastActivated");
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.progress = compound.getFloat("progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setLong("lastActivated", this.lastActivated);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setFloat("progress", this.progress);
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

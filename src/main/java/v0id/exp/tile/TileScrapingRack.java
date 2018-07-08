package v0id.exp.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.item.IKnife;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.item.ItemGeneric;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileScrapingRack extends TileEntity implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(1);
    public float progress;
    public int progressTimer;

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

    public boolean handleClick(EntityPlayer clicker)
    {
        ItemStack is = clicker.getHeldItem(EnumHand.MAIN_HAND);
        if (this.inventory.getStackInSlot(0).isEmpty())
        {
            if (is.getItem() instanceof ItemGeneric && is.getMetadata() == ItemGeneric.EnumGenericType.SOAKED_HIDE.ordinal())
            {
                if (!this.world.isRemote)
                {
                    this.inventory.setStackInSlot(0, is.copy());
                    this.inventory.getStackInSlot(0).setCount(1);
                    is.shrink(1);
                    this.sendUpdatePacket();
                }

                this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (is.getItem() instanceof IKnife && this.inventory.getStackInSlot(0).getMetadata() == ItemGeneric.EnumGenericType.SOAKED_HIDE.ordinal())
            {
                if (this.progressTimer <= 0)
                {
                    if (!this.world.isRemote)
                    {
                        this.progressTimer = 20;
                        this.progress += 0.1F;
                        if (this.progress >= 1.0F)
                        {
                            this.inventory.setStackInSlot(0, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.PREPARED_HIDE.ordinal()));
                            this.progress = 0;
                        }

                        is.damageItem(1, clicker);
                        this.sendUpdatePacket();
                    }

                    this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_LEASHKNOT_PLACE, SoundCategory.BLOCKS, 1, 1);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (!this.world.isRemote)
                {
                    InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.inventory.getStackInSlot(0));
                    this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    this.sendUpdatePacket();
                }

                return true;
            }
        }
    }

    @Override
    public void update()
    {
        --this.progressTimer;
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
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.progress = compound.getFloat("progress");
        this.progressTimer = compound.getInteger("timer");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setFloat("progress", this.progress);
        ret.setInteger("timer", this.progressTimer);
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

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
}

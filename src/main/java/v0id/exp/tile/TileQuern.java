package v0id.exp.tile;

import net.minecraft.inventory.InventoryHelper;
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
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesQuern;
import v0id.api.exp.tile.ISyncableTile;
import v0id.exp.net.ExPNetwork;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileQuern extends TileEntity implements ITickable, ISyncableTile
{
    public ItemStackHandler inventory = new ItemStackHandler(3)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            if (slot == 0)
            {
                TileQuern.this.sendUpdatePacket();
            }
        }
    };

    public int rotationIndex;

    public void sendUpdatePacket()
    {
        if (this.world != null && !this.world.isRemote)
        {
            ExPNetwork.sendTileData(this, false);
        }
    }

    @Override
    public void update()
    {
        if (this.rotationIndex > 0)
        {
            --this.rotationIndex;
        }

        if (this.world.isRemote)
        {
            return;
        }

        if (this.rotationIndex == 1)
        {
            RecipesQuern.IRecipeQuern recipe = RecipesQuern.getRecipe(this.inventory.getStackInSlot(1));
            if (recipe != null)
            {
                ItemStack out = recipe.getOut(this.inventory.getStackInSlot(1));
                this.inventory.getStackInSlot(1).shrink(1);
                if (this.inventory.getStackInSlot(2).isItemEqual(out) && this.inventory.getStackInSlot(2).getCount() <= this.inventory.getStackInSlot(2).getMaxStackSize() + out.getCount())
                {
                    this.inventory.getStackInSlot(2).grow(out.getCount());
                }
                else
                {
                    if (this.inventory.getStackInSlot(2).isEmpty())
                    {
                        this.inventory.setStackInSlot(2, out.copy());
                    }
                    else
                    {
                        InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), out.copy());
                    }
                }
            }
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
        this.rotationIndex = compound.getByte("rotationIndex");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setByte("rotationIndex", (byte)this.rotationIndex);
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

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }

    @Override
    public NBTTagCompound serializeData()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setByte("rotationIndex", (byte)this.rotationIndex);
        return ret;
    }

    @Override
    public void readData(NBTTagCompound tag)
    {
        this.inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        this.rotationIndex = tag.getByte("rotationIndex");
    }
}

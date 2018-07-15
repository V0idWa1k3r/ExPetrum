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
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.exp.util.RotaryHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileMechanicalQuern extends TileEntity implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(2);
    public int rotationIndex;
    public float progress;
    public RotaryHandler rotaryHandler = new RotaryHandler();

    @Override
    public void update()
    {
        if (this.rotaryHandler.getSpeed() > 0 || this.rotaryHandler.getTorque() > 0)
        {
            ++this.rotationIndex;
        }

        if (this.rotaryHandler.getTorque() > 32)
        {
            this.progress += this.rotaryHandler.getTorque() / 4096F + this.rotaryHandler.getSpeed() / 8192F;
        }

        this.rotaryHandler.setSpeed(0);
        this.rotaryHandler.setTorque(0);
        if (this.world.isRemote)
        {
            return;
        }

        if (this.progress >= 1)
        {
            RecipesQuern.IRecipeQuern recipe = RecipesQuern.getRecipe(this.inventory.getStackInSlot(0));
            if (recipe != null)
            {
                ItemStack out = recipe.getOut(this.inventory.getStackInSlot(0));
                this.inventory.getStackInSlot(0).shrink(1);
                if (this.inventory.getStackInSlot(1).isItemEqual(out) && this.inventory.getStackInSlot(1).getCount() <= this.inventory.getStackInSlot(1).getMaxStackSize() + out.getCount())
                {
                    this.inventory.getStackInSlot(1).grow(out.getCount());
                }
                else
                {
                    if (this.inventory.getStackInSlot(1).isEmpty())
                    {
                        this.inventory.setStackInSlot(1, out.copy());
                    }
                    else
                    {
                        InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), out.copy());
                    }
                }
            }

            this.progress = 0;
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
        this.rotaryHandler.deserializeNBT(compound.getCompoundTag("rotaryHandler"));
        this.rotationIndex = compound.getByte("rotationIndex");
        this.progress = compound.getFloat("progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setByte("rotationIndex", (byte)this.rotationIndex);
        ret.setTag("rotaryHandler", this.rotaryHandler.serializeNBT());
        ret.setFloat("progress", this.progress);
        return ret;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || (capability == ExPRotaryCapability.cap && facing == EnumFacing.UP);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : capability == ExPRotaryCapability.cap && facing == EnumFacing.UP ? ExPRotaryCapability.cap.cast(this.rotaryHandler) : super.getCapability(capability, facing);
    }

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
}

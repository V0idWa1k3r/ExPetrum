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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesPottery;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.exp.util.OreDictManager;
import v0id.exp.util.RotaryHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileMechanicalPotteryStation extends TileEntity implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(2);
    public RotaryHandler rotaryHandler = new RotaryHandler();
    public float progress;
    public int selectedRecipe;

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

        RecipesPottery.RecipePottery recipe = RecipesPottery.allRecipes.get(this.selectedRecipe);
        ItemStack is = recipe.getItem();
        if (this.inventory.getStackInSlot(1).isEmpty() || (ItemHandlerHelper.canItemStacksStack(this.inventory.getStackInSlot(1), is) && this.inventory.getStackInSlot(1).getCount() + is.getCount() <= this.inventory.getStackInSlot(1).getMaxStackSize()))
        {
            if (!this.inventory.getStackInSlot(0).isEmpty() && ArrayUtils.contains(OreDictManager.getOreNames(this.inventory.getStackInSlot(0)), "clay") && this.inventory.getStackInSlot(0).getCount() >= 4)
            {
                if (this.rotaryHandler.getSpeed() >= 32 && this.rotaryHandler.getTorque() >= 16)
                {
                    this.progress += this.rotaryHandler.getSpeed() / 8192 + this.rotaryHandler.getTorque() / 65536;
                    this.rotaryHandler.setTorque(0);
                    this.rotaryHandler.setSpeed(0);
                    if (this.progress >= 1)
                    {
                        this.progress = 0;
                        if (this.inventory.getStackInSlot(1).isEmpty())
                        {
                            this.inventory.setStackInSlot(1, is.copy());
                        }
                        else
                        {
                            this.inventory.getStackInSlot(1).grow(is.getCount());
                        }

                        this.inventory.getStackInSlot(0).shrink(4);
                    }
                }
            }
        }
        else
        {
            this.progress = 0;
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
        this.progress = compound.getFloat("progress");
        this.selectedRecipe = compound.getInteger("selectedRecipe");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setTag("rotaryHandler", this.rotaryHandler.serializeNBT());
        ret.setFloat("progress", this.progress);
        ret.setInteger("selectedRecipe", this.selectedRecipe);
        return ret;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == ExPRotaryCapability.cap;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : capability == ExPRotaryCapability.cap && facing == EnumFacing.DOWN ? ExPRotaryCapability.cap.cast(this.rotaryHandler) : super.getCapability(capability, facing);
    }
}

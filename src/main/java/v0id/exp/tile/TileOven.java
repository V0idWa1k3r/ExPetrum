package v0id.exp.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.api.exp.tile.ExPTemperatureCapability;
import v0id.api.exp.tile.ISyncableTile;
import v0id.exp.item.ItemFood;
import v0id.exp.net.ExPNetwork;
import v0id.exp.util.Helpers;
import v0id.exp.util.temperature.TemperatureHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileOven extends TileEntity implements ITickable, ISyncableTile
{
    public ItemStackHandler inventory = new ItemStackHandler(7);
    public FluidTank fluidTank = new FluidTank(1000);
    public TemperatureHandler temperature_handler = new TemperatureHandler(500);
    public int burnTimeLeft = 0;
    public int maxBurnTime = 0;
    public boolean litUp = false;

    public void sendUpdatePacket()
    {
        if (this.world != null && !this.world.isRemote)
        {
            ExPNetwork.sendTileData(this, false);
        }
    }

    public boolean tryLitUp()
    {
        if (!this.litUp && !this.inventory.getStackInSlot(6).isEmpty() && TileEntityFurnace.getItemBurnTime(this.inventory.getStackInSlot(6)) > 0)
        {
            this.litUp = true;
            ExPNetwork.sendTileData(this, true);
            return true;
        }

        return false;
    }

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
            TemperatureUtils.tickItem(is, TemperatureUtils.getTemperature(is) > this.temperature_handler.getCurrentTemperature());
            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }

            if (i == 0)
            {
                if (Helpers.tryConsumeFluidItem(is, this.fluidTank, stack ->
                {
                    if (this.inventory.getStackInSlot(1).isEmpty())
                    {
                        this.inventory.setStackInSlot(1, stack);
                        return true;
                    }
                    else
                    {
                        if (ItemHandlerHelper.canItemStacksStack(stack, this.inventory.getStackInSlot(1)) && this.inventory.getStackInSlot(1).getCount() + stack.getCount() <= this.inventory.getStackInSlot(1).getMaxStackSize())
                        {
                            this.inventory.getStackInSlot(1).grow(stack.getCount());
                            return true;
                        }
                    }

                    return false;
                }, stack ->
                {
                    this.inventory.getStackInSlot(0).shrink(1);
                    return true;
                }))
                {
                    this.sendUpdatePacket();
                }
            }
            else
            {
                if (i >= 2 && i <= 5)
                {
                    if (TemperatureUtils.getTemperature(is) < this.temperature_handler.getCurrentTemperature())
                    {
                        TemperatureUtils.incrementTemperature(is, is.getItem() instanceof ItemFood ? 1.0F : 0.5F);
                        Integer lambdaCapture = i;
                        RecipesSmelting.checkForSmelting(stack -> this.inventory.setStackInSlot(lambdaCapture, stack), is, false, true);
                        if (this.inventory.getStackInSlot(i) != is)
                        {
                            is = this.inventory.getStackInSlot(i);
                            if (is.getItem() instanceof ItemFood)
                            {
                                if (this.fluidTank.getFluid().getFluid() == ExPFluids.oliveOil || this.fluidTank.getFluid().getFluid() == ExPFluids.walnutOil)
                                {
                                    if (this.fluidTank.getFluid().amount >= 50)
                                    {
                                        this.fluidTank.drain(50, true);
                                        ItemFood food = (ItemFood) is.getItem();
                                        food.setPreservationType(is, (byte)2);
                                        food.setItemRotMultiplier(is, 0.35F);
                                        this.sendUpdatePacket();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (--this.burnTimeLeft <= 0)
        {
            if (this.litUp)
            {
                ItemStack is = this.inventory.getStackInSlot(6);
                int burnTime = TileEntityFurnace.getItemBurnTime(is);
                if (burnTime > 0)
                {
                    this.burnTimeLeft = burnTime;
                    this.maxBurnTime = burnTime;
                    is.shrink(1);
                }
            }
        }

        if (this.temperature_handler.getCurrentTemperature() <= 200 && this.burnTimeLeft <= 0 && this.litUp)
        {
            this.litUp = false;
            ExPNetwork.sendTileData(this, true);
        }

        this.temperature_handler.incrementTemperature(this.burnTimeLeft > 0 ? 0.5F : -0.5F, false);
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
        this.burnTimeLeft = compound.getInteger("burn");
        this.maxBurnTime = compound.getInteger("maxBurn");
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.fluidTank.readFromNBT(compound.getCompoundTag("fluidTank"));
        this.temperature_handler.deserializeNBT(compound.getCompoundTag("temp"));
        this.litUp = compound.getBoolean("lit");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setInteger("burn", this.burnTimeLeft);
        ret.setInteger("maxBurn", this.maxBurnTime);
        ret.setTag("inventory", this.inventory.serializeNBT());
        NBTTagCompound fluidTankNBT = new NBTTagCompound();
        fluidTank.writeToNBT(fluidTankNBT);
        ret.setTag("fluidTank", fluidTankNBT);
        ret.setTag("temp", this.temperature_handler.serializeNBT());
        ret.setBoolean("lit", this.litUp);
        return ret;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPTemperatureCapability.cap || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPTemperatureCapability.cap ? ExPTemperatureCapability.cap.cast(this.temperature_handler) : capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.fluidTank) : super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound serializeData()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setBoolean("lit", this.litUp);
        NBTTagCompound fluidTankNBT = new NBTTagCompound();
        fluidTank.writeToNBT(fluidTankNBT);
        ret.setTag("fluidTank", fluidTankNBT);
        return ret;
    }

    @Override
    public void readData(NBTTagCompound tag)
    {
        this.litUp = tag.getBoolean("lit");
        this.fluidTank.readFromNBT(tag.getCompoundTag("fluidTank"));
    }
}

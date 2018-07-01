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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.api.exp.tile.ExPTemperatureCapability;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.util.temperature.TemperatureHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TileCampfire extends TileEntity implements ITickable
{
    public ItemStackHandler inventory_wood = new ItemStackHandler(3);
    public ItemStackHandler inventory_thing = new ItemStackHandler(1);

    public TemperatureHandler temperature_handler = new TemperatureHandler(610);
    public int burnTimeLeft = 0;
    public int maxBurnTime = 0;
    public boolean litUp = false;

    @Override
    public void markDirty()
    {
        super.markDirty();
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
        if (this.world.isRemote)
        {
            return;
        }

        for (ItemStack is : this.getAllItems())
        {
            TemperatureUtils.tickItem(is, is != this.inventory_thing.getStackInSlot(0));
            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }
        }

        if (!this.inventory_thing.getStackInSlot(0).isEmpty())
        {
            ItemStack is = this.inventory_thing.getStackInSlot(0);
            float t = TemperatureUtils.getTemperature(is);
            TemperatureUtils.tickItem(is, this.temperature_handler.getCurrentTemperature() < t);
            if (this.temperature_handler.getCurrentTemperature() >= t)
            {
                TemperatureUtils.incrementTemperature(is, 0.5F);
                RecipesSmelting.checkForSmelting(i -> this.inventory_thing.setStackInSlot(0, i), is, false, true);
            }
        }

        int prevBurnTime = this.burnTimeLeft;
        if (--this.burnTimeLeft <= 0)
        {
            if (this.litUp)
            {
                for (int i = 0; i < this.inventory_wood.getSlots(); ++i)
                {
                    ItemStack is = this.inventory_wood.getStackInSlot(i);
                    int burnTime = TileEntityFurnace.getItemBurnTime(is);
                    if (burnTime > 0)
                    {
                        this.burnTimeLeft = burnTime;
                        this.maxBurnTime = burnTime;
                        is.shrink(1);
                        break;
                    }
                }
            }
        }

        if (this.temperature_handler.getCurrentTemperature() <= 200 && this.burnTimeLeft <= 0 && this.litUp)
        {
            this.litUp = false;
            this.markDirty();
        }

        if ((prevBurnTime <= 0 && this.burnTimeLeft > 0) || (prevBurnTime > 0 && this.burnTimeLeft == 0))
        {
            VoidNetwork.sendDataToAllAround(PacketType.TileData, this.serializeNBT(), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64));
        }

        this.temperature_handler.incrementTemperature(this.burnTimeLeft > 0 ? 0.5F : -0.5F, false);
    }

    public Iterable<ItemStack> getAllItems()
    {
        ItemStack[] allItems = new ItemStack[this.inventory_thing.getSlots() + this.inventory_wood.getSlots()];
        int i = 0;
        for (;i < this.inventory_wood.getSlots(); ++i)
        {
            allItems[i] = this.inventory_wood.getStackInSlot(i);
        }

        for (;i < allItems.length; ++i)
        {
            allItems[i] = this.inventory_thing.getStackInSlot(i - this.inventory_wood.getSlots());
        }

        return Arrays.asList(allItems);
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
        this.inventory_wood.deserializeNBT(compound.getCompoundTag("wood"));
        this.inventory_thing.deserializeNBT(compound.getCompoundTag("thing"));
        this.temperature_handler.deserializeNBT(compound.getCompoundTag("temp"));
        this.litUp = compound.getBoolean("lit");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setInteger("burn", this.burnTimeLeft);
        ret.setInteger("maxBurn", this.maxBurnTime);
        ret.setTag("wood", this.inventory_wood.serializeNBT());
        ret.setTag("thing", this.inventory_thing.serializeNBT());
        ret.setTag("temp", this.temperature_handler.serializeNBT());
        ret.setBoolean("lit", this.litUp);
        return ret;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPTemperatureCapability.cap || (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing));
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPTemperatureCapability.cap ? ExPTemperatureCapability.cap.cast(this.temperature_handler) : capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? facing == EnumFacing.DOWN || facing == EnumFacing.UP ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory_thing) : CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory_wood) : super.getCapability(capability, facing);
    }
}

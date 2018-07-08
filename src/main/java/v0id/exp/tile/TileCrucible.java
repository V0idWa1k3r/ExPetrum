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
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.item.IMold;
import v0id.api.exp.metal.AlloyHelper;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.api.exp.tile.ExPTemperatureCapability;
import v0id.api.exp.tile.ITemperatureHandler;
import v0id.api.exp.tile.ITemperatureHolder;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.util.temperature.TemperatureHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class TileCrucible extends TileEntity implements ITickable, ITemperatureHolder
{
    public Map<EnumMetal, Float> metalMap = new EnumMap<>(EnumMetal.class);
    public ItemStackHandler inventory = new ItemStackHandler(2)
    {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return slot == 1 ? 1 : super.getStackLimit(slot, stack);
        }
    };

    public TemperatureHandler temperature_handler = new TemperatureHandler(10000);

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
            TemperatureUtils.tickItem(is, TemperatureUtils.getTemperature(is) >= this.temperature_handler.getCurrentTemperature());
            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }

            if (i == 0)
            {
                boolean doSmeltingRecipe = true;
                TemperatureUtils.incrementTemperature(is, TemperatureUtils.getTemperature(is) < this.temperature_handler.getCurrentTemperature() ? this.metalMap.isEmpty() ? 0.5F : 2.0F : 0);
                if (is.getItem() instanceof IMeltableMetal)
                {
                    if (TemperatureUtils.getTemperature(is) >= ((IMeltableMetal) is.getItem()).getMeltingTemperature(is))
                    {
                        float amt = ((IMeltableMetal) is.getItem()).getMetalAmound(is);
                        EnumMetal metal = ((IMeltableMetal) is.getItem()).getMetal(is);
                        if (this.metalMap.values().stream().mapToInt(f -> f.intValue()).sum() + amt <= 6400)
                        {
                            if (this.metalMap.containsKey(metal))
                            {
                                this.metalMap.put(metal, this.metalMap.get(metal) + amt);
                            }
                            else
                            {
                                this.metalMap.put(metal, amt);
                            }

                            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
                            this.sendUpdatePacket();
                            doSmeltingRecipe = false;
                        }
                    }
                }

                if (doSmeltingRecipe)
                {
                    RecipesSmelting.checkForSmelting(stack -> this.inventory.setStackInSlot(0, stack), is, false, true);
                }
            }
            else
            {
                int overallMetal = this.metalMap.values().stream().mapToInt(f -> f.intValue()).sum();
                if (is.getItem() instanceof IMold && !this.metalMap.isEmpty())
                {
                    EnumMetal alloy = this.getCurrentAlloy();
                    if (alloy == null)
                    {
                        alloy = this.metalMap.size() == 1 ? this.metalMap.keySet().stream().findFirst().get() : EnumMetal.ANY;
                    }

                    boolean result = ((IMold)is.getItem()).tryFill(is, (metal, value) ->
                    {
                        if (this.metalMap.containsKey(metal))
                        {
                            this.metalMap.put(metal, this.metalMap.get(metal) - value);
                            if (this.metalMap.get(metal) <= 0)
                            {
                                this.metalMap.remove(metal);
                            }
                        }
                        else
                        {
                            if (metal.getComposition() != null)
                            {
                                for (Pair<EnumMetal, Pair<Float, Float>> compositionData : metal.getComposition().compositionData)
                                {
                                    float currentPercentage = this.metalMap.get(compositionData.getLeft()) / overallMetal;
                                    this.metalMap.put(compositionData.getLeft(), this.metalMap.get(compositionData.getLeft()) - currentPercentage * value);
                                    if (this.metalMap.get(compositionData.getLeft()) <= 0)
                                    {
                                        this.metalMap.remove(compositionData.getLeft());
                                    }
                                }
                            }
                            else
                            {
                                throw new UnsupportedOperationException("Impossible exception thrown! Metal that is being poured into a mold is not present in the crucible but it has no alloy composition!");
                            }
                        }

                    }, s ->
                    {
                        this.inventory.setStackInSlot(1, s);
                        TemperatureUtils.setTemperature(s, this.temperature_handler.getCurrentTemperature());
                    }, alloy, overallMetal);

                    if (result)
                    {
                        this.sendUpdatePacket();
                    }
                }
            }
        }

        float addedT = -0.5F;
        TileEntity tile = this.world.getTileEntity(this.pos.down());
        {
            if (tile != null)
            {
                ITemperatureHandler handler = tile.getCapability(ExPTemperatureCapability.cap, EnumFacing.UP);
                if (handler != null)
                {
                    if (handler.getCurrentTemperature() > this.temperature_handler.getCurrentTemperature())
                    {
                        addedT = 0.5F;
                    }
                    else
                    {
                        if (handler.getCurrentTemperature() >= this.temperature_handler.getCurrentTemperature() - 1F)
                        {
                            addedT = 0;
                        }
                    }
                }
            }
        }

        this.temperature_handler.incrementTemperature(addedT, false);
        this.markDirty();
    }

    public EnumMetal getCurrentAlloy()
    {
        return AlloyHelper.getAlloy(this.metalMap.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue().intValue())).toArray(Pair[]::new));
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
        this.temperature_handler.deserializeNBT(compound.getCompoundTag("temp"));
        this.metalMap.clear();
        for (int i = 0; i < compound.getByte("metals"); ++i)
        {
            NBTTagCompound tag = compound.getCompoundTag("metal_" + i);
            this.metalMap.put(EnumMetal.values()[tag.getByte("k")], tag.getFloat("v"));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setTag("temp", this.temperature_handler.serializeNBT());
        compound.setByte("metals", (byte) this.metalMap.size());
        int i = 0;
        for (Map.Entry<EnumMetal, Float> data : this.metalMap.entrySet())
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("k", (byte) data.getKey().ordinal());
            tag.setFloat("v", data.getValue());
            compound.setTag("metal_" + i++, tag);
        }

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
        return capability == ExPTemperatureCapability.cap ? ExPTemperatureCapability.cap.cast(this.temperature_handler) : capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : super.getCapability(capability, facing);
    }

    @Override
    public void acceptBellows(EnumFacing side, boolean b)
    {
        if (this.world.getTileEntity(this.pos.down()) instanceof ITemperatureHolder)
        {
            ((ITemperatureHolder)this.world.getTileEntity(this.pos.down())).acceptBellows(side, b);
        }
    }
}

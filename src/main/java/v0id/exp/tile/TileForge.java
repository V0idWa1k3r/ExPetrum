package v0id.exp.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.block.property.EnumKaolinType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.api.exp.tile.ExPTemperatureCapability;
import v0id.api.exp.tile.ISyncableTile;
import v0id.api.exp.tile.ITemperatureHolder;
import v0id.exp.net.ExPNetwork;
import v0id.exp.util.temperature.TemperatureHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class TileForge extends TileEntity implements ITickable, ITemperatureHolder, ISyncableTile
{
    public boolean isLit;
    public int burnTimeLeft;
    public int maxBurnTime;
    public boolean isStructureCorrect;
    public int structureCheckCounter;
    public float additionalMaxT;
    public float bellowsAdditionalT;
    public boolean firstTick = true;
    public ItemStackHandler inventory = new ItemStackHandler(10);
    public TemperatureHandler temperature_handler = new TemperatureHandler(1000)
    {
        @Override
        public float getMaxTemperature()
        {
            return super.getMaxTemperature() + TileForge.this.additionalMaxT + TileForge.this.bellowsAdditionalT;
        }
    };

    public void sendUpdatePacket()
    {
        if (this.world != null && !this.world.isRemote)
        {
            ExPNetwork.sendTileData(this, true);
        }
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        if (this.firstTick || (++this.structureCheckCounter) >= 200)
        {
            this.isStructureCorrect = this.checkStructure();
            this.firstTick = false;
            this.additionalMaxT = 0;
            this.checkAdditionalTRecursive(this.pos.up(3), 0);
        }

        if (!this.isStructureCorrect)
        {
            this.additionalMaxT = -1000;
        }

        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack is = this.inventory.getStackInSlot(i);
            TemperatureUtils.tickItem(is, i >= 5 || TemperatureUtils.getTemperature(is) >= this.temperature_handler.getCurrentTemperature());
            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }

            if (i < 5)
            {
                float t = TemperatureUtils.getTemperature(is);
                if (this.temperature_handler.getCurrentTemperature() >= t)
                {
                    TemperatureUtils.incrementTemperature(is, 0.5F);
                    AtomicInteger lambdaCapture = new AtomicInteger(i);
                    RecipesSmelting.checkForSmelting(stack -> this.inventory.setStackInSlot(lambdaCapture.get(), stack), is, false, true);
                }
            }
        }

        if (--this.burnTimeLeft <= 0)
        {
            if (this.isLit)
            {
                for (int i = 5; i < this.inventory.getSlots(); ++i)
                {
                    ItemStack is = this.inventory.getStackInSlot(i);
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

        this.bellowsAdditionalT = Math.max(0, this.bellowsAdditionalT - 0.2F);
        if (this.temperature_handler.getCurrentTemperature() <= 200 && this.burnTimeLeft <= 0 && this.isLit)
        {
            this.isLit = false;
            this.sendUpdatePacket();
        }

        this.temperature_handler.incrementTemperature(this.burnTimeLeft > 0 ? 0.5F : -0.5F, false);
        this.markDirty();
    }

    public void checkAdditionalTRecursive(BlockPos at, int iteration)
    {
        if (iteration == 10)
        {
            return;
        }

        at = at.up();
        if (this.world.isAirBlock(at) && this.world.getBlockState(at.south()).isOpaqueCube() && this.world.getBlockState(at.north()).isOpaqueCube() && this.world.getBlockState(at.east()).isOpaqueCube() && this.world.getBlockState(at.west()).isOpaqueCube())
        {
            this.additionalMaxT += 25;
            this.checkAdditionalTRecursive(at, ++iteration);
        }
    }

    public boolean checkStructure()
    {
        if (this.checkBlock(this.pos.north()) && this.checkBlock(this.pos.south()) && this.checkBlock(this.pos.west()) && this.checkBlock(this.pos.east()) && this.checkBlock(this.pos.north().west()) && this.checkBlock(this.pos.north().east()) && this.checkBlock(this.pos.south().west()) && this.checkBlock(this.pos.south().east()))
        {
            return this.checkSide(EnumFacing.NORTH) || this.checkSide(EnumFacing.EAST) || this.checkSide(EnumFacing.SOUTH) || this.checkSide(EnumFacing.WEST);
        }

        return false;
    }

    public boolean checkSide(EnumFacing facing)
    {
        EnumFacing left = facing.rotateYCCW();
        EnumFacing right = facing.rotateY();
        BlockPos at = this.pos.up().offset(facing);
        return this.checkBlock(at) && this.checkBlock(at.offset(left)) && this.checkBlock(at.offset(right)) && this.checkBlock(at.up().offset(left)) && this.checkBlock(at.up().offset(right)) && this.checkBlock(at.up().up());
    }

    public boolean checkBlock(BlockPos at)
    {
        IBlockState state = this.world.getBlockState(at);
        return state.getBlock() == ExPBlocks.kaolin && state.getValue(ExPBlockProperties.KAOLIN_TYPE) == EnumKaolinType.FIRE_BRICK;
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
        this.temperature_handler.deserializeNBT(compound.getCompoundTag("temp"));
        this.isLit = compound.getBoolean("lit");
        this.bellowsAdditionalT = compound.getFloat("bellowsT");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setInteger("burn", this.burnTimeLeft);
        ret.setInteger("maxBurn", this.maxBurnTime);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setTag("temp", this.temperature_handler.serializeNBT());
        ret.setBoolean("lit", this.isLit);
        ret.setFloat("bellowsT", this.bellowsAdditionalT);
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
        this.bellowsAdditionalT = Math.min(b ? 750 : 500, this.bellowsAdditionalT + (b ? 250 : 100));
    }

    @Override
    public NBTTagCompound serializeData()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setBoolean("lit", this.isLit);
        return ret;
    }

    @Override
    public void readData(NBTTagCompound tag)
    {
        this.isLit = tag.getBoolean("lit");
    }
}

package v0id.exp.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.block.EnumMoltenMetalState;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.property.EnumKaolinType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.item.ITuyere;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.block.BlockMoltenMetal;
import v0id.exp.item.ItemGeneric;
import v0id.exp.item.ItemOre;
import v0id.exp.util.temperature.TemperatureHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;

public class TileBloomery extends TileEntity implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(4);
    public TemperatureHandler temperatureHandler = new TemperatureHandler(1500);
    public int resultStackSize;
    public int work;
    public boolean isStructureCorrect;
    public int structureCheckTimer;

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
        if (!this.world.isRemote)
        {
            if (this.structureCheckTimer++ % 200 == 0)
            {
                this.isStructureCorrect = this.checkStructure();
            }

            ItemStack tuyere = this.inventory.getStackInSlot(3);
            if (!tuyere.isEmpty())
            {
                TemperatureUtils.tickItem(tuyere, TemperatureUtils.getTemperature(tuyere) > this.temperatureHandler.getCurrentTemperature());
                if (TemperatureUtils.getTemperature(tuyere) < this.temperatureHandler.getCurrentTemperature() - 0.5F)
                {
                    TemperatureUtils.incrementTemperature(tuyere, 0.5F);
                }
            }

            if (this.work > 0)
            {
                EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite();
                if (this.world.getBlockState(this.pos.offset(facing)) != ExPBlocks.moltenMetal.getDefaultState())
                {
                    this.world.setBlockState(this.pos.offset(facing), ExPBlocks.moltenMetal.getDefaultState());
                }

                if (this.world.getBlockState(this.pos.offset(facing).up()) != ExPBlocks.moltenMetal.getDefaultState())
                {
                    this.world.setBlockState(this.pos.offset(facing).up(), ExPBlocks.moltenMetal.getDefaultState());
                }

                if (this.world.getBlockState(this.pos.offset(facing).up(2)) != ExPBlocks.moltenMetal.getDefaultState())
                {
                    this.world.setBlockState(this.pos.offset(facing).up(2), ExPBlocks.moltenMetal.getDefaultState());
                }

                this.temperatureHandler.incrementTemperature(0.5F, false);
                if (tuyere.isEmpty() || !(tuyere.getItem() instanceof ITuyere) || !this.isStructureCorrect)
                {
                    this.work = 0;
                    if (this.world.getBlockState(this.pos.offset(facing)).getBlock() == ExPBlocks.moltenMetal)
                    {
                        this.world.setBlockState(this.pos.offset(facing), Blocks.AIR.getDefaultState(), 2);
                    }

                    if (this.world.getBlockState(this.pos.offset(facing).up()).getBlock() == ExPBlocks.moltenMetal)
                    {
                        this.world.setBlockState(this.pos.offset(facing).up(), Blocks.AIR.getDefaultState(), 2);
                    }

                    if (this.world.getBlockState(this.pos.offset(facing).up(2)).getBlock() == ExPBlocks.moltenMetal)
                    {
                        this.world.setBlockState(this.pos.offset(facing).up(2), Blocks.AIR.getDefaultState(), 2);
                    }
                }
                else
                {
                    if (TemperatureUtils.getTemperature(tuyere) >= ((ITuyere) tuyere.getItem()).getMeltingTemperature(tuyere))
                    {
                        if (this.world.rand.nextFloat() <= 0.025F)
                        {
                            tuyere.setItemDamage(tuyere.getItemDamage() + 1);
                            if (tuyere.getItemDamage() >= tuyere.getMaxDamage())
                            {
                                tuyere.shrink(1);
                            }
                        }
                    }

                    if (--this.work <= 0)
                    {
                        if (this.world.getBlockState(this.pos.offset(facing)).getBlock() == ExPBlocks.moltenMetal)
                        {
                            this.world.setBlockState(this.pos.offset(facing), Blocks.AIR.getDefaultState(), 2);
                        }

                        if (this.world.getBlockState(this.pos.offset(facing).up()).getBlock() == ExPBlocks.moltenMetal)
                        {
                            this.world.setBlockState(this.pos.offset(facing).up(), Blocks.AIR.getDefaultState(), 2);
                        }

                        if (this.world.getBlockState(this.pos.offset(facing).up(2)).getBlock() == ExPBlocks.moltenMetal)
                        {
                            this.world.setBlockState(this.pos.offset(facing).up(2), Blocks.AIR.getDefaultState(), 2);
                        }

                        this.inventory.setStackInSlot(2, new ItemStack(ExPItems.generic, this.resultStackSize, ItemGeneric.EnumGenericType.IRON_BLOOM.ordinal()));
                        TemperatureUtils.setTemperature(this.inventory.getStackInSlot(2), this.temperatureHandler.getCurrentTemperature());
                        this.resultStackSize = 0;
                        this.sendUpdatePacket();
                    }
                }
            }
            else
            {
                this.temperatureHandler.incrementTemperature(-0.5F, false);
            }
        }
    }

    public boolean initiateWork()
    {
        if (!this.world.isRemote)
        {
            ItemStack ore = this.inventory.getStackInSlot(0);
            ItemStack coal = this.inventory.getStackInSlot(1);
            ItemStack tuyere = this.inventory.getStackInSlot(3);
            if (this.inventory.getStackInSlot(2).isEmpty())
            {
                if (ore.getItem() instanceof ItemOre && (ore.getMetadata() == EnumOre.HEMATITE.ordinal() || ore.getMetadata() == EnumOre.MAGNETITE.ordinal() || ore.getMetadata() == EnumOre.PENTLANDITE.ordinal()))
                {
                    if (isItemCharcoal(coal) && tuyere.getItem() instanceof ITuyere)
                    {
                        if (this.work <= 0 && this.isStructureCorrect)
                        {
                            int lesserStack = Math.min(ore.getCount(), coal.getCount());
                            int val = lesserStack - (lesserStack % 10);
                            if (val >= 10)
                            {
                                this.resultStackSize = val / 10;
                                ore.shrink(val);
                                coal.shrink(val);
                                this.work = 12000;
                                EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite();
                                this.world.setBlockState(this.pos.offset(facing), ExPBlocks.moltenMetal.getDefaultState());
                                this.world.setBlockState(this.pos.offset(facing).up(), ExPBlocks.moltenMetal.getDefaultState());
                                this.world.setBlockState(this.pos.offset(facing).up(2), ExPBlocks.moltenMetal.getDefaultState());
                                this.sendUpdatePacket();
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean checkStructure()
    {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite();
        for (int x = -1; x <= 1; ++x)
        {
            for (int y = -1; y <= 3; ++y)
            {
                for (int z = -1; z <= 1; ++z)
                {
                    BlockPos at = this.pos.offset(facing).add(x, y, z);
                    if (at.getX() == this.pos.getX() && at.getY() == this.pos.getY() && at.getZ() == this.pos.getZ())
                    {
                        continue;
                    }

                    if (x != 0 && z != 0)
                    {
                        if (!this.checkBlock(at))
                        {
                            return false;
                        }
                    }
                    else
                    {
                        if (y == -1)
                        {
                            if (!this.checkBlock(at))
                            {
                                return false;
                            }
                        }
                        else
                        {
                            if (x == 0 && z == 0)
                            {
                                if (!this.world.isAirBlock(at))
                                {
                                    IBlockState state = this.world.getBlockState(at);
                                    if (!(state.getBlock() instanceof BlockMoltenMetal && state.getValue(ExPBlockProperties.MOLTEN_METAL_STATE) == EnumMoltenMetalState.NORMAL))
                                    {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean checkBlock(BlockPos at)
    {
        IBlockState state = this.world.getBlockState(at);
        return state.getBlock() == ExPBlocks.kaolin && state.getValue(ExPBlockProperties.KAOLIN_TYPE) == EnumKaolinType.FIRE_BRICK;
    }

    public static boolean isItemCharcoal(ItemStack is)
    {
        return is.getItem() instanceof ItemGeneric && (is.getMetadata() == ItemGeneric.EnumGenericType.CHARCOAL.ordinal() || is.getMetadata() == ItemGeneric.EnumGenericType.ANTHRACITE.ordinal());
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
        this.work = compound.getInteger("work");
        this.resultStackSize = compound.getInteger("resultStackSize");
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.temperatureHandler.deserializeNBT(compound.getCompoundTag("temperatureHandler"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setInteger("work", this.work);
        ret.setInteger("resultStackSize", this.resultStackSize);
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setTag("temperatureHandler", this.temperatureHandler.serializeNBT());
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
}

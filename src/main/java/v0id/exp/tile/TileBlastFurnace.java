package v0id.exp.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.block.EnumMoltenMetalState;
import v0id.api.exp.block.property.EnumKaolinType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.item.ITuyere;
import v0id.api.exp.recipe.RecipesBlastFurnace;
import v0id.api.exp.tile.ExPTemperatureCapability;
import v0id.api.exp.tile.ISyncableTile;
import v0id.api.exp.tile.ITemperatureHolder;
import v0id.exp.block.BlockBlastFurnaceMetal;
import v0id.exp.block.BlockCrucible;
import v0id.exp.block.BlockKaolin;
import v0id.exp.block.BlockMoltenMetal;
import v0id.exp.net.ExPNetwork;
import v0id.exp.util.temperature.TemperatureHandler;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;
import java.util.List;

public class TileBlastFurnace extends TileEntity implements ITickable, ISyncableTile, ITemperatureHolder
{
    public int recipe = -1;
    public int work;
    public int maxWork;
    public int recipeCheck;
    public int structureCheck;
    public boolean isStructureCorrect;
    public int structureLayers;
    public int recipeAmount;
    public TemperatureHandler temperatureHandler = new TemperatureHandler(1000)
    {
        @Override
        public float getMaxTemperature()
        {
            return super.getMaxTemperature() + TileBlastFurnace.this.additionalTFromBellows;
        }
    };

    public float additionalTFromBellows;
    public ItemStackHandler inventory = new ItemStackHandler(2);

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.recipe = tag.getByte("recipe");
        this.work = tag.getInteger("work");
        this.maxWork = tag.getInteger("maxWork");
        this.temperatureHandler.deserializeNBT(tag.getCompoundTag("temperatureHandler"));
        this.inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        this.recipeAmount = tag.getInteger("recipeAmount");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setByte("recipe", (byte) this.recipe);
        ret.setInteger("work", this.work);
        ret.setInteger("maxWork", this.maxWork);
        ret.setTag("temperatureHandler", this.temperatureHandler.serializeNBT());
        ret.setTag("inventory", this.inventory.serializeNBT());
        ret.setInteger("recipeAmount", this.recipeAmount);
        return ret;
    }

    @Override
    public NBTTagCompound serializeData()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setInteger("work", this.work);
        ret.setByte("recipe", (byte) this.recipe);
        ret.setInteger("recipeAmount", this.recipeAmount);
        return ret;
    }

    @Override
    public void readData(NBTTagCompound tag)
    {
        this.work = tag.getInteger("work");
        this.recipe = tag.getByte("recipe");
        this.recipeAmount = tag.getInteger("recipeAmount");
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
    public void update()
    {
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack is = inventory.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof IContainerTickable)
                {
                    ((IContainerTickable) is.getItem()).onContainerTick(is, this.getWorld(), this.getPos(), this);
                }

                TemperatureUtils.tickItem(is, TemperatureUtils.getTemperature(is) > this.temperatureHandler.getCurrentTemperature());
            }
        }

        if (++this.recipeCheck % 20 == 0)
        {
            this.checkRecipe();
        }

        if (this.structureCheck++ % 200 == 0)
        {
            this.checkStructure();
        }

        if (!this.world.isRemote)
        {
            if (this.work > 0)
            {
                ItemStack t0 = this.inventory.getStackInSlot(0);
                ItemStack t1 = this.inventory.getStackInSlot(1);
                if (!this.isStructureCorrect || t0.isEmpty() || t1.isEmpty() || !(t0.getItem() instanceof ITuyere) || !(t1.getItem() instanceof ITuyere))
                {
                    this.work = 0;
                    BlockPos offset = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite()).up(this.structureLayers - 1);
                    for (int i = 0; i < this.structureLayers; ++i)
                    {
                        BlockPos at = offset.down(i);
                        this.world.setBlockToAir(at);
                    }
                }
                else
                {
                    this.temperatureHandler.incrementTemperature(0.5F, false);
                    RecipesBlastFurnace.IBlastFurnaceRecipe rec = RecipesBlastFurnace.allRecipes.get(this.recipe);
                    if (this.temperatureHandler.getCurrentTemperature() >= rec.getTemperature())
                    {
                        if (--this.work <= 0)
                        {
                            this.recipe = -1;
                            TileCrucible crucibleTile = (TileCrucible) this.world.getTileEntity(this.pos.down());
                            if (crucibleTile.metalMap.containsKey(rec.getResult()))
                            {
                                crucibleTile.metalMap.put(rec.getResult(), crucibleTile.metalMap.get(rec.getResult()) + rec.getResultAmount() * this.recipeAmount);
                            }
                            else
                            {
                                crucibleTile.metalMap.put(rec.getResult(), rec.getResultAmount() * this.recipeAmount);
                            }

                            BlockPos offset = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite()).up(this.structureLayers - 1);
                            for (int i = 0; i < this.structureLayers; ++i)
                            {
                                BlockPos at = offset.down(i);
                                this.world.setBlockToAir(at);
                            }

                            this.recipeAmount = 0;
                            ExPNetwork.sendTileData(crucibleTile, true);
                            ExPNetwork.sendTileData(this, true);
                        }
                    }

                    BlockPos offset = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite()).up(this.structureLayers - 1);
                    for (int i = 0; i < this.structureLayers; ++i)
                    {
                        BlockPos at = offset.down(i);
                        IBlockState state = this.world.getBlockState(at);
                        if (state.getBlock() instanceof BlockMoltenMetal && state.getValue(ExPBlockProperties.MOLTEN_METAL_STATE) != EnumMoltenMetalState.NORMAL)
                        {
                            this.world.setBlockState(at, state.withProperty(ExPBlockProperties.MOLTEN_METAL_STATE, EnumMoltenMetalState.NORMAL));
                        }
                    }

                    for (ItemStack tuyere : new ItemStack[]{ t0, t1 })
                    {
                        if (TemperatureUtils.getTemperature(tuyere) < this.temperatureHandler.getCurrentTemperature())
                        {
                            TemperatureUtils.incrementTemperature(tuyere, 0.5F);
                        }

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
        if (this.isStructureCorrect && this.work <= 0 && this.recipe != -1)
        {
            ItemStack t0 = this.inventory.getStackInSlot(0);
            ItemStack t1 = this.inventory.getStackInSlot(1);
            if (!t0.isEmpty() && !t1.isEmpty() && t0.getItem() instanceof ITuyere && t1.getItem() instanceof ITuyere)
            {
                BlockPos offset = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite()).up(this.structureLayers - 1);
                int recipeValue = 0;
                for (int i = 0; i < this.structureLayers; ++i)
                {
                    BlockPos at = offset.down(i);
                    IBlockState state = this.world.getBlockState(at);
                    if (state.getBlock() instanceof BlockBlastFurnaceMetal)
                    {
                        recipeValue += state.getValue(BlockSnow.LAYERS);
                        if (!this.world.isRemote)
                        {
                            this.world.setBlockState(at, ExPBlocks.moltenMetal.getDefaultState(), 2);
                        }
                    }
                }

                RecipesBlastFurnace.IBlastFurnaceRecipe rec = RecipesBlastFurnace.allRecipes.get(this.recipe);
                this.work = rec.getWorkRequired();
                this.maxWork = this.work;
                this.recipeAmount = recipeValue;
                ExPNetwork.sendTileData(this, true);
                return true;
            }
        }

        return false;
    }

    public void checkStructure()
    {
        this.isStructureCorrect = false;
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite();
        BlockPos offset = this.pos.offset(facing);
        for (int x = -1; x <= 1; ++x)
        {
            for (int z = -1; z <= 1; ++z)
            {
                BlockPos at = offset.add(x, -1, z);
                if (at.getZ() == this.pos.getZ() && at.getX() == this.pos.getX())
                {
                    if (!(world.getBlockState(at).getBlock() instanceof BlockCrucible))
                    {
                        return;
                    }
                }
                else
                {
                    if (!this.checkBlock(at))
                    {
                        return;
                    }
                }
            }
        }

        if (!this.checkBlock(offset.offset(facing)))
        {
            return;
        }

        if (!this.checkBlock(offset.offset(facing.rotateY())))
        {
            return;
        }

        if (!this.checkBlock(offset.offset(facing.rotateYCCW())))
        {
            return;
        }

        this.isStructureCorrect = true;
        this.structureLayers = 1;
        BlockPos up = offset.up();
        while (true)
        {
            for (int x = -1; x <= 1; ++x)
            {
                for (int z = -1; z <= 1; ++z)
                {
                    BlockPos at = up.add(x, 0, z);
                    if (x != 0 || z != 0)
                    {
                        if (!this.checkBlock(at))
                        {
                            return;
                        }
                    }
                    else
                    {
                        if (!this.checkMiddleBlock(at))
                        {
                            return;
                        }
                    }
                }
            }

            up = up.up();
            ++this.structureLayers;
        }
    }

    public void checkRecipe()
    {
        if (this.isStructureCorrect && this.work <= 0)
        {
            BlockPos from = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite());
            List<EntityItem> items = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(from, from.add(1, this.structureLayers, 1)));
            if (this.recipe == -1)
            {
                int i = 0;
                for (RecipesBlastFurnace.IBlastFurnaceRecipe rec : RecipesBlastFurnace.allRecipes)
                {
                    if (rec.matches(items))
                    {
                        this.recipe = i;
                        rec.consumeItems(items);
                        this.addRecipe();
                        ExPNetwork.sendTileData(this, false);
                        break;
                    }

                    ++i;
                }
            }
            else
            {
                RecipesBlastFurnace.IBlastFurnaceRecipe rec = RecipesBlastFurnace.allRecipes.get(this.recipe);
                if (rec.matches(items))
                {
                    rec.consumeItems(items);
                    this.addRecipe();
                    ExPNetwork.sendTileData(this, false);
                }
            }
        }
    }

    public void addRecipe()
    {
        if (!this.world.isRemote)
        {
            BlockPos offset = this.pos.offset(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getOpposite()).up(this.structureLayers - 1);
            while (offset.getY() >= this.pos.getY())
            {
                if (this.world.isAirBlock(offset))
                {
                    offset = offset.down();
                }
                else
                {
                    IBlockState state = this.world.getBlockState(offset);
                    if (state.getBlock() instanceof BlockBlastFurnaceMetal)
                    {
                        int layers = state.getValue(BlockSnow.LAYERS);
                        if (layers >= 8)
                        {
                            offset = offset.up();
                            this.world.setBlockState(offset, ExPBlocks.blastFurnaceMetal.getDefaultState().withProperty(BlockSnow.LAYERS, 1), 2);
                            return;
                        }
                        else
                        {
                            this.world.setBlockState(offset, state.cycleProperty(BlockSnow.LAYERS));
                            return;
                        }
                    }
                }
            }

            offset = offset.up();
            this.world.setBlockState(offset, ExPBlocks.blastFurnaceMetal.getDefaultState().withProperty(BlockSnow.LAYERS, 1), 2);
        }
    }

    public boolean checkBlock(BlockPos at)
    {
        return this.world.getBlockState(at).getBlock() instanceof BlockKaolin && this.world.getBlockState(at).getValue(ExPBlockProperties.KAOLIN_TYPE) == EnumKaolinType.IRON_PLATED_FIRE_BRICK;
    }

    public boolean checkMiddleBlock(BlockPos at)
    {
        return this.world.isAirBlock(at) || this.world.getBlockState(at).getBlock() instanceof BlockMoltenMetal || this.world.getBlockState(at).getBlock() instanceof BlockBlastFurnaceMetal;
    }

    @Override
    public void acceptBellows(EnumFacing side, boolean mechanical)
    {
        this.additionalTFromBellows = Math.min(mechanical ? 1000 : 500, additionalTFromBellows + (mechanical ? 200 : 100));
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
        return capability == ExPTemperatureCapability.cap ? ExPTemperatureCapability.cap.cast(this.temperatureHandler) : capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : super.getCapability(capability, facing);
    }
}

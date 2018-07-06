package v0id.exp.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesPress;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;

import javax.annotation.Nullable;

public class TileFruitPress extends TileEntity implements ITickable
{
    public ItemStackHandler inventory = new ItemStackHandler(1);

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

    public void jump()
    {
        if (!this.world.isRemote)
        {
            RecipesPress.IRecipePress pressRecipe = null;
            for (RecipesPress.IRecipePress rec : RecipesPress.allRecipes)
            {
                if (rec.matches(this.inventory.getStackInSlot(0)))
                {
                    pressRecipe = rec;
                    break;
                }
            }

            if (pressRecipe != null)
            {
                int current = this.world.getBlockState(this.pos).getValue(ExPBlockProperties.PRESS_VALUE);
                this.world.setBlockState(this.pos, ExPBlocks.fruitPress.getDefaultState().withProperty(ExPBlockProperties.PRESS_VALUE, current - 1));
                this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.BLOCKS, 1.0F, 2F);
                if (current == 1)
                {
                    TileEntity tile = this.world.getTileEntity(this.pos.down());
                    if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
                    {
                        IFluidHandler fh = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                        fh.fill(pressRecipe.getOutput(this.inventory.getStackInSlot(0)), true);
                        this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SLIME_ATTACK, SoundCategory.BLOCKS, 1.0F, 0.1F);
                        this.inventory.getStackInSlot(0).shrink(1);
                    }
                }
            }
        }
    }

    @Override
    public void update()
    {
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack is = this.inventory.getStackInSlot(i);
            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }
        }

        RecipesPress.IRecipePress pressRecipe = null;
        for (RecipesPress.IRecipePress rec : RecipesPress.allRecipes)
        {
            if (rec.matches(this.inventory.getStackInSlot(0)))
            {
                pressRecipe = rec;
                break;
            }
        }

        if (pressRecipe != null)
        {
            if (this.world.getBlockState(this.pos).getValue(ExPBlockProperties.PRESS_VALUE) == 0)
            {
                this.world.setBlockState(this.pos, ExPBlocks.fruitPress.getDefaultState().withProperty(ExPBlockProperties.PRESS_VALUE, 8));
            }
        }
        else
        {
            if (this.world.getBlockState(this.pos).getValue(ExPBlockProperties.PRESS_VALUE) != 0)
            {
                this.world.setBlockState(this.pos, ExPBlocks.fruitPress.getDefaultState().withProperty(ExPBlockProperties.PRESS_VALUE, 0));
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}

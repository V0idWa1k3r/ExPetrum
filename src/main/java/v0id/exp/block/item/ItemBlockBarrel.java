package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.tile.TileBarrel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBlockBarrel extends ItemBlockWithMetadata
{
    public ItemBlockBarrel(Block block)
    {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        TileBarrel tile = (TileBarrel) world.getTileEntity(pos);
        IFluidHandler fh = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        tile.fluidInventory.fill(fh.drain(Integer.MAX_VALUE, true), true);
        tile.treeType = EnumTreeType.values()[stack.getMetadata()];
        return ret;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ICapabilitySerializable<NBTTagCompound>()
        {
            private FluidTank fluidTank = new FluidTank(10000);

            @Override
            public NBTTagCompound serializeNBT()
            {
                NBTTagCompound ret = new NBTTagCompound();
                NBTTagCompound ftnbt = new NBTTagCompound();
                fluidTank.writeToNBT(ftnbt);
                ret.setTag("fluidTank", ftnbt);
                return ret;
            }

            @Override
            public void deserializeNBT(NBTTagCompound nbt)
            {
                fluidTank.readFromNBT(nbt.getCompoundTag("fluidTank"));
            }

            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
            {
                return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
            }

            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
            {
                return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.fluidTank) : null;
            }
        };
    }
}

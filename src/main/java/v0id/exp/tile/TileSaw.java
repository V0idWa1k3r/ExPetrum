package v0id.exp.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.exp.block.tree.BlockLog;
import v0id.exp.util.RotaryHandler;

import javax.annotation.Nullable;

public class TileSaw extends TileEntity implements ITickable
{
    public float progress;
    public RotaryHandler rotaryHandler = new RotaryHandler();

    @Override
    public void update()
    {
        if (this.rotaryHandler.getSpeed() >= 64 && this.rotaryHandler.getTorque() >= 64)
        {
            this.progress += this.rotaryHandler.getSpeed() / 1024 + this.rotaryHandler.getTorque() / 1024;
            this.rotaryHandler.setSpeed(0);
            this.rotaryHandler.setTorque(0);
            IBlockState below = this.world.getBlockState(this.pos.down());
            if (below.getBlock() instanceof BlockLog)
            {
                for (int i = 0; i < 20; ++i)
                {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.pos.getX() + this.world.rand.nextFloat(), this.pos.getY() - 1 + this.world.rand.nextFloat(), this.pos.getZ() + this.world.rand.nextFloat(), (this.world.rand.nextFloat() - this.world.rand.nextFloat()) / 10, (this.world.rand.nextFloat() - this.world.rand.nextFloat()) / 10, (this.world.rand.nextFloat() - this.world.rand.nextFloat()) / 10);
                }

                if (this.world.getWorldTime() % 5 == 0)
                {
                    this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_CAT_HISS, SoundCategory.BLOCKS, 1.0F, 2F);
                }

                if (this.progress >= 20)
                {
                    this.progress %= 20;
                    if (!this.world.isRemote)
                    {
                        EnumTreeType ttype = below.getValue(ExPBlockProperties.TREE_TYPE);
                        Block planks = ExPBlocks.planks[ttype.ordinal() / 15];
                        int meta = ttype.ordinal() % 15;
                        this.world.setBlockToAir(this.pos.down());
                        InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY() - 1, this.pos.getZ(), new ItemStack(planks, 4, meta));
                        this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 2.0F, 0.1F);
                    }
                }
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
        this.rotaryHandler.deserializeNBT(compound.getCompoundTag("rotaryHandler"));
        this.progress = compound.getFloat("progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("rotaryHandler", this.rotaryHandler.serializeNBT());
        ret.setFloat("progress", this.progress);
        return ret;
    }

    public EnumFacing.Axis getAxis()
    {
        return this.world.getBlockState(this.pos).getValue(BlockRotatedPillar.AXIS);
    }

    public EnumFacing.Axis getCheckAxis()
    {
        return this.getAxis() == EnumFacing.Axis.Z ?EnumFacing.Axis.X : EnumFacing.Axis.Z;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap && facing.getAxis() == this.getCheckAxis();
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPRotaryCapability.cap && facing.getAxis() == this.getCheckAxis() ? ExPRotaryCapability.cap.cast(this.rotaryHandler) : super.getCapability(capability, facing);
    }

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
}

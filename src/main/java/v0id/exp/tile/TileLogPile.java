package v0id.exp.tile;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.block.BlockCharcoal;
import v0id.exp.block.BlockLogPile;

public class TileLogPile extends TileEntity
{
    public EnumTreeType type = EnumTreeType.KALOPANAX;
    public int count = 1;
    public boolean rotated = false;
    public Calendar litAt;
    public BlockPos cornerLit1;
    public BlockPos cornerLit2;

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
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.type = EnumTreeType.values()[compound.getByte("type")];
        this.count = compound.getByte("count");
        this.rotated = compound.getBoolean("rotated");
        if (compound.hasKey("litAt"))
        {
            this.litAt = new Calendar();
            this.litAt.deserializeNBT((NBTTagLong) compound.getTag("litAt"));
            this.cornerLit1 = new BlockPos(compound.getInteger("c1x"), compound.getInteger("c1y"), compound.getInteger("c1z"));
            this.cornerLit2 = new BlockPos(compound.getInteger("c2x"), compound.getInteger("c2y"), compound.getInteger("c2z"));
        }
        else
        {
            this.litAt = null;
            this.cornerLit2 = null;
            this.cornerLit1 = null;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setByte("type", (byte)this.type.ordinal());
        ret.setByte("count", (byte)this.count);
        ret.setBoolean("rotated", this.rotated);
        if (this.litAt != null)
        {
            ret.setTag("litAt", this.litAt.serializeNBT());
            ret.setInteger("c1x", this.cornerLit1.getX());
            ret.setInteger("c1y", this.cornerLit1.getY());
            ret.setInteger("c1z", this.cornerLit1.getZ());
            ret.setInteger("c2x", this.cornerLit2.getX());
            ret.setInteger("c2y", this.cornerLit2.getY());
            ret.setInteger("c2z", this.cornerLit2.getZ());
        }

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

    public void onBlockTick()
    {
        if (this.litAt != null)
        {
            if (!this.checkStructure() || !this.checkEnclosure())
            {
                for (int dx = 0; dx < this.cornerLit2.getX() - this.cornerLit1.getX() + 1; ++dx)
                {
                    for (int dy = 0; dy < this.cornerLit2.getY() - this.cornerLit1.getY() + 1; ++dy)
                    {
                        for (int dz = 0; dz < this.cornerLit2.getZ() - this.cornerLit1.getZ() + 1; ++dz)
                        {
                            TileEntity te = this.world.getTileEntity(this.cornerLit1.add(dx, dy, dz));
                            if (te instanceof TileLogPile && te != this)
                            {
                                TileLogPile tlp = (TileLogPile) te;
                                tlp.cornerLit1 = null;
                                tlp.cornerLit2 = null;
                                tlp.litAt = null;
                                tlp.markDirty();
                            }
                        }
                    }
                }

                this.cornerLit1 = null;
                this.cornerLit2 = null;
                this.litAt = null;
                this.markDirty();
            }
            else
            {
                Calendar current = IExPWorld.of(this.world).today();
                long diff = current.getTime() - this.litAt.getTime();
                if (diff >= 8 * current.ticksPerHour)
                {
                    this.formCharcoal();
                }
            }
        }
    }

    public void formCharcoal()
    {
        BlockPos corner1 = this.cornerLit1;
        BlockPos corner2 = this.cornerLit2;
        World world = this.world;
        for (int dy = 0; dy < corner2.getY() - corner1.getY() + 1; ++dy)
        {
            for (int dx = 0; dx < corner2.getX() - corner1.getX() + 1; ++dx)
            {
                for (int dz = 0; dz < corner2.getZ() - corner1.getZ() + 1; ++dz)
                {
                    BlockPos at = this.cornerLit1.add(dx, dy, dz);
                    TileEntity te = this.world.getTileEntity(at);
                    if (te instanceof TileLogPile)
                    {
                        TileLogPile tlp = (TileLogPile) te;
                        world.setBlockState(at, Blocks.AIR.getDefaultState(), 2);
                        this.makeCharcoal(at, tlp.count - world.rand.nextInt(4) + world.rand.nextInt(4), world);
                    }
                }
            }
        }
    }

    public void makeCharcoal(BlockPos pos, int amount, World world)
    {
        while (world.isAirBlock(pos.down()))
        {
            pos = pos.down();
        }

        if (world.getBlockState(pos.down()).getBlock() instanceof BlockCharcoal)
        {
            int current = world.getBlockState(pos.down()).getValue(ExPBlockProperties.CHARCOAL_COUNT);
            if (current < 16)
            {
                int needs = 16 - current;
                int added = 0;
                if (amount >= needs)
                {
                    added = needs;
                }
                else
                {
                    added = amount;
                }

                amount -= added;
                world.setBlockState(pos.down(), world.getBlockState(pos.down()).withProperty(ExPBlockProperties.CHARCOAL_COUNT, current + added), 2);
            }
        }

        if (amount > 0)
        {
            world.setBlockState(pos, ExPBlocks.charcoal.getDefaultState().withProperty(ExPBlockProperties.CHARCOAL_COUNT, amount + 1), 2);
        }
    }

    public boolean initStructure()
    {
        BlockPos step = this.getPos();
        while (world.getBlockState(step).getBlock() instanceof BlockLogPile)
        {
            step = step.north();
        }

        step = step.south();
        while (world.getBlockState(step).getBlock() instanceof BlockLogPile)
        {
            step = step.west();
        }

        step = step.east();
        while (world.getBlockState(step).getBlock() instanceof BlockLogPile)
        {
            step = step.down();
        }

        step = step.up();
        this.cornerLit1 = step;
        step = this.getPos();
        while (world.getBlockState(step).getBlock() instanceof BlockLogPile)
        {
            step = step.south();
        }

        step = step.north();
        while (world.getBlockState(step).getBlock() instanceof BlockLogPile)
        {
            step = step.east();
        }

        step = step.west();
        while (world.getBlockState(step).getBlock() instanceof BlockLogPile)
        {
            step = step.up();
        }

        step = step.down();
        this.cornerLit2 = step;
        if (this.checkStructure())
        {
            for (int dx = 0; dx < this.cornerLit2.getX() - this.cornerLit1.getX() + 1; ++dx)
            {
                for (int dy = 0; dy < this.cornerLit2.getY() - this.cornerLit1.getY() + 1; ++dy)
                {
                    for (int dz = 0; dz < this.cornerLit2.getZ() - this.cornerLit1.getZ() + 1; ++dz)
                    {
                        TileLogPile tlp = (TileLogPile) this.world.getTileEntity(this.cornerLit1.add(dx, dy, dz));
                        tlp.cornerLit1 = this.cornerLit1;
                        tlp.cornerLit2 = this.cornerLit2;
                        tlp.litAt = this.litAt;
                        tlp.markDirty();
                    }
                }
            }

            return true;
        }
        else
        {
            this.litAt = null;
            this.cornerLit1 = null;
            this.cornerLit2 = null;
            return false;
        }
    }

    public boolean checkStructure()
    {
        for (int dx = 0; dx < this.cornerLit2.getX() - this.cornerLit1.getX() + 1; ++dx)
        {
            for (int dy = 0; dy < this.cornerLit2.getY() - this.cornerLit1.getY() + 1; ++dy)
            {
                for (int dz = 0; dz < this.cornerLit2.getZ() - this.cornerLit1.getZ() + 1; ++dz)
                {
                    BlockPos at = this.cornerLit1.add(dx, dy, dz);
                    if (!(world.getBlockState(at).getBlock() instanceof BlockLogPile))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean checkEnclosure()
    {
        for (int dx = 0; dx < this.cornerLit2.getX() - this.cornerLit1.getX() + 1; ++dx)
        {
            for (int dy = 0; dy < this.cornerLit2.getY() - this.cornerLit1.getY() + 1; ++dy)
            {
                for (int dz = 0; dz < this.cornerLit2.getZ() - this.cornerLit1.getZ() + 1; ++dz)
                {
                    BlockPos at = this.cornerLit1.add(dx, dy, dz);
                    if (dx == 0)
                    {
                        if (this.world.isAirBlock(at.west()))
                        {
                            return false;
                        }
                    }

                    if (dx == this.cornerLit2.getX() - this.cornerLit1.getX())
                    {
                        if (this.world.isAirBlock(at.east()))
                        {
                            return false;
                        }
                    }

                    if (dy == 0)
                    {
                        if (this.world.isAirBlock(at.down()))
                        {
                            return false;
                        }
                    }

                    if (dy == this.cornerLit2.getY() - this.cornerLit1.getY())
                    {
                        if (this.world.isAirBlock(at.up()))
                        {
                            return false;
                        }
                    }

                    if (dz == 0)
                    {
                        if (this.world.isAirBlock(at.north()))
                        {
                            return false;
                        }
                    }

                    if (dz == this.cornerLit2.getZ() - this.cornerLit1.getZ())
                    {
                        if (this.world.isAirBlock(at.south()))
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}

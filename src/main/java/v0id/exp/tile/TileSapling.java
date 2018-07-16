package v0id.exp.tile;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.api.exp.world.gen.TreeGenerators;

public class TileSapling extends TileEntity
{
    public long placedOn;
    public EnumTreeType type;

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.type = EnumTreeType.values()[compound.getByte("type")];
        this.placedOn = compound.getLong("placedOn");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setByte("type", (byte) this.type.ordinal());
        ret.setLong("placedOn", this.placedOn);
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

    public boolean canGrow()
    {
        Calendar c = IExPWorld.of(this.world).today();
        if (c.getTime() - this.placedOn < c.ticksPerMonth)
        {
            return false;
        }

        for (int x = -3; x <= 3; ++x)
        {
            for (int z = -3; z <= 3; ++z)
            {
                for (int y = 0; y <= 8; ++y)
                {
                    if (x != 0 || y != 0 || z != 0)
                    {
                        if (!this.world.isAirBlock(this.pos.add(x, y, z)) && !this.world.getBlockState(pos.add(x, y, z)).getBlock().isAssociatedBlock(Blocks.TALLGRASS))
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public void grow()
    {
        TreeGenerators.generators.get(this.type).generate(this.world, this.world.rand, this.pos.offset(EnumFacing.DOWN));
    }
}

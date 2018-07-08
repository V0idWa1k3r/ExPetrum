package v0id.exp.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import v0id.api.exp.tile.IMultiblock;

public class TileStructurePiece extends TileEntity
{
    public BlockPos center;

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.center = new BlockPos(compound.getInteger("cX"), compound.getInteger("cY"), compound.getInteger("cZ"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setInteger("cX", this.center.getX());
        tag.setInteger("cY", this.center.getY());
        tag.setInteger("cZ", this.center.getZ());
        return tag;
    }

    public void breakBlock()
    {
        TileEntity tile = this.world.getTileEntity(this.center);
        if (tile instanceof IMultiblock)
        {
            ((IMultiblock) tile).clearMultiblock();
        }
    }
}

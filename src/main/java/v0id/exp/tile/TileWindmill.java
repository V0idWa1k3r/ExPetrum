package v0id.exp.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.tile.ExPRotaryCapability;
import v0id.api.exp.tile.IMultiblock;
import v0id.api.exp.tile.IRotaryHandler;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.block.BlockWindmill;

public class TileWindmill extends TileEntity implements ITickable, IMultiblock
{
    private EnumFacing.Axis axis;
    public float rotation;
    public float prevRotation;

    @Override
    public void update()
    {
        if (this.world.getBlockState(this.pos).getBlock() instanceof BlockWindmill)
        {
            this.axis = this.world.getBlockState(pos).getValue(BlockHorizontal.FACING).getAxis();
        }

        float wind = IExPWorld.of(this.getWorld()).getWindStrength();
        float speed = Math.min(42, wind) * 16;
        float torque = Math.min(42, wind) * 4;
        this.prevRotation = this.rotation;
        this.rotation += wind / 2F;
        EnumFacing offset = this.world.getBlockState(pos).getValue(BlockHorizontal.FACING);
        TileEntity tile = this.world.getTileEntity(this.pos.offset(offset));
        if (tile != null)
        {
            IRotaryHandler handler = tile.getCapability(ExPRotaryCapability.cap, offset.getOpposite());
            if (handler != null)
            {
                handler.setSpeed(speed);
                handler.setTorque(torque);
            }
        }
    }

    private boolean breakingMultiblock;
    @Override
    public void clearMultiblock()
    {
        if (!breakingMultiblock && this.world.getBlockState(this.pos).getBlock() instanceof BlockWindmill)
        {
            breakingMultiblock = true;
            for (BlockPos at : createPositionArrayFromAxis(this.axis, this.pos))
            {
                this.world.setBlockToAir(at);
            }

            breakingMultiblock = false;
            this.world.getBlockState(this.pos).getBlock().dropBlockAsItem(this.world, this.pos, this.world.getBlockState(this.pos), 0);
            this.world.setBlockToAir(this.pos);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.pos.getX() - 3, this.pos.getY() - 3, this.pos.getZ() - 3, this.pos.getX() + 3, this.pos.getY() + 3, this.pos.getZ() + 3);
    }

    public void createMultiblock()
    {
        for (BlockPos at : createPositionArrayFromAxis(this.world.getBlockState(this.pos).getValue(BlockHorizontal.FACING).getAxis(), this.pos))
        {
            this.placeStructureBlock(at);
        }
    }

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }
    
    public void placeStructureBlock(BlockPos at)
    {
        this.world.setBlockState(at, ExPBlocks.structure.getDefaultState(), 2);
        ((TileStructurePiece)this.world.getTileEntity(at)).center = this.pos;
    }
    
    public static BlockPos[] createPositionArrayFromAxis(EnumFacing.Axis axis, BlockPos pos)
    {
        assert axis != EnumFacing.Axis.Y : "Windmills can't be placed vertically!";
        if (axis == EnumFacing.Axis.Z)
        {
            return new BlockPos[]{
                    pos.west(),
                    pos.east(),
                    pos.down(),
                    pos.up(),
                    pos.up().west(),
                    pos.up().east(),
                    pos.down().west(),
                    pos.down().east(),
                    pos.west(2),
                    pos.east(2),
                    pos.down(2),
                    pos.up(2),
                    pos.up(2).west(),
                    pos.up(2).east(),
                    pos.down(2).west(),
                    pos.down(2).east(),
                    pos.up().west(2),
                    pos.up().east(2),
                    pos.down().west(2),
                    pos.down().east(2)
            };
        }
        else
        {
            return new BlockPos[]{
                    pos.north(),
                    pos.south(),
                    pos.down(),
                    pos.up(),
                    pos.up().north(),
                    pos.up().south(),
                    pos.down().north(),
                    pos.down().south(),
                    pos.north(2),
                    pos.south(2),
                    pos.down(2),
                    pos.up(2),
                    pos.up(2).north(),
                    pos.up(2).south(),
                    pos.down(2).north(),
                    pos.down(2).south(),
                    pos.up().north(2),
                    pos.up().south(2),
                    pos.down().north(2),
                    pos.down().south(2)
            };
        }
    }
}

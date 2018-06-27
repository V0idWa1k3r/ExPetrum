package v0id.api.exp.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChiselable
{
    IBlockState chisel(IBlockState original, World world, BlockPos pos);
}

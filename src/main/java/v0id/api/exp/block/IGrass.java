package v0id.api.exp.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IGrass
{
	int getGrassColor(IBlockState state, BlockPos pos, IBlockAccess w);
	
	EnumGrassState getState();
	
	EnumGrassAmount getAmount(IBlockState state, BlockPos pos, IBlockAccess w);
}

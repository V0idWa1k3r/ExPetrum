package v0id.api.exp.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IShrub
{
	int getShrubColor(IBlockState state, BlockPos pos, IBlockAccess w);
	
	EnumShrubState getState();
	
	int getShrubInternalType();
}

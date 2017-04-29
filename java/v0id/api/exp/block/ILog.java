package v0id.api.exp.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface ILog
{
	boolean isSameWoodType(World w, IBlockState state, BlockPos pos, IBlockState of);
	
	void dropLogItem(World w, IBlockState state, Vec3d at);
}

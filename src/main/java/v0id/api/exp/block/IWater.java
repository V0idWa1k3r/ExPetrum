package v0id.api.exp.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWater
{
	boolean isSalt(World w, BlockPos pos);
}

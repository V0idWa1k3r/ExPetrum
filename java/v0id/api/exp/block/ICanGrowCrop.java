package v0id.api.exp.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICanGrowCrop
{
	float getNutrientLevel(World w, BlockPos pos);
	
	float getHumidityLevel(World w, BlockPos pos);
	
	float getGrowthMultiplier(World w, BlockPos pos);
}

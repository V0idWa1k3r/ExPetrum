package v0id.exp.world.gen.biome;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import v0id.api.exp.data.ExPBlocks;

public class CoralGenerator extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		if (!this.checkBiome(worldIn, position))
		{
			return false;
		}
		
		while ((worldIn.isAirBlock(position) || worldIn.getBlockState(position).getMaterial() == Material.WATER) && position.getY() > 0)
		{
			position = position.down();
		}
		
		if (position.getY() == 0)
		{
			return false;
		}
		
		this.genCoral(worldIn, position, 1 + rand.nextInt(3), 1 + rand.nextInt(6));
		genLoop: for (int i = 0; i < 32 + rand.nextInt(96); ++i)
		{
			Vec3d xzDirOffset = new Vec3d(rand.nextDouble() - rand.nextDouble(), 0, rand.nextDouble() - rand.nextDouble()).normalize().scale(2 + rand.nextDouble() * 6);
			BlockPos offset = position.add(xzDirOffset.x, 0, xzDirOffset.z);
			if (worldIn.getBlockState(offset).getMaterial() != Material.WATER)
			{
				while (worldIn.getBlockState(offset).getMaterial() != Material.WATER)
				{
					offset = offset.up();
					if (worldIn.isAirBlock(offset) || worldIn.getBlockState(offset) == this.coralPlantState || worldIn.getBlockState(offset) == this.coralState)
					{
						continue genLoop;
					}
				}
				
				offset = offset.down();
			}
			else
			{
				while (worldIn.getBlockState(offset).getMaterial() == Material.WATER)
				{
					offset = offset.down();
					if (worldIn.getBlockState(offset) == this.coralPlantState || worldIn.getBlockState(offset) == this.coralState)
					{
						continue genLoop;
					}
				}
			}
			
			if (this.checkBiome(worldIn, offset))
			{
				this.genCoral(worldIn, offset, 1 + rand.nextInt(3), 1 + rand.nextInt(6));
			}
		}
		
		return true;
	}
	
	final IBlockState coralState = ExPBlocks.coralRock.getDefaultState();
	final IBlockState coralPlantState = ExPBlocks.coralPlant.getDefaultState();
	
	public void genCoral(World w, BlockPos pos, int height, int depth)
	{
		for (int i = 0; i <= depth; ++i)
		{
			w.setBlockState(pos.down(i), this.coralState);
		}
		
		for (int i = 0; i < height; ++i)
		{
			w.setBlockState(pos.up(i), this.coralState);
		}
		
		w.setBlockState(pos.up(height), this.coralPlantState);
	}
	
	public boolean checkBiome(World w, BlockPos pos)
	{
		return BiomeDictionary.areSimilar(w.getBiome(pos), Biomes.OCEAN);
	}
}

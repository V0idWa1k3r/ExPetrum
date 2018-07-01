package v0id.exp.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.items.IItemHandler;
import v0id.core.VoidApi;
import v0id.core.util.java.ColorHEX;
import v0id.core.util.java.ColorHSV;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.ICanGrowCrop;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.block.ILeaves;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.IBiome;
import v0id.api.exp.world.IExPWorld;

import java.util.List;
import java.util.Optional;

public class Helpers
{
	public static final double TIME_TO_DEGREE_CONST = 0.01275;
	public static final int DAYNIGHT_LENGTH = 24000;

	public static void dropInventoryItems(World world, BlockPos pos, IItemHandler cap)
    {
        if (cap == null)
        {
            return;
        }

        for (int i = 0; i < cap.getSlots(); ++i)
        {
            ItemStack itemstack = cap.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
            }
        }
    }

	public static <T extends Entity> T getClosest(List<T> entities, Entity to)
	{
		T ret = null;
		double dist = Double.MAX_VALUE;
		for (T t : entities)
		{
			double distance = t.getDistance(to);
			if (distance < dist)
			{
				dist = distance;
				ret = t;
			}
		}
		
		return ret;
	}
	
	public static <T extends Entity>List<T> rayTraceEntities(World w, Vec3d pos, Vec3d ray, Optional<Predicate<T>> entityFilter, Class<T> entityClazz)
	{
		AxisAlignedBB aabb = new AxisAlignedBB(pos, pos.add(new Vec3d(1, 1, 1))).expand(ray.x, ray.y, ray.z);
		Vec3d checkVec = pos.add(ray);
		List<T> ret = Lists.newArrayList();
		for (T t : w.getEntitiesWithinAABB(entityClazz, aabb, entityFilter.orElse(Predicates.alwaysTrue())))
		{
			AxisAlignedBB entityBB = t.getEntityBoundingBox();
			if (entityBB == null)
			{
				continue;
			}
			
			if (entityBB.intersects(pos, checkVec))
			{
				ret.add(t);
			}
		}
		
		return ret;
	}
	
	public static int getLeafColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
	{
		if (state.getBlock() instanceof ILeaves)
		{
			return ((ILeaves)state.getBlock()).getLeavesColor(worldIn, state, pos);
		}
		
		return worldIn != null && pos != null ? VoidApi.proxy.getClientGrassColor(worldIn, pos) : 0x166612;
	}
	
	public static int getCoralColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
	{
		int xzpos = pos.getX() + pos.getZ() / 2;
		ColorHSV hsv = new ColorHSV(Math.abs(xzpos) % 360, 1, 1);
		return ColorHEX.FromHSV(hsv).getHexcode();
	}
	
	public static int getGrassColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
	{
		if (state.getBlock() instanceof IGrass)
		{
			return ((IGrass)state.getBlock()).getGrassColor(state, pos, worldIn);
		}
		
		return worldIn != null && pos != null ? VoidApi.proxy.getClientGrassColor(worldIn, pos) : 0x166612;
	}
	
	public static boolean canPlantGrow(BlockPos pos, World w)
	{
		return w.canBlockSeeSky(pos.up());
	}
	
	/**
	 * @deprecated Use {@link #getGenericGrowthModifier(BlockPos,World,boolean)} instead
	 */
	public static float getGenericGrowthModifier(BlockPos pos, World w)
	{
		return getGenericGrowthModifier(pos, w, false);
	}

	public static float getGenericGrowthModifier(BlockPos pos, World w, boolean checkFertility)
	{
		float timeMultiplier = (float) Math.sin(Math.toRadians((w.getWorldTime() % DAYNIGHT_LENGTH) * TIME_TO_DEGREE_CONST));
		float ret = 1 + timeMultiplier;
		if (checkFertility)
		{
			IBlockState growsOn = w.getBlockState(pos.down());
			if (growsOn.getBlock() instanceof ICanGrowCrop)
			{
				ret *= ((ICanGrowCrop)growsOn.getBlock()).getGrowthMultiplier(w, pos.down());
			}
		}
		
		return ret;
	}
	
	public static EnumGrassState getSuggestedGrassState(BlockPos pos, World w)
	{
		IExPWorld world = IExPWorld.of(w);
		EnumSeason season = world.getCurrentSeason();
		return season == EnumSeason.WINTER ? EnumGrassState.DEAD : season == EnumSeason.AUTUMN ? EnumGrassState.DRY : EnumGrassState.NORMAL;
	}
	
	public static int getWindStrengthIndex(float windKH)
	{
		return windKH < 1 ? 0 :
			windKH >= 1 && windKH <= 5 ? 1 :
			windKH > 5 && windKH <= 11 ? 2 :
			windKH > 11 && windKH <= 19 ? 3 :
			windKH > 19 && windKH <= 28 ? 4 :
			windKH > 28 && windKH <= 38 ? 5 :
			windKH > 38 && windKH <= 49 ? 6 :
			windKH > 49 && windKH <= 61 ? 7 :
			windKH > 61 && windKH <= 74 ? 8 :
			windKH > 74 && windKH <= 88 ? 9 :
			windKH > 88 && windKH <= 102 ? 10 :
			windKH > 102 && windKH <= 117 ? 11 : 12;
	}
	
	public static float getTemperatureAt(World w, BlockPos pos)
	{
		float tempBase = IExPWorld.of(w).getOverhaulTemperature();
		Biome b = w.getBiome(pos);
		if (b instanceof IBiome)
		{
			tempBase *= ((IBiome)b).getTemperatureMultiplier();
			tempBase += ((IBiome)b).getTemperatureBaseModifier();
		}
		return tempBase;
	}
	
	public static int getTemperatureIndex(float tempC)
	{
		return 
			tempC < -30 ? 0 :
			tempC > -30 && tempC <= -20 ? 1 :
			tempC > -20 && tempC <= -12 ? 2 :
			tempC > -12 && tempC <= -2 ? 3 :
			tempC > -2 && tempC < 2 ? 4 :
			tempC >= 2 && tempC < 12 ? 5 :
			tempC >= 12 && tempC < 20 ? 6 :
			tempC >= 20 && tempC < 30 ? 7 : 8;
	}
	
	public static float getMoistureAt(World w, BlockPos pos)
	{
		return w.isRaining() ? 1 : IExPWorld.of(w).getOverhaulHumidity();
	}
}

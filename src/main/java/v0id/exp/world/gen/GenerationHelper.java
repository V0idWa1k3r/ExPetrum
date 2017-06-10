package v0id.exp.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.exp.world.gen.WorldTypeExP.BiomeProviderExP;

public class GenerationHelper
{
	public static Random rand = new Random();
	public static long lastRememberedSeed;
	public static Vec3i offsetStone;
	public static Vec3i offsetDirt;
	
	public static EnumRockClass getStoneTypeAt(World w, BlockPos at)
	{
		if (lastRememberedSeed != w.getSeed())
		{
			rand.setSeed(w.getSeed());
			offsetStone = new Vec3i(rand.nextInt(300000) - rand.nextInt(300000), 0, rand.nextInt(300000) - rand.nextInt(300000));
			offsetDirt = new Vec3i(rand.nextInt(300000) - rand.nextInt(300000), 0, rand.nextInt(300000) - rand.nextInt(300000));
		}
		
		BlockPos actual = at.add(offsetStone);
		
		try
		{
			FeatureProvider provider = ((BiomeProviderExP)w.getBiomeProvider()).featureProvider;
			return EnumRockClass.values()[Math.abs(provider.getByte(actual, provider.cacheRocks)) % 16];
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return EnumRockClass.ANDESITE;
		}
	}
	
	public static EnumDirtClass getDirtTypeAt(World w, BlockPos at)
	{
		if (lastRememberedSeed != w.getSeed())
		{
			rand.setSeed(w.getSeed());
			offsetStone = new Vec3i(rand.nextInt(300000) - rand.nextInt(300000), 0, rand.nextInt(300000) - rand.nextInt(300000));
			offsetDirt = new Vec3i(rand.nextInt(300000) - rand.nextInt(300000), 0, rand.nextInt(300000) - rand.nextInt(300000));
		}
		
		BlockPos actual = at.add(offsetDirt);
		
		try
		{
			FeatureProvider provider = ((BiomeProviderExP)w.getBiomeProvider()).featureProvider;
			return EnumDirtClass.values()[Math.abs(provider.getByte(actual, provider.cacheSoil)) % 16];
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return EnumDirtClass.ACRISOL;
		}
	}
}

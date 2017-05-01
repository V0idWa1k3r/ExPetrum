package v0id.exp.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumRockClass;

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
		Biome b = w.getBiomeProvider().getBiome(actual);
		return EnumRockClass.values()[Biome.getIdForBiome(b) % EnumRockClass.values().length];
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
		Biome b = w.getBiomeProvider().getBiome(actual);
		return EnumDirtClass.values()[Biome.getIdForBiome(b) % EnumDirtClass.values().length];
	}
}

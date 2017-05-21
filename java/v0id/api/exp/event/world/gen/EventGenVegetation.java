package v0id.api.exp.event.world.gen;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired when ExPetrum tries to generate tallgrass in the world. <br>
 * This event is fired at MinecraftForge.TERRAIN_GEN_BUS. <br>
 * This event is cancelable.
 * @author V0idWa1k3r
 *
 */
@Cancelable
public class EventGenVegetation extends Event
{
	/**
	 * World the tallgrass is generated in
	 */
	public final World world;
	
	/**
	 * Position in the world the tallgrass is generating at
	 */
	public final BlockPos pos;
	
	/**
	 * Generic random
	 */
	public final Random rand;
	
	/**
	 * Tallgrass generator. Please do not set to null. 
	 * Can be changed to another WorldGenerator implementation.
	 * If changed the provided implementation will be used when generating tallgrass.
	 */
	@Nonnull
	public WorldGenerator generator;
	
	/**
	 * The type of the generation
	 */
	public final Type generationType;
	
	public EventGenVegetation(World w, BlockPos pos, Random rand, WorldGenerator generator, Type t)
	{
		this.world = w;
		this.pos = pos;
		this.rand = rand;
		this.generator = generator;
		this.generationType = t;
	}
	
	public static enum Type
	{
		TALLGRASS,
		FLOWER,
		CATTAIL,
		SEAWEED,
		BUSH,
		TALL_PLANT,
		WILD_CROP,
		OTHER
	}
}

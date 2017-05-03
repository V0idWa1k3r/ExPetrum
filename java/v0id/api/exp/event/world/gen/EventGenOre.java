package v0id.api.exp.event.world.gen;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired when ExPetrum tries to generate ores in the world. <br>
 * This event is fired at MinecraftForge.TERRAIN_GEN_BUS. <br>
 * This event is cancelable.
 * @author V0idWa1k3r
 *
 */
@Cancelable
public class EventGenOre extends Event
{
	/**
	 * World the ores are generated in
	 */
	public final World world;
	
	/**
	 * Position in the world the ores are generating at
	 */
	public final BlockPos pos;
	
	/**
	 * Generic random
	 */
	public final Random rand;
	
	/**
	 * Ore generator. Please do not set to null. 
	 * Can be changed to another WorldGenerator implementation.
	 * If changed the provided implementation will be used when generating ores.
	 */
	@Nonnull
	public WorldGenerator generator;
	
	public EventGenOre(World w, BlockPos pos, Random rand, WorldGenerator generator)
	{
		this.world = w;
		this.pos = pos;
		this.rand = rand;
		this.generator = generator;
	}
}

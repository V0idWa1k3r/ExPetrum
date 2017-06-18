package v0id.api.exp.event.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import v0id.api.exp.world.gen.ITreeGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Fired when ExPetrum tries to generate a tree in the world. <br>
 * This event is fired at MinecraftForge.TERRAIN_GEN_BUS. <br>
 * This event is cancelable.
 * @author V0idWa1k3r
 *
 */
@SuppressWarnings("CanBeFinal")
@Cancelable
public class EventGenTree extends Event
{
	/**
	 * World the tree is generated in
	 */
	public final World world;
	
	/**
	 * Position in the world the tree is generating at
	 */
	public final BlockPos pos;
	
	/**
	 * Generic random
	 */
	public final Random rand;
	
	/**
	 * Tree generator. Please do not set to null. 
	 * Can be changed to another ITreeGenerator implementation.
	 * If changed the provided implementation will be used when generating a tree.
	 */
	@Nonnull
	public ITreeGenerator generator;
	
	public EventGenTree(World w, BlockPos pos, Random rand, @Nonnull ITreeGenerator generator)
	{
		this.world = w;
		this.pos = pos;
		this.rand = rand;
		this.generator = generator;
	}
}

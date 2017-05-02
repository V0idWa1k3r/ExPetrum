package v0id.api.exp.event.world;

import net.minecraftforge.fml.common.eventhandler.Event;
import v0id.api.exp.world.IExPWorld;

/**
 * Fired when the world handler creates a temperature table for today and tomorrow. <br>
 * Fired at MinecraftForge.EVENT_BUS. <br>
 * This event is not cancelable. <br>
 * This event does not have a result.
 * @author V0idWa1k3r
 *
 */
public class EventGenerateTemperatureTable extends Event
{
	/**
	 * The world handler
	 */
	public final IExPWorld world;
	
	/**
	 * The temperature data provided. Can be changed.
	 */
	public float[] data;
	
	public EventGenerateTemperatureTable(IExPWorld world, float[] data)
	{
		super();
		this.world = world;
		this.data = data;
	}
}

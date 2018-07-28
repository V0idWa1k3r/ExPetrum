package v0id.api.exp.event.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * The event for temperature getter firing at {@link v0id.exp.util.Helpers#getTemperatureAt(World, BlockPos)}
 * <br>
 * This event is fired at MinecraftForge.EVENT_BUS
 */
public class EventTemperatureGetter extends Event
{
    /**
     * The temperature that is being get. Mutable.
     */
    public float temperature;

    /**
     * The world the temperature is get at.
     */
    @Nonnull
    public final World w;

    /**
     * The position the temperature is checked at.
     */
    @Nonnull
    public final BlockPos pos;

    public EventTemperatureGetter(float temperature, @Nonnull World w, @Nonnull BlockPos pos)
    {
        this.temperature = temperature;
        this.w = w;
        this.pos = pos;
    }
}

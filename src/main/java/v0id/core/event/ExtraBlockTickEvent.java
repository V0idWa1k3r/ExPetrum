package v0id.core.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.util.DimBlockPos;

/**
 * ExtraBlockTickEvent is fired each tick for a random block in a loaded chunk in each loaded world.<br>
 * This event is fired during server tick handling in <br>
 * {@link v0id.core.event.handler.VCEventHandler#onWorldServerTick(TickEvent.WorldTickEvent)} <br>
 * <br>
 * This event is not {@link Cancelable}.<br>
 * <br>
 * This event does not have a result. {@link HasResult} <br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 **/
public class ExtraBlockTickEvent extends Event 
{
	private final BlockPos tickingPos;
	private final World world;
	public final Side side = Side.SERVER;
	
	public ExtraBlockTickEvent(DimBlockPos pos)
	{
		super();
		this.tickingPos = pos.pos;
		this.world = DimensionManager.getWorld(pos.dim);
	}

	public BlockPos getTickingPos()
	{
		return tickingPos;
	}

	public World getWorld()
	{
		return world;
	}
}

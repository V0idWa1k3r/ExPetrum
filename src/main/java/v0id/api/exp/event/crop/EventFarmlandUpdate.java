package v0id.api.exp.event.crop;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.api.exp.world.ImmutableCalendar;

/**
 * Generic event for IFarmland capability's onWorldTick method. 
 * Fired at MinecraftForge.EVENT_BUS. <br>
 * This event is not cancelable. <br>
 * This event does not have a result.
 * @author V0idWa1k3r
 *
 */
public class EventFarmlandUpdate extends Event
{
	/**
	 * The farmland being updated
	 */
	public final IFarmland farmland;
	
	/**
	 * The world the event is happening in
	 */
	public final World world;
	
	/**
	 * Farmland's position
	 */
	public final BlockPos pos;

	public EventFarmlandUpdate(IFarmland farmland, World world, BlockPos pos)
	{
		super();
		this.farmland = farmland;
		this.world = world;
		this.pos = pos;
	}
	
	/**
	 * Fired before default update logic happens at all
	 * Fired at MinecraftForge.EVENT_BUS. <br>
	 * This event is cancelable. If cancelled no default logic will evaluate <br>
	 * This event does not have a result.
	 * @author V0idWa1k3r
	 *
	 */
	@Cancelable
	public static class Pre extends EventFarmlandUpdate
	{
		public Pre(IFarmland farmland, World world, BlockPos pos)
		{
			super(farmland, world, pos);
		}
	}
	
	/**
	 * Fired after all logic has happened.
	 * Fired at MinecraftForge.EVENT_BUS. <br>
	 * This event is not cancelable. <br>
	 * This event does not have a result.
	 * @author V0idWa1k3r
	 *
	 */
	public static class Post extends EventFarmlandUpdate
	{
		public Post(IFarmland farmland, World world, BlockPos pos)
		{
			super(farmland, world, pos);
		}
	}
	
	/**
	 * Generic logic handling event for the farmland.
	 * Fired at MinecraftForge.EVENT_BUS. <br>
	 * This event is not cancelable. <br>
	 * This event does not have a result.
	 * @author V0idWa1k3r
	 *
	 */
	public static class Logic extends EventFarmlandUpdate
	{
		/**
		 * Today reference. Immutable.
		 */
		public final ImmutableCalendar immutableCalendarReference;
		
		/**
		 * Logic ticks happened. Can be changed. The changed value will be used.
		 */
		public long ticksHappened;

		public Logic(IFarmland farmland, World world, BlockPos pos, ImmutableCalendar immutableCalendarReference, long ticksHappened)
		{
			super(farmland, world, pos);
			this.immutableCalendarReference = immutableCalendarReference;
			this.ticksHappened = ticksHappened;
		}
		
		/**
		 * Fired before all logic happens.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is cancelable. If it is canceled no logic will evaluate. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		@Cancelable
		public static class Pre extends Logic
		{
			public Pre(IFarmland farmland, World world, BlockPos pos, ImmutableCalendar immutableCalendarReference, long ticksHappened)
			{
				super(farmland, world, pos, immutableCalendarReference, ticksHappened);
			}
		}
		
		/**
		 * Fired before any changes are applied to the farmland but after most calculations have been done.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		public static class WaterNutrientLogic extends Logic
		{
			/**
			 * The amount of moisture the farmland is about to gain or loose with all checks applied. <br>
			 * Can be changed. The changed value will be used in logic. <br>
			 * Can be negative
			 */
			public float waterLossBase;
			
			/**
			 * The amount of butrients the farmland is about to gain or loose with all checks applied. <br>
			 * Can be changed. The changed value will be used in logic. <br>
			 * Can be negative
			 */
			public float nutrientGainBase;
			
			public WaterNutrientLogic(IFarmland farmland, World world, BlockPos pos, ImmutableCalendar immutableCalendarReference, long ticksHappened, float waterLossBase,	float nutrientGainBase)
			{
				super(farmland, world, pos, immutableCalendarReference, ticksHappened);
				this.waterLossBase = waterLossBase;
				this.nutrientGainBase = nutrientGainBase;
			}
		}
		
		/**
		 * Fired before any changes are applied to the farmland but after most calculations have been done, it is raining and the farmland can see the sky. <br>
		 * Fired directly before farmland's moisture gets set to 1 because of rain.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is cancelable. If canceled farmland's moisture will not change due to rain <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		@Cancelable
		public static class Rain extends Logic
		{
			public Rain(IFarmland farmland, World world, BlockPos pos, ImmutableCalendar immutableCalendarReference, long ticksHappened)
			{
				super(farmland, world, pos, immutableCalendarReference, ticksHappened);
			}
		}
		
		/**
		 * Fired after all changes were applied to the farmland but before sync and final changes have been done.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		public static class Post extends Logic
		{
			public Post(IFarmland farmland, World world, BlockPos pos, ImmutableCalendar immutableCalendarReference,long ticksHappened)
			{
				super(farmland, world, pos, immutableCalendarReference, ticksHappened);
			}
		}
	}
}

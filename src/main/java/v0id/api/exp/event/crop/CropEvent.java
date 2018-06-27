package v0id.api.exp.event.crop;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import v0id.api.exp.tile.crop.EnumCropHarvestAction;
import v0id.api.exp.tile.crop.EnumPlantNutrient;
import v0id.api.exp.tile.crop.IExPCrop;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.api.exp.world.ImmutableCalendar;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Generic event for IExPCrop capability's different methods.
 * Fired at MinecraftForge.EVENT_BUS. <br>
 * This event is not cancelable. <br>
 * This event does not have a result.
 * @author V0idWa1k3r
 *
 */
public class CropEvent extends Event
{
	/**
	 * The crop that triggers the event
	 */
	public final IExPCrop crop;
	
	/**
	 * The world the event is triggered in
	 */
	public final World w;
	
	/**
	 * The position of the crop in the world
	 */
	public final BlockPos pos;

	public CropEvent(IExPCrop crop, World w, BlockPos at)
	{
		super();
		this.crop = crop;
		this.w = w;
		this.pos = at;
	}
	
	/**
	 * Fired when a crop takes damage.
	 * Fired at MinecraftForge.EVENT_BUS. <br>
	 * This event is cancelable. If canceled the crop will not be damaged <br>
	 * This event does not have a result.
	 * @author V0idWa1k3r
	 *
	 */
	@SuppressWarnings("CanBeFinal")
	@Cancelable
	public static class Damage extends CropEvent
	{
		public Damage(IExPCrop crop, World w, BlockPos pos, float f)
		{
			super(crop, w, pos);
			this.amount = f;
		}

		/**
		 * The amount of damage the crop is going to take. Mutable.
		 */
		public float amount;
	}
	
	/**
	 * Generic event for IExPCrop capability's harvest related methods.
	 * Fired at MinecraftForge.EVENT_BUS. <br>
	 * This event is not cancelable. <br>
	 * This event does not have a result.
	 * @author V0idWa1k3r
	 *
	 */
	public static class Harvest extends CropEvent
	{
		public enum Action
		{
			HARVEST_DROPS,
			HARVEST_SEEDS
		}
		
		/**
		 * Is the player clicking their right mouse button to harvest the crop?
		 */
		public final boolean rmb;
		
		/**
		 * The action attempted. Can be null if the event is not directly responsible for harvesting!
		 */
		@Nullable
		public final Action action;
		
		public Harvest(IExPCrop crop, World w, BlockPos at, boolean b, @Nullable Action a)
		{
			super(crop, w, at);
			this.rmb = b;
			this.action = a;
		}
		
		/**
		 * Fired before any logic related to crop harvesting is fired.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is cancelable. If canceled the crop will not be harvested. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		@Cancelable
		public static class Pre extends Harvest
		{
			public Pre(IExPCrop crop, World w, BlockPos at, boolean b)
			{
				super(crop, w, at, b, null);
			}
		}
		
		/**
		 * Fired when crop needs to determine whether the player is harvesting it with a gardening spade.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event has a result. <br>
		 * If the result is DEFAULT then default checks will be applied. <br>
		 * If the result is DENY then the check will fail. <br>
		 * If the result is ALLOW then the check will succeed. <br>
		 * @author V0idWa1k3r
		 *
		 */
		@HasResult
		public static class GardeningSpadeCheck extends Harvest
		{
			public GardeningSpadeCheck(IExPCrop crop, World w, BlockPos at, ItemStack checked, boolean b)
			{
				super(crop, w, at, b, Action.HARVEST_SEEDS);
				this.checkedStack = checked;
			}

			/**
			 * The item the player uses to harvest the crop.
			 */
			public final ItemStack checkedStack;
		}
		
		/**
		 * Fired when any drop is added to crop's drop list on harvest allowing subscribers to modify the droplist contents.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		public static class PopulateDropList extends Harvest
		{
			/**
			 * The current drop list. Content can be changed at will.
			 */
			public final List<ItemStack> currentDrops;
			
			public PopulateDropList(IExPCrop crop, World w, BlockPos at, boolean b, List<ItemStack> list, Action a)
			{
				super(crop, w, at, b, a);
				this.currentDrops = list;
			}
		}
		
		/**
		 * Fired when the growth for peppers gets offset. As peppers are hardcoded this event allows influence over them.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is cancelable. If canceled then no growth change will happen <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		@SuppressWarnings("CanBeFinal")
		@Cancelable
		public static class PepperSetGrowth extends Harvest
		{
			public PepperSetGrowth(IExPCrop crop, World w, BlockPos at, boolean b, float f, Action a)
			{
				super(crop, w, at, b, a);
				this.growth = f;
			}

			/**
			 * The growth that is going to be set. Mutable
			 */
			public float growth;
		}
		
		/**
		 * Fired when the crop is about to handle it's state due to being harvested.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is cancelable. If canceled then no change will happen <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		@SuppressWarnings("CanBeFinal")
		@Cancelable
		public static class OnHarvest extends Harvest
		{
			public OnHarvest(IExPCrop crop, World w, BlockPos at, boolean b, Action a, EnumCropHarvestAction ha)
			{
				super(crop, w, at, b, a);
				this.actionAttempted = ha;
			}

			/**
			 * The action that is about to happen to the crop. Mutable.
			 */
			public EnumCropHarvestAction actionAttempted;
		}
		
		/**
		 * Fired when the player attempts to harvest the crop with their right mouse button. The crop will be harvested if the check will succeed.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event has a result. <br>
		 * If the result is DEFAULT then default checks will be applied. <br>
		 * If the result is DENY then the check will fail. <br>
		 * If the result is ALLOW then the check will succeed. <br>
		 * @author V0idWa1k3r
		 *
		 */
		@SuppressWarnings("CanBeFinal")
		@HasResult
		public static class HarvestAttempt extends Harvest
		{
			public HarvestAttempt(IExPCrop crop, World w, BlockPos at, double d)
			{
				super(crop, w, at, true, Action.HARVEST_DROPS);
				this.chance = d;
			}

			/**
			 * If the result of this event is DEFAULT then this field controls the chance for the player to harvest the crop.
			 */
			public double chance;
		}
		
		/**
		 * Fired after all harvest checks and logic has been performed.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		public static class Post extends Harvest
		{
			public Post(IExPCrop crop, World w, BlockPos at, boolean b)
			{
				super(crop, w, at, b, null);
			}
		}
	}
	
	/**
	 * Generic event for crop update ticks.
	 * Fired at MinecraftForge.EVENT_BUS. <br>
	 * This event is not cancelable. <br>
	 * This event does not have a result.
	 * @author V0idWa1k3r
	 *
	 */
	public static class Update extends CropEvent
	{
		public Update(IExPCrop crop, World w, BlockPos at)
		{
			super(crop, w, at);
		}
		
		/**
		 * Fired before any logic related to crop update is fired.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is cancelable. If canceled the crop update logic will not evaluate. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		@Cancelable
		public static class Pre extends Update
		{
			public Pre(IExPCrop crop, World w, BlockPos at)
			{
				super(crop, w, at);
			}
		}
		
		/**
		 * Fired after all update logic has been performed.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		public static class Post extends Update
		{
			public Post(IExPCrop crop, World w, BlockPos at)
			{
				super(crop, w, at);
			}
		}
		
		/**
		 * Generic event for crop update handling with ticks passed.
		 * Fired at MinecraftForge.EVENT_BUS. <br>
		 * This event is not cancelable. <br>
		 * This event does not have a result.
		 * @author V0idWa1k3r
		 *
		 */
		public static class Logic extends Update
		{
			/**
			 * The amount of ticks passed for the crop.
			 */
			public final long ticks;
			
			/**
			 * Current time reference.
			 */
			public final ImmutableCalendar calendarRef;
			
			public Logic(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c)
			{
				super(crop, w, at);
				this.ticks = l;
				this.calendarRef = c;
			}
			
			/**
			 * Fired when a wild crop tries to grow.
			 * Fired at MinecraftForge.EVENT_BUS. <br>
			 * This event is cancelable. If canceled no growth will occure. <br>
			 * This event does not have a result.
			 * @author V0idWa1k3r
			 *
			 */
			@SuppressWarnings("CanBeFinal")
			@Cancelable
			public static class WildGrowth extends Logic
			{
				public WildGrowth(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, float f)
				{
					super(crop, w, at, l, c);
					this.growth = f;
				}

				/**
				 * The amount of growth to be added. Mutable.
				 */
				public float growth;
			}
			
			/**
			 * Fired when a crop tries to consume water from the farmland it grows on.
			 * Fired at MinecraftForge.EVENT_BUS. <br>
			 * This event is cancelable. If canceled no water will be consumed but the logic will evaluate further. <br>
			 * This event does not have a result.
			 * @author V0idWa1k3r
			 *
			 */
			@SuppressWarnings("CanBeFinal")
			@Cancelable
			public static class WaterConsumption extends Logic
			{
				public WaterConsumption(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, float f, long l1, IFarmland of)
				{
					super(crop, w, at, l, c);
					this.waterConsumed = f;
					this.ticksWaterConsumedFor = l1;
					this.farmland = of;
				}
				
				/**
				 * The amount of water the crop is going to consume. Mutable. If changing do not forget to update the ticksWaterConsumedFor value!
				 */
				public float waterConsumed;
				
				/**
				 * The guessed amount of ticks the plant was consuming water for if the crops ticked upon each tick instead of diff handing. Mutable.
				 */
				public long ticksWaterConsumedFor;
				
				/**
				 * The farmland capability of the farmland block this crop grows on.
				 */
				public final IFarmland farmland;
			}
			
			/**
			 * Generic event for crop update handling the nutrient consumption.
			 * Fired at MinecraftForge.EVENT_BUS. <br>
			 * This event is not cancelable. <br>
			 * This event does not have a result.
			 * @author V0idWa1k3r
			 *
			 */
			public static class NutrientConsumption extends Logic
			{
				public NutrientConsumption(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland f)
				{
					super(crop, w, at, l, c);
					this.farmland = f;
				}
				
				/**
				 * The farmland capability of the farmland block this crop grows on.
				 */
				public final IFarmland farmland;
				
				/**
				 * Fired before the crop attempts to consume nutrients from the farmland.
				 * Fired at MinecraftForge.EVENT_BUS. <br>
				 * This event is not cancelable. <br>
				 * This event has a result. <br>
				 * If the result is DEFAULT then default logic will continue to evaluate. <br>
				 * If the result is DENY then the plant will not consume the nutrients and treat the result as if no nutrients were consumed. <br>
				 * If the result is ALLOW then the check will not consume the nutrients and treat the result as if all required nutrients were consumed. <br>
				 * @author V0idWa1k3r
				 *
				 */
				@HasResult
				public static class Pre extends NutrientConsumption
				{
					public Pre(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland f)
					{
						super(crop, w, at, l, c, f);
					}		
				}
				
				/**
				 * Fired as the crop attempts to consume a specific nutrient from the farmland.
				 * Fired at MinecraftForge.EVENT_BUS. <br>
				 * This event is not cancelable. <br>
				 * This event has a result. <br>
				 * If the result is DEFAULT then the crop will consume the specified amount of nutrient from the farmland. <br>
				 * If the result is DENY then the plant will not consume the nutrient and treat the result as if nutrient was not available. <br>
				 * If the result is ALLOW then the check will not consume the nutrient and treat the result as if nutrient was consumed. <br>
				 * @author V0idWa1k3r
				 *
				 */
				@SuppressWarnings("CanBeFinal")
				@HasResult
				public static class Consume extends NutrientConsumption
				{
					public Consume(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland f, EnumPlantNutrient n, float f1)
					{
						super(crop, w, at, l, c, f);
						this.nutrient = n;
						this.consumed = f1;
					}		
					
					/**
					 * The nutrient consumed.
					 */
					public final EnumPlantNutrient nutrient;
					
					/**
					 * The amount of nutrient consumed. Mutable.
					 */
					public float consumed;
				}
				
				/**
				 * Fired after the crop attempted to consume nutrients from the farmland.
				 * Fired at MinecraftForge.EVENT_BUS. <br>
				 * This event is not cancelable. <br>
				 * This event has a result. <br>
				 * If the result is DEFAULT then default logic will continue to evaluate. <br>
				 * If the result is DENY then the plant will treat the result as if no nutrients were consumed. <br>
				 * If the result is ALLOW then the check will treat the result as if all required nutrients were consumed. <br>
				 * @author V0idWa1k3r
				 *
				 */
				@HasResult
				public static class Post extends NutrientConsumption
				{
					public Post(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland f)
					{
						super(crop, w, at, l, c, f);
					}		
				}
			}
			
			/**
			 * Fired before all growth is done.
			 * Fired at MinecraftForge.EVENT_BUS. <br>
			 * This event is not cancelable. <br>
			 * This event does not have a result.
			 * @author V0idWa1k3r
			 *
			 */
			@SuppressWarnings("CanBeFinal")
			public static class GrowthRate extends Logic
			{
				public GrowthRate(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland farmland, float f)
				{
					super(crop, w, at, l, c);
					this.farmland = farmland;
					this.growthRate = f;
				}
				
				/**
				 * The farmland capability of the farmland block this crop grows on.
				 */
				public final IFarmland farmland;
				
				/**
				 * The growth rate of the crop. Mutable.
				 */
				public float growthRate;
			}
			
			/**
			 * Fired before the crop's growth state changes.
			 * Fired at MinecraftForge.EVENT_BUS. <br>
			 * This event is cancelable. If canceled no growth will occure. <br>
			 * This event does not have a result.
			 * @author V0idWa1k3r
			 *
			 */
			@SuppressWarnings("CanBeFinal")
			@Cancelable
			public static class Growth extends Logic
			{
				public Growth(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland farmland, float f)
				{
					super(crop, w, at, l, c);
					this.farmland = farmland;
					this.growth = f;
				}
				
				/**
				 * The farmland capability of the farmland block this crop grows on.
				 */
				public final IFarmland farmland;
				
				/**
				 * The amount of growth to add to the crop. Mutable.
				 */
				public float growth;
			}
			
			/**
			 * Fired as the crop attempts to regenerate it's health.
			 * Fired at MinecraftForge.EVENT_BUS. <br>
			 * This event is cancelable. If canceled no health will be restored. <br>
			 * This event does not have a result.
			 * @author V0idWa1k3r
			 *
			 */
			@SuppressWarnings("CanBeFinal")
			@Cancelable
			public static class Heal extends Logic
			{
				public Heal(IExPCrop crop, World w, BlockPos at, long l, ImmutableCalendar c, IFarmland farmland, float f)
				{
					super(crop, w, at, l, c);
					this.farmland = farmland;
					this.heal = f;
				}
				
				/**
				 * The farmland capability of the farmland block this crop grows on.
				 */
				public final IFarmland farmland;
				
				/**
				 * The amount of health restored. Mutable.
				 */
				public float heal;
			}
		}
	}
}

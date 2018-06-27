package v0id.exp.crop;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import org.apache.commons.lang3.tuple.Pair;
import v0id.core.VoidApi;
import v0id.core.settings.VCSettings;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.event.crop.CropEvent;
import v0id.api.exp.event.crop.EventFarmlandUpdate;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.quality.EnumQuality;
import v0id.api.exp.tile.crop.*;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.api.exp.world.ImmutableCalendar;
import v0id.api.exp.world.TemperatureRange;
import v0id.exp.block.plant.BlockVegetation;
import v0id.exp.item.ItemFood;
import v0id.exp.util.Helpers;

import java.util.Map.Entry;
import java.util.stream.Stream;

@SuppressWarnings("RedundantCast")
public class CropLogic
{
	public static NonNullList<ItemStack> createDrops(ExPCrop crop)
	{
		if (crop.isDead())
		{
			return NonNullList.withSize(0, ItemStack.EMPTY);
		}
		
		int meta = ItemFood.getMetadataFromCrop(crop.getType());
		if (meta == -1)
		{
			if (crop.getType() == EnumCrop.PEPPER)
			{
				int growthIndex = crop.getGrowthIndex();
				int growthMax = crop.getType().getData().growthStages - 1;
				if (growthIndex == growthMax)
				{
					meta = FoodEntry.PEPPER_RED.getId();
				}
				else
				{
					if (growthIndex == growthMax - 1)
					{
						meta = FoodEntry.PEPPER_YELLOW.getId();
					}
					else
					{
						if (growthIndex == growthMax - 2)
						{
							meta = FoodEntry.PEPPER_GREEN.getId();
						}
						else
						{
							return NonNullList.withSize(0, ItemStack.EMPTY);
						}
					}
				}
			}
			else
			{
				return NonNullList.withSize(0, ItemStack.EMPTY);
			}
		}
		
		ItemStack food = new ItemStack(ExPItems.food, 1, meta);
		ItemFood item = (ItemFood) food.getItem();
		int generation = crop.getGeneration();
		Pair<Float, Float> dropWeight = crop.getType().getDropWeight();
		float randomAmount = dropWeight.getLeft() + v0id.core.VCStatics.rng.nextFloat() * (dropWeight.getRight() - dropWeight.getLeft());
		randomAmount *= Math.min(2, 1 + (float) generation / 100);
		randomAmount *= crop.getHealth() / crop.getType().getData().baseHealth;
		item.setLastTickTime(food, IExPWorld.of(crop.getContainer().getWorld()).today());
		item.setTotalWeight(food, randomAmount);
		TemperatureRange rangeMin = crop.getSurvivalTemperature();
		TemperatureRange rangeMed = crop.getOptimalTemperature();
		TemperatureRange rangeHig = crop.getIdealTemperature();
		float temperature = Helpers.getTemperatureAt(crop.getContainer().getWorld(), crop.getContainer().getPos());
		float qualityMultiplier = rangeHig.isWithinRange(temperature) ? 1.0F : rangeMed.isWithinRange(temperature) ? 0.5F : rangeMin.isWithinRange(temperature) ? 0.25F : 0F;
		EnumQuality quality = crop.isWild() ? EnumQuality.PLAIN : EnumQuality.values()[Math.min((int)((v0id.core.VCStatics.rng.nextFloat() * qualityMultiplier) * (EnumQuality.values().length - 1)), EnumQuality.values().length - 1)];
		quality.setForItem(food);
		return NonNullList.withSize(1, food);
	}
	
	public static ItemStack createSeeds(ExPCrop crop)
	{
		ItemStack seeds = new ItemStack(ExPItems.seeds, crop.isWild() ? 1 : 2, crop.getType().ordinal() - 1);
		CropStats stats = new CropStats(crop.stats);
		++stats.generation;
		stats.growth = 0;
		stats.wild = false;
		float randomChanceMultipleSeeds = Math.min(6, (float)crop.getGeneration() * 0.1F);
		while (randomChanceMultipleSeeds > 0)
		{
			if (v0id.core.VCStatics.rng.nextDouble() * randomChanceMultipleSeeds < 0.5)
			{
				seeds.grow(1);
			}
			
			--randomChanceMultipleSeeds;
		}
		
		if (v0id.core.VCStatics.rng.nextInt(35) < crop.getGeneration())
		{
			TemperatureRange rangeMin = crop.getSurvivalTemperature();
			TemperatureRange rangeMed = crop.getOptimalTemperature();
			TemperatureRange rangeHig = crop.getIdealTemperature();
			if (rangeMin.min > -20 && rangeMin.max < 60)
			{
				stats.growthRanges[0] = new TemperatureRange(rangeMin.min * 1.05F, rangeMin.max * 1.05F);
				stats.growthRanges[1] = new TemperatureRange(rangeMed.min * 1.05F, rangeMed.max * 1.05F);
				stats.growthRanges[2] = new TemperatureRange(rangeHig.min * 1.05F, rangeHig.max * 1.05F);
			}
		}
		
		if (v0id.core.VCStatics.rng.nextInt(100) < crop.getGeneration())
		{
			stats.growthRate *= 1.1;
		}
		
		if (v0id.core.VCStatics.rng.nextInt(20) < crop.getGeneration())
		{
			stats.waterConsumption *= 0.94F;
		}
		
		IExPSeed cap = seeds.getCapability(ExPSeedsCapability.seedsCap, null);
		cap.setExtendedData(stats.createItemNBT(null));
		return seeds;
	}
	
	public static void onFarmlandUpdate(ExPFarmland farmland)
	{
		if (((VCSettings)(VoidApi.config.dataHolder)).recoveryMode)
		{
			return;
		}
		
		if (MinecraftForge.EVENT_BUS.post(new EventFarmlandUpdate.Pre(farmland, farmland.getContainer().getWorld(), farmland.getContainer().getPos())))
		{
			return;
		}
		
		Calendar prev = farmland.timeKeeper;
		if (prev.getTime() == 0)
		{
			prev.setTime(IExPWorld.of(farmland.getContainer().getWorld()).today().getTime());
		}
		else
		{
			Calendar current = IExPWorld.of(farmland.getContainer().getWorld()).today();
			long ticksDelta = current.getTime() - prev.getTime();
			farmland.timeKeeper = current;
			handleFarmlandTimePassed(farmland, ticksDelta, current);
		}
		
		MinecraftForge.EVENT_BUS.post(new EventFarmlandUpdate.Post(farmland, farmland.getContainer().getWorld(), farmland.getContainer().getPos()));
	}
	
	public static void handleFarmlandTimePassed(ExPFarmland farmland, long ticks, Calendar calendarReference)
	{
		World w = farmland.getContainer().getWorld();
		BlockPos pos = farmland.getContainer().getPos();
		ImmutableCalendar ref = new ImmutableCalendar(calendarReference);
		EventFarmlandUpdate.Logic.Pre pre = new EventFarmlandUpdate.Logic.Pre(farmland, w, pos, ref, ticks);
		if (MinecraftForge.EVENT_BUS.post(pre))
		{
			return;
		}
		
		ticks = pre.ticksHappened;
		IBlockState above = w.getBlockState(pos.up());
        TileEntity tileAbove = w.getTileEntity(pos.up());
		if (tileAbove != null && tileAbove.hasCapability(ExPCropCapability.cropCap, EnumFacing.DOWN))
		{
			return;
		}
		
		EventFarmlandUpdate.Logic.WaterNutrientLogic wnLogic = new EventFarmlandUpdate.Logic.WaterNutrientLogic(farmland, w, pos, ref, ticks, (0.00001f * (above.getBlock() instanceof BlockVegetation ? 2 : 1)) * ticks, (1.39e-6F * (above.getBlock() instanceof BlockVegetation ? -10 : 1)) * ticks);
		MinecraftForge.EVENT_BUS.post(wnLogic);
		float waterLoss = wnLogic.waterLossBase;
		float nutrientGain = wnLogic.nutrientGainBase;
		if (w.isRaining() && w.getPrecipitationHeight(pos).getY() - 1 <= pos.getY() && !MinecraftForge.EVENT_BUS.post(new EventFarmlandUpdate.Logic.Rain(farmland, w, pos, ref, ticks)))
		{
			farmland.setMoisture(1);
		}
		else
		{
			farmland.setMoisture(MathHelper.clamp(farmland.getMoisture() - waterLoss, 0, 1));
		}
		
		Stream.of(EnumPlantNutrient.values()).forEach(n -> farmland.setNutrient(n, MathHelper.clamp(farmland.getNutrient(n) + nutrientGain, 0, 1)));
		MinecraftForge.EVENT_BUS.post(new EventFarmlandUpdate.Logic.Post(farmland, w, pos, ref, ticks));
		farmland.setDirty();
	}
	
	public static void onWorldUpdate(ExPCrop crop)
	{
		if (MinecraftForge.EVENT_BUS.post(new CropEvent.Update.Pre(crop, crop.getContainer().getWorld(), crop.getContainer().getPos())))
		{
			return;
		}
		
		Calendar prev = crop.timeKeeper;
		
		// Should not happen
		if (prev.getTime() == 0)
		{
			prev.setTime(IExPWorld.of(crop.getContainer().getWorld()).today().getTime());
		}
		else
		{
			Calendar current = IExPWorld.of(crop.getContainer().getWorld()).today();
			long ticksDelta = current.getTime() - prev.getTime();
			crop.timeKeeper = current;
			handleTimePassed(crop, ticksDelta, current);
		}
		
		if (crop.getContainer() != null && !crop.getContainer().isInvalid() && !crop.getContainer().getWorld().isAirBlock(crop.getContainer().getPos()))
		{
			MinecraftForge.EVENT_BUS.post(new CropEvent.Update.Post(crop, crop.getContainer().getWorld(), crop.getContainer().getPos()));
		}
	}
	
	public static void handleTimePassed(ExPCrop crop, long ticks, Calendar calendarReference)
	{
		// Dead crops can't update
		if (crop.getType() == EnumCrop.DEAD)
		{
			return;
		}
		
		ImmutableCalendar immutableRef = new ImmutableCalendar(calendarReference);
		World w = crop.getContainer().getWorld();
		BlockPos pos = crop.getContainer().getPos();
		TileEntity tile = w.getTileEntity(pos.down());
		
		if (crop.isWild())
		{
			if (crop.getSurvivalTemperature().isWithinRange(Helpers.getTemperatureAt(w, pos)))
			{
				CropEvent.Update.Logic.WildGrowth evt = new CropEvent.Update.Logic.WildGrowth(crop, w, pos, ticks, immutableRef, ticks * (crop.stats.growthRate / (90 * calendarReference.ticksPerDay)));
				if (!MinecraftForge.EVENT_BUS.post(evt))
				{
					crop.setGrowth(Math.min(1, crop.getGrowth() + evt.growth));
				}
			}
			else
			{
				crop.causeDamage(ticks * 0.0001F);
			}
			
			return;
		}
		
		// Crop can't exist on a block that can't support it. Should not happen.
		if (tile == null || !tile.hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
		{
			w.setBlockToAir(pos);
			return;
		}
		
		IFarmland farmland = IFarmland.of(tile, EnumFacing.UP);
		float waterConsumption = crop.getWaterConsumption() / calendarReference.ticksPerDay / 100;
		float waterConsumed = waterConsumption * ticks;
		float waterPresent = farmland.getMoisture();
		float waterActuallyConsumed = Math.min(waterConsumed, waterPresent);
		long ticksWaterConsumedFor = (long) (waterActuallyConsumed / waterConsumption);
		CropEvent.Update.Logic.WaterConsumption waterConsumedEvent = new CropEvent.Update.Logic.WaterConsumption(crop, w, pos, ticks, immutableRef, waterActuallyConsumed, ticksWaterConsumedFor, farmland);
		boolean waterConsumptionEventResult = MinecraftForge.EVENT_BUS.post(waterConsumedEvent);
		waterActuallyConsumed = waterConsumedEvent.waterConsumed;
		ticksWaterConsumedFor = waterConsumedEvent.ticksWaterConsumedFor;
		float waterLeft = waterPresent - waterActuallyConsumed;
		if (!waterConsumptionEventResult)
		{
			farmland.setMoisture(waterLeft);
		}
		
		boolean allNutrientsFine = consumeNutrients(farmland, ticks, crop, calendarReference, w, pos, immutableRef);
		boolean moistureInRange = waterPresent >= crop.getType().getData().humidityRange.min && waterPresent <= crop.getType().getData().humidityRange.max;
		float temperature = Helpers.getTemperatureAt(w, pos);
		boolean inBaseRange = crop.getSurvivalTemperature().isWithinRange(temperature);
		boolean canGrow = crop.canGrowOverride(Helpers.canPlantGrow(pos, w)) && inBaseRange; 
		long growthTicks;
		if (canGrow)
		{
			float growthMultiplier = 0.25F;
			if (allNutrientsFine)
			{
				if (crop.getIdealTemperature().isWithinRange(temperature))
				{
					growthMultiplier = 1.1F;
				}
				else
				{
					if (crop.getOptimalTemperature().isWithinRange(growthMultiplier))
					{
						growthMultiplier = 1.0F;
					}
					else
					{
						growthMultiplier = 0.75F;
					}
				}
			}
			
			growthMultiplier *= farmland.getGrowthMultiplier();
			CropEvent.Update.Logic.GrowthRate growthRateEvent = new CropEvent.Update.Logic.GrowthRate(crop, w, pos, ticks, immutableRef, farmland, growthMultiplier);
			MinecraftForge.EVENT_BUS.post(growthRateEvent);
			growthMultiplier = growthRateEvent.growthRate;
			growthTicks = (long) (ticksWaterConsumedFor * growthMultiplier);
			float grownBy = growthTicks * (crop.stats.growthRate / (calendarReference.ticksPerDay * 30));
			CropEvent.Update.Logic.Growth growthEvent = new CropEvent.Update.Logic.Growth(crop, w, pos, ticks, immutableRef, farmland, grownBy);
			if (!MinecraftForge.EVENT_BUS.post(growthEvent))
			{
				crop.setGrowth(crop.getGrowth() == 1 ? 1 : Math.min(crop.isHarvestSeason(IExPWorld.of(w).getCurrentSeason()) ? 1 : 0.99F, crop.getGrowth() + growthEvent.growth));
			}
			
			if (ticks > ticksWaterConsumedFor)
			{
				crop.causeDamage((ticksWaterConsumedFor - ticks) * 0.0005F);
			}
			else
			{
				if (!crop.isSick && moistureInRange)
				{
					CropEvent.Update.Logic.Heal healEvent = new CropEvent.Update.Logic.Heal(crop, w, pos, ticks, immutableRef, farmland, ticks * 0.0001F);
					if (!MinecraftForge.EVENT_BUS.post(healEvent))
					{
						crop.setHealth(Math.min(crop.getType().getData().baseHealth, crop.getHealth() + healEvent.heal));
					}
				}
			}
		}
		else
		{
			crop.causeDamage(ticks * 0.00138F);
		}
		
		if (crop.isSick)
		{
			crop.causeDamage(ticks * 0.0006F);
		}
		
		if (!moistureInRange)
		{
			crop.causeDamage(ticks * 0.0002F);
		}
		
		farmland.setDirty();
	}
	
	public static boolean consumeNutrients(IFarmland of, long ticks, ExPCrop crop, Calendar calendarReference, World w, BlockPos at, ImmutableCalendar c)
	{
		CropEvent.Update.Logic.NutrientConsumption.Pre pre = new CropEvent.Update.Logic.NutrientConsumption.Pre(crop, w, at, ticks, c, of);
		MinecraftForge.EVENT_BUS.post(pre);
		Result res = pre.getResult();
		if (res != Result.DEFAULT)
		{
			return res == Result.ALLOW;
		}
		
		boolean consumedAll = true;
		for (Entry<EnumPlantNutrient, Float> kv : crop.getNutrientConsumption().entrySet())
		{
			float nutrientConsumed = kv.getValue() / calendarReference.ticksPerDay;
			float nutrientCurrent = of.getNutrient(kv.getKey());
			CropEvent.Update.Logic.NutrientConsumption.Consume evt = new CropEvent.Update.Logic.NutrientConsumption.Consume(crop, w, at, ticks, c, of, kv.getKey(), Math.min(nutrientCurrent, nutrientConsumed));
			MinecraftForge.EVENT_BUS.post(evt);
			Result rese = evt.getResult();
			if (rese != Result.DEFAULT)
			{
				consumedAll &= rese == Result.ALLOW;
				continue;
			}
			
			float actuallyConsumed = evt.consumed;
			float left = nutrientCurrent - actuallyConsumed;
			of.setNutrient(kv.getKey(), left);
			consumedAll &= actuallyConsumed < nutrientCurrent;
		}
		
		CropEvent.Update.Logic.NutrientConsumption.Post post = new CropEvent.Update.Logic.NutrientConsumption.Post(crop, w, at, ticks, c, of);
		MinecraftForge.EVENT_BUS.post(post);
		Result resp = post.getResult();
		if (resp != Result.DEFAULT)
		{
			return resp == Result.ALLOW;
		}
		
		return consumedAll;
	}
}

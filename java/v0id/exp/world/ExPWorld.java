package v0id.exp.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import v0id.api.core.logging.LogLevel;
import v0id.api.core.util.nbt.NBTList;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.event.world.EventGenerateTemperatureTable;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.net.PacketHandlerWorldData;

public class ExPWorld implements IExPWorld
{
	public boolean serverIsDirty;
	public boolean clientIsDirty = true;
	public boolean isRemote;
	public World owner;
	public Calendar dayTimeKeeper = new Calendar();
	
	public long persistentTicks;
	public Vec3d windDirection;
	public float windStrength;
	public float baseTemp;
	public float[] dayTemp;
	public float baseHumidity;
	public float accumulatedHumidity;
	public int rainTicksRemaining;
	
	public boolean persistentTicks_isDirty;
	public boolean windDirection_isDirty;
	public boolean windStrength_isDirty;
	public boolean baseTemp_isDirty;
	public boolean dayTemp_isDirty;
	public boolean baseHumidity_isDirty;
	public boolean accumulatedHumidity_isDirty;
	
	@Override
	public long getPersistentTicks()
	{
		return this.persistentTicks;
	}

	@Override
	public void setPersistentTicks(long newValue)
	{
		assert newValue >= this.persistentTicks : "Persistent ticks can't be decremented!";
		this.persistentTicks_isDirty |= newValue != this.persistentTicks;
		this.serverIsDirty |= this.persistentTicks_isDirty;
		this.persistentTicks = newValue;
	}
	
	public void createDayData()
	{
		Calendar today = this.today();
		Calendar tomorrow = new Calendar(this.getPersistentTicks() + today.ticksPerDay);
		byte monthToday = today.getMonth();
		byte monthTomorrow = tomorrow.getMonth();
		EnumSeason seasonToday = EnumSeason.values()[monthToday / 3];
		EnumSeason seasonTomorrow = EnumSeason.values()[monthTomorrow / 3];
		float monthTodayPercentage = (float)monthToday / 3F;
		float monthTomorrowPercentage = (float)monthTomorrow / 3F;
		byte dayToday = today.getDay();
		byte dayTomorrow = tomorrow.getDay();
		float dayTodayPercentage = (float)dayToday / (float)today.daysPerMonth;
		float dayTomorrowPercentage = (float)dayTomorrow / (float)today.daysPerMonth;
		if (this.dayTemp == null)
		{
			this.dayTemp = new float[8];
			for (byte b = 0; b < 4; ++b)
			{
				this.dayTemp[b] = seasonToday.getTemperatureData().getTemperature(this.owner.rand, monthTodayPercentage, dayTodayPercentage, 0.25F * (b + 1));
				this.dayTemp[b + 4] = seasonTomorrow.getTemperatureData().getTemperature(this.owner.rand, monthTomorrowPercentage, dayTomorrowPercentage, 0.25F * (b + 1));
			}
		}
		else
		{
			for (byte b = 0; b < 4; ++b)
			{
				this.dayTemp[b] = this.dayTemp[b + 4];
				this.dayTemp[b + 4] = seasonTomorrow.getTemperatureData().getTemperature(this.owner.rand, monthTomorrowPercentage, dayTomorrowPercentage, 0.25F * (b + 1));
			}
		}
		
		EventGenerateTemperatureTable egtt = new EventGenerateTemperatureTable(this, this.dayTemp);
		MinecraftForge.EVENT_BUS.post(egtt);
		this.dayTemp = egtt.data;
		
		this.dayTemp_isDirty = true;
		this.serverIsDirty = true;
	}

	@Override
	public Calendar today()
	{
		return this.dayTimeKeeper.withTicks(this.getPersistentTicks());
	}

	@Override
	public EnumSeason getCurrentSeason()
	{
		return EnumSeason.values()[Math.min(this.today().getMonth() / 3, 11)];
	}

	@Override
	public Vec3d getWindDirection()
	{
		return this.windDirection;
	}

	@Override
	public void setWindDirection(Vec3d vec)
	{
		this.windDirection_isDirty |= !this.windDirection.equals(vec);
		this.serverIsDirty |= this.windDirection_isDirty;
		this.windDirection = vec;
	}

	@Override
	public float getWindStrength()
	{
		return this.windStrength;
	}

	@Override
	public void setWindStrength(float newValue)
	{
		this.serverIsDirty |= this.windStrength_isDirty |= this.windStrength != newValue;
		this.windStrength = newValue;
	}

	@Override
	public float getOverhaulTemperature()
	{
		if (this.dayTemp == null)
		{
			return 0;
		}
		
		Calendar c = this.today();
		byte dayProgressCurrent = (byte) (((float)(this.persistentTicks % c.ticksPerDay) / (float)c.ticksPerDay) * 4);
		float tempCurrent = this.dayTemp[dayProgressCurrent];
		float tempNext = this.dayTemp[dayProgressCurrent + 1];
		float currentToNextProgress = (((this.persistentTicks % c.ticksPerDay) + ((float)c.ticksPerDay / 4F)) - (this.persistentTicks % c.ticksPerDay)) / ((float)c.ticksPerDay / 4F);
		float tempRet = this.baseTemp + tempCurrent * (1 - currentToNextProgress) + tempNext * currentToNextProgress;
		if (this.rainTicksRemaining > 0 && tempRet > 0)
		{
			tempRet *= 0.9F;
		}
		
		// Spring temperature ranges fall in the negatives but it would be too harsh to start the player in a winter right away
		boolean firstSpring = c.getTime() < c.ticksPerDay * c.daysPerMonth * 3;
		return firstSpring ? Math.max(1, tempRet) : tempRet;
	}

	@Override
	public void setOverhaulTemperatureBase(float f)
	{
		this.baseTemp_isDirty |= this.baseTemp != f;
		this.serverIsDirty |= this.baseTemp_isDirty;
		this.baseTemp = f;
	}

	@Override
	public float getOverhaulHumidity()
	{
		return this.owner.isRaining() ? 1.0F : Math.min(1, this.baseHumidity + this.accumulatedHumidity);
	}

	@Override
	public void setOverhaulHumidity(float f)
	{
		this.baseHumidity_isDirty |= this.baseHumidity != f;
		this.serverIsDirty |= this.baseHumidity_isDirty;
		this.baseHumidity = f;
	}

	@Override
	public void sendNBT()
	{
		this.serverIsDirty = true;
	}

	@Override
	public void requestNBT()
	{
		this.clientIsDirty = true;
	}

	@Override
	public World getWorld()
	{
		return this.owner;
	}

	@Override
	public void onTick()
	{
		if (this.owner == null || this.owner.provider.getDimension() != 0)
		{
			return;
		}
		
		if (!this.isRemote && (this.dayTemp == null || this.persistentTicks % this.today().ticksPerDay == 0))
		{
			this.createDayData();
		}
		
		long time = this.owner.getWorldTime();
		long ticksSkipped = 0;
		if (time > this.persistentTicks)
		{
			if (time - this.persistentTicks > 1)
			{
				ticksSkipped = time - this.persistentTicks - 1;
			}
			
			this.setPersistentTicks(time);
		}
		
		if (ticksSkipped >= 24000)
		{
			ExPMisc.modLogger.log(LogLevel.Warning, "%d ticks were skipped by the world! This can cause issues.", ticksSkipped);
		}
		
		this.rainTicksRemaining -= 1 + ticksSkipped;
		
		if (this.rainTicksRemaining == 0 && this.owner.isRaining())
		{
			this.owner.setRainStrength(0);
			this.owner.setThunderStrength(0);
			this.owner.getWorldInfo().setRaining(false);
			this.owner.getWorldInfo().setThundering(false);
			this.accumulatedHumidity = this.getWorld().rand.nextFloat() / 3;
			this.accumulatedHumidity_isDirty = true;
			this.serverIsDirty = true;
		}
		
		if (this.rainTicksRemaining < 0 && this.owner.isRaining())
		{
			this.owner.setRainStrength(0);
			this.owner.setThunderStrength(0);
			this.owner.getWorldInfo().setRaining(false);
			this.owner.getWorldInfo().setThundering(false);
		}
		
		if (this.rainTicksRemaining > 0 && !this.owner.isRaining())
		{
			this.owner.getWorldInfo().setRaining(true);
			this.owner.setRainStrength(1F);
		}
		
		if (!this.isRemote)
		{
			// Need this block as the client for some mysterious reason does not receive temperature correctly upon the initial sync packet
			// FIXME fix client not receiving correct dayTemperature data!
			boolean shouldSyncBrokenData = this.persistentTicks % 200 == 0;;
			this.dayTemp_isDirty |= shouldSyncBrokenData;
			this.windDirection_isDirty |= this.windStrength_isDirty |= this.dayTemp_isDirty;
			this.serverIsDirty |= this.dayTemp_isDirty;
			boolean shouldBeRaining = this.owner.rand.nextDouble() <= this.accumulatedHumidity / 5000;
			
			if (this.rainTicksRemaining <= 0)
			{
				this.accumulatedHumidity += this.getWorld().rand.nextFloat() / 12000;
				this.accumulatedHumidity_isDirty = this.persistentTicks % 100 == 0;
				this.serverIsDirty |= this.accumulatedHumidity_isDirty;
			}
			
			if (shouldBeRaining && this.rainTicksRemaining <= 0)
			{
				this.rainTicksRemaining = (int) (1000 + this.getWorld().rand.nextDouble() * 20000);
			}
			
			if (this.getWorld().rand.nextDouble() < 0.001D)
			{
				this.setWindDirection(new Vec3d(this.getWorld().rand.nextDouble() - this.getWorld().rand.nextDouble(), -this.getWorld().rand.nextDouble() / 10, this.getWorld().rand.nextDouble() - this.getWorld().rand.nextDouble()).normalize());
				float strengthRandomFactor = (55F - this.windStrength);
				this.setWindStrength(this.getWorld().rand.nextFloat() * strengthRandomFactor);
			}
		}
		
		if (this.isRemote && this.clientIsDirty)
		{
			this.clientIsDirty = false;
			PacketHandlerWorldData.sendRequestPacket();
		}
		
		if (!this.isRemote && this.serverIsDirty)
		{
			this.serverIsDirty = false;
			PacketHandlerWorldData.sendSyncPacket(this.getWorld());
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		this.setAllDirty(true);
		return this.serializePartialNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.deserializePartialNBT(nbt);
	}
	
	public NBTTagCompound serializePartialNBT()
	{
		NBTTagCompound ret = new NBTTagCompound();
		if (this.owner == null || this.owner.provider.getDimension() != 0)
		{
			return ret;
		}
		
		if (this.persistentTicks_isDirty)
		{
			ret.setLong("persistentTicks", this.persistentTicks);
		}
		
		if (this.windDirection_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			lst.appendTag(new NBTTagDouble(this.windDirection.xCoord));
			lst.appendTag(new NBTTagDouble(this.windDirection.yCoord));
			lst.appendTag(new NBTTagDouble(this.windDirection.zCoord));
			ret.setTag("windDirection", lst);
		}
		
		if (this.windStrength_isDirty)
		{
			ret.setFloat("windStrength", this.windStrength);
		}
		
		if (this.baseTemp_isDirty)
		{
			ret.setFloat("baseTemp", this.baseTemp);
		}
		
		if (this.dayTemp_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			if (this.dayTemp != null)
			{
				for (float f : this.dayTemp)
				{
					lst.appendTag(new NBTTagFloat(f));
				}
				
				ret.setTag("dayTemp", lst);
			}
		}
		
		if (this.baseHumidity_isDirty)
		{
			ret.setFloat("baseHumidity", this.baseHumidity);
		}
		
		if (this.accumulatedHumidity_isDirty)
		{
			ret.setFloat("accumulatedHumidity", this.accumulatedHumidity);
		}
		
		ret.setInteger("rainTicks", this.rainTicksRemaining);
		this.setAllDirty(false);
		return ret;
	}
	
	public void deserializePartialNBT(NBTTagCompound nbt)
	{
		if (this.owner == null || this.owner.provider.getDimension() != 0)
		{
			return;
		}
		
		if (nbt.hasKey("persistentTicks"))
		{
			this.persistentTicks = nbt.getLong("persistentTicks");
		}
		
		if (nbt.hasKey("windDirection"))
		{
			NBTTagList direction = nbt.getTagList("windDirection", NBT.TAG_DOUBLE);
			this.windDirection = new Vec3d(direction.getDoubleAt(0), direction.getDoubleAt(1), direction.getDoubleAt(2));
		}
		
		if (nbt.hasKey("windStrength"))
		{
			this.windStrength = nbt.getFloat("windStrength");
		}
		
		if (nbt.hasKey("baseTemp"))
		{
			this.baseTemp = nbt.getFloat("baseTemp");
		}
		
		if (nbt.hasKey("dayTemp"))
		{
			int i = 0;
			if (this.dayTemp == null)
			{
				this.dayTemp = new float[8];
			}
			
			for (NBTTagFloat nbtFloat : NBTList.<NBTTagFloat>of(nbt.getTagList("dayTemp", NBT.TAG_FLOAT)))
			{
				this.dayTemp[i++] = nbtFloat.getFloat();
			}
		}
		
		if (nbt.hasKey("baseHumidity"))
		{
			this.baseHumidity = nbt.getFloat("baseHumidity");
		}
		
		if (nbt.hasKey("accumulatedHumidity"))
		{
			this.accumulatedHumidity = nbt.getFloat("accumulatedHumidity");
		}
		
		this.rainTicksRemaining = nbt.getInteger("rainTicks");
		if (this.isRemote)
		{
			if (this.rainTicksRemaining > 0)
			{
				this.owner.getWorldInfo().setRaining(true);
				this.owner.setRainStrength(1.0F);
			}
			else
			{
				this.owner.getWorldInfo().setRaining(false);
				this.owner.setRainStrength(0.0F);
			}
		}
	}

	public void setAllDirty(boolean b)
	{
		this.persistentTicks_isDirty = this.windDirection_isDirty = this.windStrength_isDirty = this.baseTemp_isDirty = this.dayTemp_isDirty = this.baseHumidity_isDirty = this.accumulatedHumidity_isDirty = b;
	}
	
	public static ExPWorld createDefault()
	{
		ExPWorld ret = new ExPWorld();
		ret.windDirection = Vec3d.ZERO;
		return ret;
	}
	
	public static ExPWorld createWithOwner(World w)
	{
		ExPWorld ret = createDefault();
		ret.owner = w;
		ret.isRemote = w.isRemote;
		return ret;
	}
}

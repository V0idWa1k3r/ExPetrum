package v0id.exp.world;

import com.google.common.collect.Maps;
import net.minecraft.nbt.*;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.event.world.EventGenerateTemperatureTable;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.IExPWorld;
import v0id.api.exp.world.chunk.IExPChunk;
import v0id.exp.net.ExPNetwork;
import v0id.exp.world.chunk.ExPChunk;

import java.util.Map;

public class ExPWorld implements IExPWorld
{
	public boolean serverIsDirty;
	public boolean clientIsDirty = true;
	public boolean isRemote;
	public World owner;
	public final Calendar dayTimeKeeper = new Calendar();

	public long lastTime;
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

	public final Map<ChunkPos, IExPChunk> chunkDataMap = Maps.newHashMap();

    public ExPWorld()
    {
        this.setAllDirty(true);
    }

    @Override
	public long getPersistentTicks()
	{
		return this.persistentTicks;
	}

	@Override
	public void setPersistentTicks(long newValue)
	{
		assert newValue >= this.persistentTicks : "Persistent ticks can't be decremented!";
		this.persistentTicks_isDirty = this.serverIsDirty = true;
		this.persistentTicks = newValue;
	}
	
	public void createDayData()
	{
		Calendar today = this.today();
		Calendar tomorrow = new Calendar(today.getTime() + today.ticksPerDay);
		byte monthToday = today.getMonth();
		byte monthTomorrow = tomorrow.getMonth();
		EnumSeason seasonToday = EnumSeason.values()[monthToday];
		EnumSeason seasonTomorrow = EnumSeason.values()[monthTomorrow];
		byte dayToday = today.getDay();
		byte dayTomorrow = tomorrow.getDay();
		float dayTodayPercentage = (float)dayToday / (float)today.daysPerMonth;
		float dayTomorrowPercentage = (float)dayTomorrow / (float)today.daysPerMonth;
		float weekTodayPercentage = (float)(dayToday % 10) / 10F;
		float weekTomorrowPercentage = (float)(dayTomorrow % 10) / 10F;
		if (this.dayTemp == null)
		{
			this.dayTemp = new float[8];
			for (byte b = 0; b < 4; ++b)
			{
				this.dayTemp[b] = seasonToday.getTemperatureData().getTemperature(this.owner.rand, dayTodayPercentage, weekTodayPercentage, 0.25F * (b + 1));
				this.dayTemp[b + 4] = seasonTomorrow.getTemperatureData().getTemperature(this.owner.rand, dayTomorrowPercentage, weekTomorrowPercentage, 0.25F * (b + 1));
			}
		}
		else
		{
			for (byte b = 0; b < 4; ++b)
			{
				this.dayTemp[b] = this.dayTemp[b + 4];
				this.dayTemp[b + 4] = seasonTomorrow.getTemperatureData().getTemperature(this.owner.rand, dayTomorrowPercentage, dayTomorrowPercentage, 0.25F * (b + 1));
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
		return this.dayTimeKeeper.withTicks(this.getPersistentTicks() + this.dayTimeKeeper.ticksPerMonth);
	}

	@Override
	public EnumSeason getCurrentSeason()
	{
		return EnumSeason.values()[Math.min(this.today().getMonth(), 3)];
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
		float currentToNextProgress = (this.persistentTicks % ((float)c.ticksPerDay / 4F)) / ((float)c.ticksPerDay / 4F);
		float tempRet = this.baseTemp + tempCurrent * (1 - currentToNextProgress) + tempNext * currentToNextProgress;
		if (this.rainTicksRemaining > 0 && tempRet > 0)
		{
			tempRet *= 0.9F;
		}
		
		return tempRet;
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
		if (this.lastTime > 0 && time - this.lastTime > 0)
        {
            ticksSkipped = time - lastTime;
        }

        this.lastTime = time;
		this.setPersistentTicks(this.getPersistentTicks() + ticksSkipped);
		if (ticksSkipped >= 100)
		{
			final int skipped = (int) ticksSkipped;
			this.getWorld().playerEntities.forEach(e -> IExPPlayer.of(e).skipTicks(skipped));
		}
		
		if (ticksSkipped >= 24000)
		{
			ExPMisc.modLogger.warn("%d ticks were skipped by the world! This can cause issues.", ticksSkipped);
		}
		
		if (ticksSkipped >= 24000 || this.lastTime % 24000 > this.persistentTicks % 24000)
		{
			this.createDayData();
		}
		
		this.rainTicksRemaining -= ticksSkipped;
		
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
			boolean shouldSyncBrokenData = this.persistentTicks % 200 == 0;
			this.dayTemp_isDirty |= shouldSyncBrokenData;
			this.windDirection_isDirty |= this.windStrength_isDirty |= this.dayTemp_isDirty;
			this.serverIsDirty |= this.dayTemp_isDirty;
			boolean shouldBeRaining = this.owner.rand.nextDouble() <= this.accumulatedHumidity / 10000;
			
			if (this.rainTicksRemaining <= 0)
			{
				this.accumulatedHumidity += this.getWorld().rand.nextFloat() / 48000;
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

		if (!this.isRemote && this.serverIsDirty)
		{
			this.serverIsDirty = false;
            ExPNetwork.sendWorldData(this);
		}
	}

    @Override
    public IExPChunk getChunkDataAt(ChunkPos pos)
    {
        return chunkDataMap.getOrDefault(pos, ExPChunk.EMPTY);
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
			lst.appendTag(new NBTTagDouble(this.windDirection.x));
			lst.appendTag(new NBTTagDouble(this.windDirection.y));
			lst.appendTag(new NBTTagDouble(this.windDirection.z));
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

		ret.setLong("lastTime", this.lastTime);
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
			
			for (NBTBase nbtFloat : nbt.getTagList("dayTemp", NBT.TAG_FLOAT))
			{
				this.dayTemp[i++] = ((NBTTagFloat)nbtFloat).getFloat();
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

		this.lastTime = nbt.getLong("lastTime");
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

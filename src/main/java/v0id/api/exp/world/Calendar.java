package v0id.api.exp.world;

import net.minecraft.nbt.NBTTagLong;
import net.minecraftforge.common.util.INBTSerializable;

public class Calendar implements INBTSerializable<NBTTagLong>
{
	// These are MINECRAFT-time metrics!
	// Not real life!
	// Fyi, the ticksPerDay for real life metrics would be 1728000! 72 times bigger!
	public long ticksPerDay = 24000;
	public long daysPerMonth = 30;
	public long monthsPerYear = 4;
	public long hoursPerDay = 24;
	public long minutesPerHour = 60;
	public long secondsPerMinute = 60;
	public long ticksPerYear;
	public long ticksPerMonth;
	public long ticksPerHour;
	public float ticksPerMinute;
	public float ticksPerSecond;
	
	private long time;
	
	public Calendar()
	{
		this(0);
	}
	
	public Calendar(long time)
	{
		super();
		this.time = time;
		this.recalculateMetrics();
	}
	
	public Calendar withTicks(long time)
	{
		this.setTime(time);
		return this;
	}
	
	public short getYear()
	{
		return (short) (this.getTime() / (this.ticksPerDay * this.daysPerMonth * this.monthsPerYear));
	}
	
	public byte getMonth()
	{
		return (byte) ((this.getTime() % this.ticksPerYear) / this.ticksPerMonth);
	}
	
	public int getTotalMonth()
	{
		return (int) (this.getTime() / this.ticksPerMonth);
	}
	
	public byte getDay()
	{
		return (byte) ((this.getTime() % this.ticksPerMonth) / this.ticksPerDay);
	}
	
	public int getTotalDay()
	{
		return (int) (this.getTime() / this.ticksPerDay);
	}
	
	public short getDayTicks()
	{
		return (short) (this.getTime() % this.ticksPerDay);
	}
	
	public byte getHour()
	{
		return (byte) (this.time % this.ticksPerDay / this.ticksPerHour);
	}
	
	public long getTotalHour()
	{
		return this.time / this.ticksPerHour;
	}

	public void setTicksPerDay(long ticksPerDay)
	{
		this.ticksPerDay = ticksPerDay;
	}

	public void setDaysPerMonth(long daysPerMonth)
	{
		this.daysPerMonth = daysPerMonth;
	}

	public void setMonthsPerYear(long monthsPerYear)
	{
		this.monthsPerYear = monthsPerYear;
	}

	public void setHoursPerDay(long hoursPerDay)
	{
		this.hoursPerDay = hoursPerDay;
	}

	public void setMinutesPerHour(long minutesPerHour)
	{
		this.minutesPerHour = minutesPerHour;
	}

	public void setSecondsPerMinute(long secondsPerMinute)
	{
		this.secondsPerMinute = secondsPerMinute;
	}
	
	public byte getMinute()
	{
		return (byte) ((float)((this.time % this.ticksPerHour) - (this.getHour() * this.ticksPerHour)) / (float)this.minutesPerHour);
	}
	
	public byte getDate()
	{
		return (byte) (this.getTotalDay() % 7);
	}

	public long getTime()
	{
		return this.time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	@Override
	public NBTTagLong serializeNBT()
	{
		return new NBTTagLong(this.getTime());
	}

	@Override
	public void deserializeNBT(NBTTagLong nbt)
	{
		this.setTime(nbt.getLong());
	}
	
	public void recalculateMetrics()
	{
		this.ticksPerYear = this.ticksPerDay * this.daysPerMonth * this.monthsPerYear;
		this.ticksPerMonth = this.ticksPerDay * this.daysPerMonth;
		this.ticksPerHour = this.ticksPerDay / this.hoursPerDay;
		this.ticksPerMinute = this.ticksPerHour / this.minutesPerHour;
		this.ticksPerSecond = this.ticksPerSecond / this.secondsPerMinute;
	}
}

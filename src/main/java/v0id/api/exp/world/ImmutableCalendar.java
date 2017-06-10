package v0id.api.exp.world;

import net.minecraft.nbt.NBTTagLong;

public class ImmutableCalendar extends Calendar
{
	private final Calendar ref;
	
	private ImmutableCalendar()
	{
		this.ref = new Calendar();
	}
	
	private ImmutableCalendar(long time)
	{
		this.ref = new Calendar(time);
	}
	
	public ImmutableCalendar(Calendar c)
	{
		this.ref = c;
	}
	
	@Override
	public Calendar withTicks(long time)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setTime(long time)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void deserializeNBT(NBTTagLong nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void recalculateMetrics()
	{
		
	}
	
	@Override
	public short getYear()
	{
		return this.ref.getYear();
	}
	
	@Override
	public byte getMonth()
	{
		return this.ref.getMonth();
	}
	
	@Override
	public int getTotalMonth()
	{
		return this.ref.getTotalMonth();
	}
	
	@Override
	public byte getDay()
	{
		return this.ref.getDay();
	}
	
	@Override
	public int getTotalDay()
	{
		return this.ref.getTotalDay();
	}
	
	@Override
	public short getDayTicks()
	{
		return this.ref.getDayTicks();
	}
	
	@Override
	public byte getHour()
	{
		return this.ref.getHour();
	}
	
	@Override
	public long getTotalHour()
	{
		return this.ref.getTotalHour();
	}
	
	@Override
	public byte getMinute()
	{
		return this.ref.getMinute();
	}
	
	@Override
	public byte getDate()
	{
		return this.ref.getDate();
	}

	@Override
	public long getTime()
	{
		return this.ref.getTime();
	}
}

package v0id.api.exp.world;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class DailyTemperatureRange
{
	public TemperatureRange nightTemperature;
	public TemperatureRange dawnTemperature;
	public TemperatureRange dayTemperature;
	public TemperatureRange duskTemperature;
	
	public DailyTemperatureRange(TemperatureRange nightTemperature, TemperatureRange dawnTemperature, TemperatureRange dayTemperature, TemperatureRange duskTemperature)
	{
		super();
		this.setNightTemperature(nightTemperature);
		this.setDawnTemperature(dawnTemperature);
		this.setDayTemperature(dayTemperature);
		this.setDuskTemperature(duskTemperature);
	}
	
	public DailyTemperatureRange()
	{
		super();
	}

	public float getTemperature(Random rand, byte daytimeIndex)
	{
		switch (daytimeIndex)
		{
			case 1:
			{
				return this.getDawnTemperature().getTemperature(rand);
			}
			case 2:
			{
				return this.getDayTemperature().getTemperature(rand);
			}
			case 3:
			{
				return this.getDuskTemperature().getTemperature(rand);
			}
			default:
			{
				return this.getNightTemperature().getTemperature(rand);
			}
		}
	}
	
	public float getTemperature(Random rand, float dayCompletionPercentage)
	{
		return this.getTemperature(rand, this.getDaytimeIndex(dayCompletionPercentage));
	}
	
	public byte getDaytimeIndex(float dayPercentage)
	{
		float convinientDayIndex = dayPercentage * 4;
		if (convinientDayIndex < 0.5 || convinientDayIndex > 3.5)
		{
			return 0;
		}
		
		return (byte) MathHelper.clamp(Math.floor(convinientDayIndex), 0, 3);
	}

	public TemperatureRange getNightTemperature()
	{
		return this.nightTemperature;
	}

	public void setNightTemperature(TemperatureRange nightTemperature)
	{
		this.nightTemperature = nightTemperature;
	}

	public TemperatureRange getDawnTemperature()
	{
		return this.dawnTemperature;
	}

	public void setDawnTemperature(TemperatureRange dawnTemperature)
	{
		this.dawnTemperature = dawnTemperature;
	}

	public TemperatureRange getDayTemperature()
	{
		return this.dayTemperature;
	}

	public void setDayTemperature(TemperatureRange dayTemperature)
	{
		this.dayTemperature = dayTemperature;
	}

	public TemperatureRange getDuskTemperature()
	{
		return this.duskTemperature;
	}

	public void setDuskTemperature(TemperatureRange duskTemperature)
	{
		this.duskTemperature = duskTemperature;
	}
}

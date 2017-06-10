package v0id.api.exp.world;

import java.util.Random;

public class MonthlyTemperatureRange
{
	public DailyTemperatureRange beginningTemperature;
	public DailyTemperatureRange middleTemperature;
	public DailyTemperatureRange endTemperature;
	
	public MonthlyTemperatureRange(DailyTemperatureRange beginningTemperature, DailyTemperatureRange middleTemperature,	DailyTemperatureRange endTemperature)
	{
		super();
		this.setBeginningTemperature(beginningTemperature);
		this.setMiddleTemperature(middleTemperature);
		this.setEndTemperature(endTemperature);
	}
	
	public MonthlyTemperatureRange()
	{
		super();
	}

	public float getTemperature(Random rand, byte monthIndex, float dayCompletionPercentage)
	{
		return monthIndex == 0 ? this.getBeginningTemperature().getTemperature(rand, dayCompletionPercentage) : monthIndex == 1 ? this.getMiddleTemperature().getTemperature(rand, dayCompletionPercentage) : this.getEndTemperature().getTemperature(rand, dayCompletionPercentage);
	}
	
	public float getTemperature(Random rand, float monthPercentage, float dayPercentage)
	{
		return this.getTemperature(rand, (byte) Math.floor(monthPercentage * 3), dayPercentage);
	}

	public DailyTemperatureRange getBeginningTemperature()
	{
		return this.beginningTemperature;
	}

	public void setBeginningTemperature(DailyTemperatureRange beginningTemperature)
	{
		this.beginningTemperature = beginningTemperature;
	}

	public DailyTemperatureRange getMiddleTemperature()
	{
		return this.middleTemperature;
	}

	public void setMiddleTemperature(DailyTemperatureRange middleTemperature)
	{
		this.middleTemperature = middleTemperature;
	}

	public DailyTemperatureRange getEndTemperature()
	{
		return this.endTemperature;
	}

	public void setEndTemperature(DailyTemperatureRange endTemperature)
	{
		this.endTemperature = endTemperature;
	}
}

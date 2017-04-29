package v0id.api.exp.world;

import java.util.Random;

public class SeasonalTemperatureRange
{
	public MonthlyTemperatureRange beginningTemperature;
	public MonthlyTemperatureRange middleTemperature;
	public MonthlyTemperatureRange endTemperature;
	
	public SeasonalTemperatureRange(MonthlyTemperatureRange beginningTemperature, MonthlyTemperatureRange middleTemperature, MonthlyTemperatureRange endTemperature)
	{
		super();
		this.beginningTemperature = beginningTemperature;
		this.middleTemperature = middleTemperature;
		this.endTemperature = endTemperature;
	}

	public SeasonalTemperatureRange()
	{
		super();
	}
	
	public MonthlyTemperatureRange getBeginningTemperature()
	{
		return this.beginningTemperature;
	}

	public void setBeginningTemperature(MonthlyTemperatureRange beginningTemperature)
	{
		this.beginningTemperature = beginningTemperature;
	}

	public MonthlyTemperatureRange getMiddleTemperature()
	{
		return this.middleTemperature;
	}

	public void setMiddleTemperature(MonthlyTemperatureRange middleTemperature)
	{
		this.middleTemperature = middleTemperature;
	}

	public MonthlyTemperatureRange getEndTemperature()
	{
		return this.endTemperature;
	}

	public void setEndTemperature(MonthlyTemperatureRange endTemperature)
	{
		this.endTemperature = endTemperature;
	}

	public float getTemperature(Random rand, byte monthIndex, float monthPercentage, float dayPercentage)
	{
		return monthIndex == 0 ? this.getBeginningTemperature().getTemperature(rand, monthPercentage, dayPercentage) : monthIndex == 1 ? this.getMiddleTemperature().getTemperature(rand, monthPercentage, dayPercentage) : this.getEndTemperature().getTemperature(rand, monthPercentage, dayPercentage);
	}
	
	public float getTemperature(Random rand, float seasonPercentage, float monthPercentage, float dayPercentage)
	{
		return this.getTemperature(rand, (byte) Math.floor(seasonPercentage * 3), monthPercentage, dayPercentage);
	}
}

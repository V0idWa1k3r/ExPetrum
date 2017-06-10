package v0id.api.exp.world;

// This one is for JSON
public class YearlyTemperatureRange
{
	public static transient YearlyTemperatureRange instance;
	public SeasonalTemperatureRange winter;
	public SeasonalTemperatureRange spring;
	public SeasonalTemperatureRange summer;
	public SeasonalTemperatureRange autumn;
	
	public YearlyTemperatureRange()
	{
		
	}
	
	public static SeasonalTemperatureRange ofSeason(EnumSeason season)
	{
		switch (season)
		{
			case WINTER:
			{
				return instance.winter;
			}
			case SPRING:
			{
				return instance.spring;
			}
			case SUMMER:
			{
				return instance.summer;
			}
			default:
			{
				return instance.autumn;
			}
		}
	}
}

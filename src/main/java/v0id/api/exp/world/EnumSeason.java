package v0id.api.exp.world;

public enum EnumSeason
{
	SPRING,
	SUMMER,
	AUTUMN,
	WINTER;
	
	public SeasonalTemperatureRange getTemperatureData()
	{
		assert YearlyTemperatureRange.instance != null : "Wait until temperature data is initialized before requesting it!";
		return YearlyTemperatureRange.ofSeason(this);
	}
}

package v0id.api.exp.world;

public interface IBiome
{
	float getTemperatureMultiplier();
	
	float getHumidityMultiplier();
	
	float getTemperatureBaseModifier();
	
	float getHumidityBaseModifier();
	
	boolean isWaterSalt();
}

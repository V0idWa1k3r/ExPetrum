package v0id.api.exp.tile.crop;

import java.util.ArrayList;
import java.util.Map;

import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.TemperatureRange;

/**
 * Base stats for crops
 * @author V0idWa1k3r
 *
 */
public class CropData
{
	public EnumCrop crop;
	public TemperatureRange minimalTemperature;
	public TemperatureRange optimalTemperature;
	public TemperatureRange perfectTemperature;
	public TemperatureRange humidityRange;
	public int growthStages;
	public float growthRate;
	public float baseHealth;
	public EnumCropBug bug;
	public ArrayList<String> foundIn;
	public ArrayList<EnumSeason> harvestSeason;
	public EnumCropHarvestAction harvestAction;
	public Map<EnumPlantNutrient, Float> nutrientConsumption;
	public float waterConsumption;
	
	// For GSON
	public CropData()
	{
		
	}
}

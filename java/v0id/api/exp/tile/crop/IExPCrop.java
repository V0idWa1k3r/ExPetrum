package v0id.api.exp.tile.crop;

import java.util.EnumMap;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.TemperatureRange;

/**
 * Crop capability
 * @author V0idWa1k3r
 *
 */
public interface IExPCrop extends INBTSerializable<NBTTagCompound>
{
	/**
	 * Returns the tile entity this capability is attached to
	 * @return The tile entity this capability is attached to
	 */
	TileEntity getContainer();
	
	/**
	 * Unused
	 */
	void onTick();
	
	/**
	 * Happens at Block::updateTick. <br>
	 * Default implementation handles time changes.
	 */
	void onWorldTick();
	
	/**
	 * Gets the basic range of temperature this crop can survive at.
	 * @return The basic range of temperature this crop can survive at.
	 */
	TemperatureRange getSurvivalTemperature();
	
	/**
	 * Gets the range of temperature the crop is comfortable growing at.
	 * @return The range of temperature the crop is comfortable growing at.
	 */
	TemperatureRange getOptimalTemperature();
	
	/**
	 * Gets the range of temperature that is considered 'ideal' for this crop.
	 * @return The range of temperature that is considered 'ideal' for this crop.
	 */
	TemperatureRange getIdealTemperature();
	
	/**
	 * Gets the range of humidity in the soil the plant can survive at.
	 * @return A pair of min and max values
	 */
	Pair<Float, Float> getMoistureRange();
	
	/**
	 * Gets the generation of this crop. A generation is incremented when a player harvests a fully mature crop.
	 * @return The generation of this crop.
	 */
	int getGeneration();
	
	/**
	 * Is this a wild uncultivated crop? <br>
	 * Wild crops ignore all growing conditions but the {@link v0id.api.exp.tile.crop.IExPCrop#getSurvivalTemperature()}, can grow on dirt/grass directly but do so inefficiently.
	 * @return A boolean value indicating whether this crop is wild
	 */
	boolean isWild();
	
	/**
	 * Gets a list of drops this plant will drop when mature. This list is constructed dynamically and may change any time. <br>
	 * Non-mature crops will drop either nothing or their own seeds when harvested with bare hands.
	 * @return A list of drops this plant will drop when mature
	 */
	NonNullList<ItemStack> getMatureDrops();
	
	/**
	 * Happens when a player transfers this crop to a new location
	 * @param uprootedAt : the time this crop was uprooted.
	 */
	void handleTransfer(Calendar uprootedAt);
	
	/**
	 * Passed to the crop to check if it can grow at certain conditions.
	 * @param baseCanGrow : the base predicament, simply checks if the crop sees the sky/under UV light.
	 * @return A boolean value indicating whether this crop can grow under certain conditions.
	 */
	boolean canGrowOverride(boolean baseCanGrow);
	
	/**
	 * Gets the health of the crop
	 * @return
	 */
	float getHealth();
	
	/**
	 * Sets the health of the crop to a specified value. <br>
	 * Setting health to 0 will kill the crop.
	 * @param newValue : the value to set to
	 */
	void setHealth(float newValue);
	
	/**
	 * Gets the rate at which the plant consumes nutrients from the soil below. <br>
	 * If the soil contains no such nutrients the plant WILL loose health and die.
	 * @return A map of nutrient -> value
	 */
	EnumMap<EnumPlantNutrient, Float> getNutrientConsumption();
	
	/**
	 * Sets the consumption rate for a specific nutrient
	 * @param nutrient : the nutrient to set the consumption of
	 * @param newValue : the new value to set
	 */
	void setNutrientConsumption(EnumPlantNutrient nutrient, float newValue);
	
	/**
	 * Gets the rate at which this plant consumes water from the soil.
	 * @return The rate at which this plant consumes water from the soil.
	 */
	float getWaterConsumption();
	
	/**
	 * Sets the rate at which this plant consumes water from the soil.
	 * @param newValue : the value to set to
	 */
	void setWaterConsumption(float newValue);
	
	/**
	 * Gets the type of this crop
	 * @return The type of this crop
	 */
	EnumCrop getType();
	
	/**
	 * Gets the growth index of this crop. Used for blockstates only!
	 * @return The growth index of this crop.
	 */
	int getGrowthIndex();
	
	/**
	 * Gets the growth of this crop.
	 * @return The growth of this crop.
	 */
	float getGrowth();
	
	/**
	 * Sets the growth of this crop.
	 * @param newValue : the value to set
	 */
	void setGrowth(float newValue);
	
	/**
	 * Gets the type of bug that eats this crop.
	 * @return The type of bug that eats this crop.
	 */
	Optional<EnumCropBug> getBug();
	
	/**
	 * Is this crop being eaten by it's bug?
	 * @return A boolean value indicating whether this crop is being eaten.
	 */
	boolean isBeingEaten();
	
	/**
	 * Sets this crop's beingEaten value.
	 * @param b : the value to set
	 */
	void setBeingEaten(boolean b);
	
	/**
	 * Gets the harvest season for this crop. <br>
	 * The crop will still grow in any season, but it will only reach maturity in it's harvest season.
	 * @return
	 */
	EnumSeason getHarvestSeason();
	
	/**
	 * Determines what happens to the crop plant when it is harvested(right-clicked)
	 * @return - one of the valid EnumCropHarvestAction actions
	 */
	EnumCropHarvestAction getHarvestAction();
}

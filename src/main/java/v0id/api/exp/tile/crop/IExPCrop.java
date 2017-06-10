package v0id.api.exp.tile.crop;

import java.util.EnumMap;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	 * Non-mature crops will drop nothing when harvested with bare hands.
	 * @return A list of drops this plant will drop when mature
	 */
	NonNullList<ItemStack> getMatureDrops();
	
	/**
	 * Gets a list of seeds that this plant will drop. This list is constructed dynamically and may change any time. <br>
	 * For non-mature crops the list will be empty. <br>
	 * Seeds are harvested with a knife.
	 * @return A list of seeds this plant will drop
	 */
	NonNullList<ItemStack> getSeedDrops();
	
	/**
	 * Handles the harvest logic for crops. Exposed for any kind of auto-harvesters?
	 * @param harvester : the player that harvests the crop. May be null
	 * @param harvestedIn : the world the crop is in. May be null, then the world field from tileentity will be used
	 * @param harvestedAt : the position in the world the crop is at. May be null, then the position of the tileentity will be used
	 * @param selfBlockReference : the blockstate of the crop. May be null, then will be obtined from the world and position
	 * @param playerHarvestHand : the hand the player uses to harvest the crop. May be null, will default to player's active hand if the player is present.
	 * @param playerHarvestItem : the itemstack the player harvests the crop with. This for once may NOT be null
	 * @param isHarvestingWithRMB : is the player breaking the crop or harvesting it properly with right click?
	 * @return A list of drops obtained from the plant and a result indicating whether the harvest was a success(plant was mature and it's block state in the world was handled), fail(plant was not mature) or pass(there was no harvester to begin with as the plant died)
	 */
	Pair<EnumActionResult, NonNullList<ItemStack>> onHarvest(@Nullable EntityPlayer harvester, @Nullable World harvestedIn, @Nullable BlockPos harvestedAt, @Nullable IBlockState selfBlockReference, @Nullable EnumHand playerHarvestHand, ItemStack playerHarvestItem, boolean isHarvestingWithRMB);
	
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
	 * Checks if the given season is crop's harvest season
	 * @param season - the season to check
	 * @return
	 */
	boolean isHarvestSeason(EnumSeason season);
	
	/**
	 * Determines what happens to the crop plant when it is harvested(right-clicked)
	 * @return - one of the valid EnumCropHarvestAction actions
	 */
	EnumCropHarvestAction getHarvestAction();
	
	/**
	 * Damages the crop
	 * @param damage : the amount to damage by
	 */
	void causeDamage(float damage);
	
	/**
	 * Is the crop dead?
	 * @return a boolean value indicating whether this crop is dead.
	 */
	default boolean isDead()
	{
		return this.getHealth() <= 0 || this.getType() == EnumCrop.DEAD;
	}
	
	public static IExPCrop of(TileEntity tile, EnumFacing facing)
	{
		return tile.hasCapability(ExPCropCapability.cropCap, facing) ? tile.getCapability(ExPCropCapability.cropCap, facing) : null;
	}
	
	public static IExPCrop of(TileEntity tile)
	{
		return of(tile, null);
	}
}

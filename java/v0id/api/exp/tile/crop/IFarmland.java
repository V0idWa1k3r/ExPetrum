package v0id.api.exp.tile.crop;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Farmland capability for tiles.
 * @author V0idWa1k3r
 *
 */
public interface IFarmland extends INBTSerializable<NBTTagCompound>
{
	/**
	 * Gets the tileentity this capability is bound to
	 * @return
	 */
	TileEntity getContainer();
	
	/**
	 * Getter for the nutrient level of the farmland
	 * @param nut : the nutrient to get the level of
	 * @return - The nutrient level for appropriate nutrient
	 */
	float getNutrient(EnumPlantNutrient nut);
	
	/**
	 * Setter for the nutrient level of the farmland
	 * @param nut : the nutrient to set the level of
	 * @param newVal : the value to set to
	 */
	void setNutrient(EnumPlantNutrient nut, float newVal);
	
	/**
	 * Getter for the moisture level of the farmland
	 * @return - The moisture level [0-1]
	 */
	float getMoisture();
	
	/**
	 * Setter for the moisture level of the farmland
	 * @param newVal : the value to set
	 */
	void setMoisture(float newVal);
	
	/**
	 * Getter for the growth rate multiplier
	 * @return - The growth rate multiplier
	 */
	float getGrowthMultiplier();
	
	/**
	 * Handles the world ticking
	 */
	void onWorldTick();
	
	/**
	 * Marks the farmland for sync
	 */
	void setDirty();
	
	public static IFarmland of(TileEntity tile, EnumFacing facing)
	{
		return tile.hasCapability(ExPFarmlandCapability.farmlandCap, facing) ? tile.getCapability(ExPFarmlandCapability.farmlandCap, facing) : null;
	}
	
	public static IFarmland of(TileEntity tile)
	{
		return of(tile, null);
	}
}

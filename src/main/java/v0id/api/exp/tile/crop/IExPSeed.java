package v0id.api.exp.tile.crop;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Seeds item capability.
 * @author V0idWa1k3r
 *
 */
public interface IExPSeed extends INBTSerializable<NBTTagCompound>
{
	/**
	 * Getter for the ItemStack this capability belongs to.
	 * @return - The ItemStack this capability belongs to.
	 */
	ItemStack getContainer();
	
	/**
	 * Getter for the crop these seeds belong to.
	 * @return - The crop these seeds belong to.
	 */
	EnumCrop getCrop();
	
	/**
	 * Setter for the crop these seeds belong to.
	 * @param crop : the new value
	 */
	void setCrop(EnumCrop crop);
	
	/**
	 * Getter for the generation of these seeds.
	 * @return - The generation of these seeds.
	 */
	int getGeneration();
	
	/**
	 * Setter for the generation of these seeds.
	 * @param newVal : the value to set
	 */
	void setGeneration(int newVal);
	
	/**
	 * Getter for additional crop data these seeds carry (the stats of the crop)
	 * @return - an NBTTagCompound containing additional crop data
	 */
	NBTTagCompound getExtendedData();
	
	/**
	 * Setter for additional crop data these seeds carry (the stats of the crop)
	 * @param tag : the value to set
	 */
	void setExtendedData(NBTTagCompound tag);
}

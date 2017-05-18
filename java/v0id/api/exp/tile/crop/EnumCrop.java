package v0id.api.exp.tile.crop;

import net.minecraft.util.IStringSerializable;

public enum EnumCrop implements IStringSerializable
{
	DEAD,
	
	// Grains
	MAIZE,
	RICE,
	WHEAT,
	BARLEY,
	SORGHUM,
	MILLET,
	OAT,
	RYE,
	
	// Vegetables
	CABBAGE,
	TURNIP,
	RADISH,
	CARROT,
	PARSNIP,
	BEETROOT,
	LETTUCE,
	BEANS,
	PEAS,
	POTATO,
	EGGPLANT,
	TOMATO,
	CUCUMBER,
	PUMPKIN,
	ONION,
	GARLIC,
	LEEK,
	PEPPER,
	SPINACH,
	SWEET_POTATO,
	CASSAVA;

	EnumCrop()
	{
		
	}
	
	/**
	 * Loaded at mod's PreInitialization stage. Be aware.
	 */
	private CropData data;
	
	public CropData getData()
	{
		return this.data;
	}
	
	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

	public void setData(CropData data)
	{
		this.data = data;
	}
}

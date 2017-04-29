package v0id.api.exp.block.property;

import static v0id.api.exp.block.EnumGrassAmount.*;

import net.minecraft.util.IStringSerializable;
import v0id.api.exp.block.EnumGrassAmount;

public enum EnumDirtClass implements IStringSerializable
{
	ACRISOL(0.2, 1.5, 0.3, LESS), 		//Clay-rich
	ALBELUVISOL(0.2, 0.3, 0.1, LESSER), //Useless
	CALCISOL(1, 0.5, 0.8, NORMAL),		//Weak if not irrigated
	CAMBISOL(0.8, 0.8, 0.4, NORMAL),	//Young soils, good but no nutrients have formed
	CHERNOZEM(1.5, 0.7, 2.0, GREATER),	//Perfect for crops
	FLUVISOL(1.1, 1, 1, MORE),			//Pretty good
	GLEYSOL(0.7, 1.9, 0.4, MORE),		//Very wet, grow lush vegetation
	GYPSISOL(0.6, 0.1, 1, LESS),		//Source of Gypsum
	PEAT(0.7, 1, 0.5, MORE),			//Substitute for coal
	LEPTOSOL(0.5, 0.01, 1, LESS),		//Very dry, can't even absorb water properly
	PLANOSOL(0.7, 0.7, 0.7, NORMAL),	//Pretty much a weaker standard soil
	PODZOL(0.8, 0.2, 0.2, LESS),		//Bad for farming... You thought it was good, didn't you? Wiki quote: "Most podzols are poor soils for agriculture due to the sandy portion, resulting in a low level of moisture and nutrients"
	REGOSOL(1, 0.7, 0.6, NORMAL),		//Pretty much default dirt with tweaks
	SOLONCHAK(0.1, 0.1, 0.8, LESSER),	//Salt
	UMBRISOL(0.2, 1, 0.7, NORMAL),		//Opposite of good farming soils!
	VERTISOL(0.7, 1.3, 0.2, NORMAL);	//A weird one.. 
	
	EnumDirtClass(double d, double d1, double d2, EnumGrassAmount amt)
	{
		this.setGrowthMultiplier((float) d);
		this.setHumidity((float) d1);
		this.setNutrientMultiplier((float) d2);
		this.amount = amt;
	}
	
	private float growthMultiplier;
	private float humidity;
	private float nutrientMultiplier;
	private EnumGrassAmount amount;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

	public float getGrowthMultiplier()
	{
		return this.growthMultiplier;
	}

	public void setGrowthMultiplier(float growthMultiplier)
	{
		this.growthMultiplier = growthMultiplier;
	}

	public float getHumidity()
	{
		return this.humidity;
	}

	public void setHumidity(float humidity)
	{
		this.humidity = humidity;
	}

	public float getNutrientMultiplier()
	{
		return this.nutrientMultiplier;
	}

	public void setNutrientMultiplier(float nutrientMultiplier)
	{
		this.nutrientMultiplier = nutrientMultiplier;
	}

	public EnumGrassAmount getAmount()
	{
		return this.amount;
	}

	public void setAmount(EnumGrassAmount amount)
	{
		this.amount = amount;
	}
}

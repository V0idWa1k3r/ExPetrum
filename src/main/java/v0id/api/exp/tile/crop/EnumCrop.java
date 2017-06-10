package v0id.api.exp.tile.crop;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.IStringSerializable;

public enum EnumCrop implements IStringSerializable
{
	DEAD(-1, -1),
	
	// Grains
	MAIZE(80, 120, true),
	RICE(80, 120, true),
	WHEAT(80, 120, true),
	BARLEY(80, 120, true),
	SORGHUM(80, 120, true),
	MILLET(80, 120, true),
	OAT(80, 120, true),
	RYE(80, 120, true),
	
	// Vegetables
	CABBAGE(800, 1600),
	TURNIP(400, 1200),
	RADISH(300, 1000),
	CARROT(500, 700),
	PARSNIP(300, 400),
	BEETROOT(400, 600),
	LETTUCE(1000, 3000),
	BEANS(160, 270),
	PEAS(140, 300),
	POTATO(200, 600),
	EGGPLANT(500, 1000),
	TOMATO(300, 2000),
	CUCUMBER(200, 300),
	PUMPKIN(1000, 4000),
	ONION(300, 400),
	GARLIC(80, 160),
	LEEK(90, 150),
	PEPPER(500, 1300),
	SPINACH(300, 2000),
	SWEET_POTATO(200, 600),
	CASSAVA(100, 300);
	
	EnumCrop(float min, float max)
	{
		this(min, max, false);
	}
	
	EnumCrop(float min, float max, boolean grain)
	{
		this.dropWeight = Pair.of(min, max);
		this.isGrain = grain;
	}
	
	private CropData data;
	private final Pair<Float, Float> dropWeight;
	private final boolean isGrain;
	
	public CropData getData()
	{
		return this.data;
	}
	
	public boolean isGrain()
	{
		return this.isGrain;
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
	
	public final boolean nequals(Object other) 
	{
        return !this.equals(other);
    }

	public Pair<Float, Float> getDropWeight()
	{
		return this.dropWeight;
	}
}

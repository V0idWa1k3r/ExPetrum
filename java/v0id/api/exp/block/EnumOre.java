package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumOre implements IStringSerializable
{
	// Silver
	ACANTHITE("Ag2S", 0, 0x8c8a7e),	
	
	// Barium
	BARITE("BaSO4", 1, 0xdfad94),	
	
	// Aluminum
	BAUXITE("Al(OH)3", 2, 0x5c2425),			
	
	// Gemstones. Has subtypes in the ore
	BERYL("Be3Al2(SiO3)6", 0, -1),		
	
	// Copper
	BORNITE("Cu5FeS4", 1, 0x6c6d43),		
	
	// Tin
	CASSITERITE("SnO2", 0, 0x222222),		
	
	// Copper
	CHALCOCITE("Cu2S", 0, 0x0f0f0f),		
	
	// Copper
	CHALCOPYRITE("CuFeS2", 0, 0xdce329),		
	
	// Chromium
	CHROMITE("FeCr2O4", 1, 0xe4e7d2),	
	
	// Mercury
	CINNABAR("HgS", 1, 0xe30000),			
	
	// Cobalt
	COBALTITE("CoAsS", 1, 0xa3856b),			
	
	// Tantalum
	COLTAN("FeTa2O6", 0, 0xf1b249),			
	
	// Magnesium
	DOLOMITE("CaMg(CO3)2", 1, 0xe6d7c0),		
	
	// Lead
	GALENA("PbS", 1, 0x202739),		
	
	// Gold
	GOLD("Au", 1, 0xdab950),			
	
	// Iron
	HEMATITE("Fe2O3", 0, 0xa4584a),			
	
	// Titanium
	ILMENITE("FeTiO3", 1, 0x644530),	
	
	// Iron
	MAGNETITE("Fe3O4", 0, 0x422329),		
	
	// Copper
	MALACHITE("Cu2CO3(OH)2", 0, 0x05a67a), 	
	
	// Molybdenum
	MOLYBDENITE("MoS2", 1, 0x58677c),		
	
	// Nickel + Iron
	PENTLANDITE("(FeNi)9S8", 1, 0x696e48),	
	
	// Manganese
	PYROLUSITE("MnO2", 1, 0x180023),			
	
	// Tungsten
	SCHEELITE("CaWO4", 0, 0x916002),			
	
	// Platinum
	SPERRYLITE("PtAs2", 0, 0x4c4239),		
	
	// Zinc
	SPHALERITE("ZnS", 0, 0x190a0d),			
	
	// Uranium
	URANINITE("UO2", 1, 0x000000),			
	
	// Tungsten
	WOLFRAMITE("FeWO4", 1, 0x64661b),
	
	// Coal
	LIGNITE("C", 0, 0x2c0d01),
	
	// Coal
	BITUMINOUS_COAL("C", 1, 0x555555),
	
	// Still coal, but this one can be used for steel!
	ANTHRACITE("C", 1, 0x83644e),
	
	// Still coa... Nah, kidding! It's graphite!
	// Though technically it IS coal
	GRAPHITE("C", 0, 0x3a2b20);
	
	EnumOre(String s, int i, int j)
	{
		this.formula = s;
		this.textureIndex = i;
		this.color = j;
	}
	
	// TBD
	private final String formula;
	
	/**
	 * 0 - Stripe
	 * 1 - Dense colorings
	 * 2 - Pretty much bauxite only
	 */
	private final int textureIndex;
	
	private final int color;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

	public int getTextureIndex()
	{
		return this.textureIndex;
	}

	public int getColor()
	{
		return this.color;
	}

	public String getFormula()
	{
		return this.formula;
	}
}

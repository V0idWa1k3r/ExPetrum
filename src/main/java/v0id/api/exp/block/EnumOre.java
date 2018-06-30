package v0id.api.exp.block;

import com.google.common.collect.Lists;
import net.minecraft.util.IStringSerializable;
import v0id.api.exp.event.world.gen.OreEntry;
import v0id.api.exp.metal.EnumMetal;

import java.util.List;

public enum EnumOre implements IStringSerializable
{
	// Silver
	ACANTHITE("Ag2S", 0, 0x8c8a7e, 30, EnumMetal.SILVER, 845F),
	
	// Barium
	BARITE("BaSO4", 1, 0xdfad94, 8, EnumMetal.BARIUM, 1580F),
	
	// Aluminum
	BAUXITE("Al(OH)3", 2, 0x5c2425, 30, EnumMetal.ALUMINIUM, 2040F),
	
	// Gemstones. Has subtypes in the ore
	BERYL("Be3Al2(SiO3)6", 0, -1, 10, null, 0F),
	
	// Copper
	BORNITE("Cu5FeS4", 1, 0x6c6d43, 60, EnumMetal.COPPER, 600F),
	
	// Tin
	CASSITERITE("SnO2", 0, 0x222222, 190, EnumMetal.TIN, 1127F),
	
	// Copper
	CHALCOCITE("Cu2S", 0, 0x0f0f0f, 60, EnumMetal.COPPER, 600F),
	
	// Copper
	CHALCOPYRITE("CuFeS2", 0, 0xdce329, 30, EnumMetal.COPPER, 600F),
	
	// Chromium
	CHROMITE("FeCr2O4", 1, 0xe4e7d2, 8, EnumMetal.CHROMIUM, 2270F),
	
	// Mercury
	CINNABAR("HgS", 1, 0xe30000, 10, null, 0),
	
	// Cobalt
	COBALTITE("CoAsS", 1, 0xa3856b, 6, EnumMetal.COBALT, 1495F),
	
	// Tantalum
	COLTAN("FeTa2O6", 0, 0xf1b249, 6, EnumMetal.TANTALUM, 2996F),
	
	// Magnesium
	DOLOMITE("CaMg(CO3)2", 1, 0xe6d7c0, 10, EnumMetal.MAGNESIUM, 2570F),
	
	// Lead
	GALENA("PbS", 1, 0x202739, 30, EnumMetal.LEAD, 327F),
	
	// Gold
	GOLD("Au", 1, 0xdab950, 20, EnumMetal.GOLD, 1064F),
	
	// Iron
	HEMATITE("Fe2O3", 0, 0xa4584a, 60, EnumMetal.IRON, 1538F),
	
	// Titanium
	ILMENITE("FeTiO3", 1, 0x644530, 6, EnumMetal.TITANIUM, 1050F),
	
	// Iron
	MAGNETITE("Fe3O4", 0, 0x422329, 50, EnumMetal.IRON, 1538F),
	
	// Copper
	MALACHITE("Cu2CO3(OH)2", 0, 0x05a67a, 40, EnumMetal.COPPER, 600F),
	
	// Molybdenum
	MOLYBDENITE("MoS2", 1, 0x58677c, 10, EnumMetal.MOLYBDENUM, 2623F),
	
	// Nickel + Iron
	PENTLANDITE("(FeNi)9S8", 1, 0x696e48, 10, EnumMetal.NICKEL, 1100F),
	
	// Manganese
	PYROLUSITE("MnO2", 1, 0x180023, 15, EnumMetal.MANGANESE, 535F),
	
	// Tungsten
	SCHEELITE("CaWO4", 0, 0x916002, 10, EnumMetal.TUNGSTEN, 3422F),
	
	// Platinum
	SPERRYLITE("PtAs2", 0, 0x4c4239, 10, EnumMetal.PLATINUM, 2041F),
	
	// Zinc
	SPHALERITE("ZnS", 0, 0x190a0d, 30, EnumMetal.ZINC, 1850F),
	
	// Uranium
	URANINITE("UO2", 1, 0x000000, 10, EnumMetal.URANIUM, 1132F),
	
	// Tungsten
	WOLFRAMITE("FeWO4", 1, 0x64661b, 10, EnumMetal.TUNGSTEN, 3422F),
	
	// Coal
	LIGNITE("C", 0, 0x2c0d01, 60, null, 0F),
	
	// Coal
	BITUMINOUS_COAL("C", 1, 0x555555, 30, null, 0F),
	
	// Still coal, but this one can be used for steel!
	ANTHRACITE("C", 1, 0x83644e, 20, null, 0F),
	
	// Still coa... Nah, kidding! It's graphite!
	// Though technically it IS coal
	GRAPHITE("C", 0, 0x3a2b20, 10, null, 0F),
	
	// Bismuthinite
	BISMUTHINITE("Bi2S3", 0, 0xb0aa94, 50, EnumMetal.BISMUTH, 271F);
	
	EnumOre(String s, int i, int j, int w, EnumMetal m, float f)
	{
		this.formula = s;
		this.textureIndex = i;
		this.color = j;
		this.weight = w;
		this.meltingTemperature = f;
		this.meltsInto = m;
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
	
	private final int weight;

	private final EnumMetal meltsInto;

	private final float meltingTemperature;
	
	// If you are adding new ores add them to this list manualy
	public static final List<OreEntry> worldgenEntries = Lists.newArrayList();

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
	
	public static void registerWorldgen(EnumOre ore)
	{
		worldgenEntries.add(new OreEntry(ore, ore.weight));
	}

    public EnumMetal getMeltsInto()
    {
        return meltsInto;
    }

    public float getMeltingTemperature()
    {
        return meltingTemperature;
    }
}

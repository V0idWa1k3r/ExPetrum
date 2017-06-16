package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumTreeType implements IStringSerializable
{
	KALOPANAX,
	BIRCH,
	ACACIA,
	CHESTNUT,
	OAK,
	HICKORY,
	BAOBAB,
	KAPOK,
	EUCALYPTUS,
	ASH,
	WILLOW,
	MAPLE,
	REDWOOD,
	FIR(true),
	CEDAR(true),
	SPRUCE(true),
	PINE(true),
	ELM,
	PALM,
	TEAK,
	ASPEN,
	ROWAN,
	MANGROVE,
	TUPELO,
	PARROTIA,
	SWEETGUM,
	JACKWOOD,
	TSUGA(true),
	VIBURNUM,
	KOELREUTERIA,
    APPLE,
    OLIVE,
    PEACH,
    ORANGE,
    PEAR,
    PLUM,
    BANANA,
    LEMON,
    APRICOT,
    WALNUT,
    CHERRY,
    POMEGRANATE,
    GRAPEFRUIT,
    AVOCADO,
    CARAMBOLA;
	
	EnumTreeType(boolean b)
	{
		this.evergreen = b;
	}
	
	EnumTreeType()
	{
		this(false);
	}
	
	public static EnumTreeType[][] typesForIndex = new EnumTreeType[9][5];

	private final boolean evergreen;
	
	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

	public boolean isEvergreen()
	{
		return this.evergreen;
	}
	
}

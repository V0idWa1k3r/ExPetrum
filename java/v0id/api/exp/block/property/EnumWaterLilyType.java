package v0id.api.exp.block.property;

import net.minecraft.util.IStringSerializable;

public enum EnumWaterLilyType implements IStringSerializable
{
	LEOPARDESS(false),
	ANTARES(true),
	STURTEVANTII(true),
	RED_FLARE(true),
	BLUE_BEAUTY(false);
	
	EnumWaterLilyType(boolean nightBloomer)
	{
		this.nightBloomer = nightBloomer;
	}
	
	public boolean isNightBloomer()
	{
		return this.nightBloomer;
	}

	private final boolean nightBloomer;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
}

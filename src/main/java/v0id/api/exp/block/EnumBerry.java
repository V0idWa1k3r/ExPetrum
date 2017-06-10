package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumBerry implements IStringSerializable
{
	BLACKBERRY,
	CLOUDBERRY,
	RASPBERRY,
	BLUEBERRY,
	CRANBERRY,
	ELDERBERRY,
	GOOSEBERRY,
	HACKBERRY,
	STRAWBERRY,
	WOLFBERRY,
	SALMONBERRY,
	CROWBERRY;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
}

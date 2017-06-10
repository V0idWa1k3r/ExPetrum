package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumGrassAmount implements IStringSerializable
{
	GREATER,
	MORE,
	NORMAL,
	LESS,
	LESSER;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
}

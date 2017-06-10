package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumLeafState implements IStringSerializable
{
	NORMAL,
	AUTUMN,
	DEAD;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
}

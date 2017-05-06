package v0id.api.exp.item;

import net.minecraft.util.IStringSerializable;

public enum EnumToolhead implements IStringSerializable
{
	HAMMER_STONE,
	AXE_STONE,
	SHOVEL_STONE,
	SPEAR_STONE,
	KNIFE_STONE,
	CHISEL_STONE,
	ARROW_STONE;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

}

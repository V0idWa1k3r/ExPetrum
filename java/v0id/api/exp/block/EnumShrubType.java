package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumShrubType implements IStringSerializable
{
	SPOTTED_LAUREL,
	BOX,
	CERCIS_CANADENSIS,
	CHAMAEROPS,
	VARIEGATED_DOGWOOD,
	CORNUS_KOUSA,
	ELAEAGNUS,
	EUONYMUS,
	EUONYMUS_JAPONICUS,
	KAPUKA,
	ILEX,
	LAURUS_NOBILIS,
	MAHONIA_X_MEDIA,
	RED_ROBIN,
	WHIN,
	PRUNUS;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
}

package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumMoltenMetalState implements IStringSerializable
{
    NORMAL,
    INVALID,
    SOLID;

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }
}

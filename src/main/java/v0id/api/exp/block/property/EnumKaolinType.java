package v0id.api.exp.block.property;

import net.minecraft.util.IStringSerializable;

public enum EnumKaolinType implements IStringSerializable
{
    ROCK,
    BRICK,
    FIRE_BRICK,
    IRON_PLATED_FIRE_BRICK;

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }
}

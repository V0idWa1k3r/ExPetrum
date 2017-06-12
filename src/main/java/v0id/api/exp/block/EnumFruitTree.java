package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

/**
 * Created by V0idWa1k3r on 12-Jun-17.
 */
public enum EnumFruitTree implements IStringSerializable
{
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

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }
}

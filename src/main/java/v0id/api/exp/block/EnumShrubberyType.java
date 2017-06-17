package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

/**
 * Created by V0idWa1k3r on 17-Jun-17.
 */
public enum EnumShrubberyType implements IStringSerializable
{
    TROPICAL,
    FLOWER,
    SMALL_SHRUB,
    MUSHROOM,
    VARYING_GRASS,
    TALL_FLOWER;


    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }
}

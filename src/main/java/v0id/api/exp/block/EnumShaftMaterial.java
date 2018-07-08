package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;

public enum EnumShaftMaterial implements IStringSerializable
{
    WOOD(2048, 3),
    COPPER(8192, 5),
    BRONZE(32768, 5),
    IRON(131072, 7),
    STEEL(524288, 9),
    HSLA(2097152, 12);

    EnumShaftMaterial(float maxPower, int maxLength)
    {
        this.maxPower = maxPower;
        this.maxLength = maxLength;
    }

    private final float maxPower;
    private final int maxLength;

    public float getMaxPower()
    {
        return maxPower;
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }
}

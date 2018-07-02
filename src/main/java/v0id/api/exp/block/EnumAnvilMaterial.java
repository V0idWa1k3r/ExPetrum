package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;
import v0id.api.exp.metal.EnumMetal;

public enum EnumAnvilMaterial implements IStringSerializable
{
    STONE(-1, null),
    COPPER(0, EnumMetal.COPPER),
    BRONZE(1, EnumMetal.BRONZE),
    BISMUTH_BRONZE(1, EnumMetal.BISMUTH_BRONZE),
    HIGH_LEADED_BRONZE(1, EnumMetal.HIGH_LEADED_BRONZE),
    MANGANESE_BRONZE(1, EnumMetal.MANGANESE_BRONZE),
    ALUMINUM_BRONZE(1, EnumMetal.ALUMINUM_BRONZE),
    IRON(2, EnumMetal.IRON),
    STEEL(3, EnumMetal.STEEL),
    STAINLESS_STEEL(3, EnumMetal.STAINLESS_STEEL),
    HSLA_STEEL(4, EnumMetal.HSLA_STEEL),
    TOOL_STEEL(4, EnumMetal.TOOL_STEEL);

    EnumAnvilMaterial(int tier, EnumMetal metal)
    {
        this.tier = tier;
        this.metal = metal;
    }

    private final int tier;
    private final EnumMetal metal;

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }

    public int getColor()
    {
        return this.metal == null ? 0x5e5e5e : this.metal.getColor();
    }

    public EnumMetal getMetal()
    {
        return this.metal;
    }

    public int getTier()
    {
        return this.tier;
    }
}

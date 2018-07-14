package v0id.api.exp.item;

import v0id.api.exp.metal.EnumMetal;

public enum EnumArmorStats
{
    COPPER("copper", 365, 7, 0, 20, EnumMetal.COPPER),
    LEATHER("leather", 180, 6, 2, 5, null),
    CLOTH("cloth", 120, 4, 2, 1, null),
    BRONZE("bronze", 660, 8, 0, 20, EnumMetal.BRONZE),
    BISMUTH_BRONZE("bismuth_bronze", 840, 9, 0, 20, EnumMetal.BISMUTH_BRONZE),
    LEADED_BRONZE("leaded_bronze", 540, 8, 0, 20, EnumMetal.HIGH_LEADED_BRONZE),
    MANGANESE_BRONZE("manganese_bronze", 1020, 8, 0, 20, EnumMetal.MANGANESE_BRONZE),
    ROSE_ALLOY("rose_alloy", 610, 8, 1, 20, EnumMetal.ROSE_ALLOY),
    IRON("iron", 1530, 14, 2, 20, EnumMetal.IRON),
    STEEL("steel", 3072, 18, 4, 20, EnumMetal.STEEL),
    TOOL_STEEL("tool_steel", 9216, 28, 8, 20, EnumMetal.TOOL_STEEL);

    EnumArmorStats(String name, int durability, float resistance, float toughness, float weight, EnumMetal associatedMetal)
    {
        this.name = name;
        this.durability = durability;
        this.resistance = resistance;
        this.toughness = toughness;
        this.weight = weight;
        this.associatedMetal = associatedMetal;
    }

    public final String name;
    public final int durability;
    public final float resistance;
    public final float toughness;
    public final float weight;
    public final EnumMetal associatedMetal;
}

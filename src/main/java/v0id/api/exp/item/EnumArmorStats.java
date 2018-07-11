package v0id.api.exp.item;

public enum EnumArmorStats
{
    COPPER("copper", 365, 7, 0),
    LEATHER("leather", 180, 6, 2),
    CLOTH("cloth", 120, 4, 2),
    BRONZE("bronze", 660, 8, 0),
    BISMUTH_BRONZE("bismuth_bronze", 840, 9, 0),
    LEADED_BRONZE("leaded_bronze", 540, 8, 0),
    MANGANESE_BRONZE("manganese_bronze", 1020, 8, 0),
    ROSE_ALLOY("rose_alloy", 610, 8, 1),
    IRON("iron", 1530, 14, 2),
    STEEL("steel", 3072, 18, 4),
    TOOL_STEEL("tool_steel", 9216, 28, 8);

    EnumArmorStats(String name, int durability, float resistance, float toughness)
    {
        this.name = name;
        this.durability = durability;
        this.resistance = resistance;
        this.toughness = toughness;
    }

    public final String name;
    public final int durability;
    public final float resistance;
    public final float toughness;
}

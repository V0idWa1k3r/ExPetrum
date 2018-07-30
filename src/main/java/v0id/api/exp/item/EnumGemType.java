package v0id.api.exp.item;

public enum EnumGemType
{
    AMETHYST(0xa86ec5, "gemAmethyst"),
    AQUAMARINE(0x98e3f8, "gemAquamarine"),
    DIAMOND(0xb8c6d3, "gemDiamond"),
    EMERALD(0x047c3c, "gemEmerald"),
    LAPIS(0x2a3de1, "gemLapisLazuli", "gemLapis"),
    ONYX(0x171717, "gemOnyx"),
    OPAL(0x537485, "gemOpal"),
    PERIDOT(0x3a9901, "gemPeridot"),
    RUBY(0x680508, "gemRuby"),
    SAPPHIRE(0x051465, "gemSapphire"),
    TOPAZ(0xfd8805, "gemTopaz"),
    TOURMALINE(0x036800, "gemTourmaline"),
    TURQUOISE(0x62d7eb, "gemTurquoise"),
    ZIRCON(0x387a90, "gemZircon");

    EnumGemType(int color, String... oreDictNames)
    {
        this.color = color;
        this.oreDictNames = oreDictNames;
    }

    private final int color;
    private final String[] oreDictNames;

    public String[] getOreDictNames()
    {
        return oreDictNames;
    }

    public int getColor()
    {
        return color;
    }
}

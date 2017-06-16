package v0id.api.exp.block;

import net.minecraft.util.IStringSerializable;
import v0id.api.exp.item.food.FoodEntry;

/**
 * Created by V0idWa1k3r on 12-Jun-17.
 */
public enum EnumFruit implements IStringSerializable
{
    APPLE(EnumTreeType.APPLE, FoodEntry.APPLE, 150, 1000),
    OLIVE(EnumTreeType.OLIVE, FoodEntry.OLIVE, 80, 250),
    PEACH(EnumTreeType.PEACH, FoodEntry.PEACH, 100, 1000),
    ORANGE(EnumTreeType.ORANGE, FoodEntry.ORANGE, 200, 1200),
    PEAR(EnumTreeType.PEAR, FoodEntry.PEAR, 80, 800),
    PLUM(EnumTreeType.PLUM, FoodEntry.PLUM, 60, 1200),
    BANANA(EnumTreeType.BANANA, FoodEntry.BANANA, 110, 350),
    LEMON(EnumTreeType.LEMON, FoodEntry.LEMON, 160, 1000),
    APRICOT(EnumTreeType.APRICOT, FoodEntry.APRICOT, 100, 750),
    WALNUT(EnumTreeType.WALNUT, FoodEntry.WALNUT, 10, 80),
    CHERRY(EnumTreeType.CHERRY, FoodEntry.CHERRY, 80, 500),
    POMEGRANATE(EnumTreeType.POMEGRANATE, FoodEntry.POMEGRANATE, 120, 870),
    GRAPEFRUIT(EnumTreeType.GRAPEFRUIT, FoodEntry.GRAPEFRUIT, 300, 800),
    AVOCADO(EnumTreeType.AVOCADO, FoodEntry.AVOCADO, 60, 360),
    CARAMBOLA(EnumTreeType.CARAMBOLA, FoodEntry.CARAMBOLA, 200, 1000);

    EnumFruit(EnumTreeType ett, FoodEntry e, int i, int i1)
    {
        this.associatedTreeType = ett;
        this.associatedEntry = e;
        this.weightMin = i;
        this.weightMax = i1;
        ett.setAssociatedFruit(this);
    }

    public EnumTreeType getAssociatedTreeType()
    {
        return associatedTreeType;
    }

    public FoodEntry getAssociatedEntry()
    {
        return associatedEntry;
    }

    public void setAssociatedEntry(FoodEntry associatedEntry)
    {
        this.associatedEntry = associatedEntry;
    }

    public FoodEntry associatedEntry;

    private final EnumTreeType associatedTreeType;

    public int getWeightMin()
    {
        return weightMin;
    }

    public int getWeightMax()
    {
        return weightMax;
    }

    private final int weightMin, weightMax;

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }
}

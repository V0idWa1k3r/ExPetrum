package v0id.api.exp.block;

import com.google.common.collect.Lists;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by V0idWa1k3r on 17-Jun-17.
 */
public enum EnumShrubberyType implements IStringSerializable
{
    TROPICAL,
    FLOWER,
    SMALL_SHRUB,
    MUSHROOM;

    @Override
    public String getName()
    {
        return this.name().toLowerCase();
    }

    public static EnumShrubberyType chooseType(Biome currentBiome, Random rand)
    {
        int weightTropical = 0;
        int weightFlower = 10;
        int weightShrub = 10;
        int weightMushroom = 10;
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(currentBiome);
        if (types.contains(BiomeDictionary.Type.JUNGLE))
        {
            weightTropical += 60;
            weightFlower += 10;
            weightMushroom -= 8;
            weightShrub += 20;
        }

        if (types.contains(BiomeDictionary.Type.HOT))
        {
            weightShrub += 5;
            weightMushroom -= 10;
        }

        if (types.contains(BiomeDictionary.Type.WET))
        {
            weightFlower -= 5;
            weightShrub -= 3;
            weightMushroom += 100;
            weightTropical -= 5;
        }

        if (types.contains(BiomeDictionary.Type.COLD))
        {
            weightFlower += 10;
            weightTropical -= 50;
            weightMushroom += 10;
            weightShrub += 3;
        }

        if (types.contains(BiomeDictionary.Type.FOREST))
        {
            weightFlower += 10;
            weightShrub += 10;
            weightMushroom += 8;
            weightTropical -= 20;
        }

        if (types.contains(BiomeDictionary.Type.PLAINS))
        {
            weightMushroom -= 9;
            weightFlower += 30;
            weightShrub += 10;
            weightTropical -= 80;
        }

        List<TypeItem> itemList = Lists.newArrayList(new TypeItem(TROPICAL, weightTropical), new TypeItem(FLOWER, weightFlower), new TypeItem(SMALL_SHRUB, weightShrub), new TypeItem(MUSHROOM, weightMushroom));
        itemList.removeIf(i -> i.itemWeight <= 0);
        return WeightedRandom.getRandomItem(rand, itemList).type;
    }

    private static class TypeItem extends WeightedRandom.Item
    {
        final EnumShrubberyType type;

        public TypeItem(EnumShrubberyType ordinal, int itemWeightIn)
        {
            super(itemWeightIn);
            this.type = ordinal;
        }
    }
}

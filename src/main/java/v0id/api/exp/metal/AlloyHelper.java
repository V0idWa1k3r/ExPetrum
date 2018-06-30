package v0id.api.exp.metal;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;

public class AlloyHelper
{
    public static EnumMetal getAlloy(Pair<EnumMetal, Integer>... metals)
    {
        Map<EnumMetal, Float> vals = Maps.newEnumMap(EnumMetal.class);
        float value = Arrays.stream(metals).mapToInt(Pair::getRight).sum();
        for (Pair<EnumMetal, Integer> data : metals)
        {
            vals.put(data.getLeft(), data.getRight() / value);
        }

        l: for (EnumMetal metal : EnumMetal.values())
        {
            if (metal.getComposition() != null)
            {
                for (Pair<EnumMetal, Pair<Float, Float>> compos : metal.getComposition().compositionData)
                {
                    if (!vals.containsKey(compos.getLeft()))
                    {
                        continue l;
                    }

                    float val = vals.get(compos.getLeft());
                    if (val < compos.getRight().getLeft() || val > compos.getRight().getRight())
                    {
                        continue l;
                    }
                }

                return metal;
            }
        }

        return null;
    }
}

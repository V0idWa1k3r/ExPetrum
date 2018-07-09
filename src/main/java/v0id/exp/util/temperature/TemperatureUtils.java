package v0id.exp.util.temperature;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import v0id.api.exp.util.Gradient;

import javax.vecmath.Vector3f;
import java.util.List;

public class TemperatureUtils
{
    public static final String TEMP_KEY = "exp:temperature";
    public static final List<TemperatureValue> VALUES = new ImmutableList.Builder()
            .add(
                    new TemperatureValue(0, 10, 0),
                    new TemperatureValue(10, 20, 1),
                    new TemperatureValue(20, 30, 2),
                    new TemperatureValue(30, 40, 3),
                    new TemperatureValue(40, 50, 4),
                    new TemperatureValue(50, 60, 5),
                    new TemperatureValue(60, 70, 6),
                    new TemperatureValue(70, 80, 7),
                    new TemperatureValue(80, 90, 8),
                    new TemperatureValue(90, 100, 9),
                    new TemperatureValue(100, 120, 10),
                    new TemperatureValue(120, 140, 11),
                    new TemperatureValue(140, 160, 12),
                    new TemperatureValue(160, 180, 13),
                    new TemperatureValue(180, 200, 14),
                    new TemperatureValue(200, 300, 15),
                    new TemperatureValue(300, 400, 16),
                    new TemperatureValue(400, 500, 17),
                    new TemperatureValue(500, 550, 18),
                    new TemperatureValue(550, 600, 19),
                    new TemperatureValue(600, 640, 20),
                    new TemperatureValue(640, 680, 21),
                    new TemperatureValue(680, 720, 22),
                    new TemperatureValue(720, 760, 23),
                    new TemperatureValue(760, 800, 24),
                    new TemperatureValue(800, 840, 25),
                    new TemperatureValue(840, 880, 26),
                    new TemperatureValue(880, 920, 27),
                    new TemperatureValue(920, 960, 28),
                    new TemperatureValue(960, 1000, 29),
                    new TemperatureValue(1000, 1040, 30),
                    new TemperatureValue(1040, 1080, 31),
                    new TemperatureValue(1080, 1120, 32),
                    new TemperatureValue(1120, 1160, 33),
                    new TemperatureValue(1160, 1200, 34),
                    new TemperatureValue(1200, 1240, 35),
                    new TemperatureValue(1240, 1280, 36),
                    new TemperatureValue(1280, 1320, 37),
                    new TemperatureValue(1320, 1360, 38),
                    new TemperatureValue(1360, 1400, 39),
                    new TemperatureValue(1400, 1500, 40),
                    new TemperatureValue(1500, 1600, 41),
                    new TemperatureValue(1600, 1700, 42),
                    new TemperatureValue(1700, 1800, 43),
                    new TemperatureValue(1800, Integer.MAX_VALUE, 44)
            ).build();

    public static final Gradient TEMPERATURE_GRADIENT = new Gradient();

    static
    {
        TEMPERATURE_GRADIENT.gradientFunc = Gradient.Linear;
        TEMPERATURE_GRADIENT.add(50F, new Vector3f(0, 0, 1F));
        TEMPERATURE_GRADIENT.add(200F, new Vector3f(0.68F, 0, 1F));
        TEMPERATURE_GRADIENT.add(550F, new Vector3f(0.33F, 0.08F, 0));
        TEMPERATURE_GRADIENT.add(550F, new Vector3f(0.33F, 0.08F, 0));
        TEMPERATURE_GRADIENT.add(725F, new Vector3f(1, 0, 0));
        TEMPERATURE_GRADIENT.add(875F, new Vector3f(1, 0.47F, 0));
        TEMPERATURE_GRADIENT.add(875F, new Vector3f(1, 0.47F, 0));
        TEMPERATURE_GRADIENT.add(1100F, new Vector3f(1, 1, 0));
        TEMPERATURE_GRADIENT.add(1800F, new Vector3f(1, 1, 1));
    }

    public static float getTemperature(ItemStack is)
    {
        if (is.hasTagCompound() && is.getTagCompound().hasKey(TEMP_KEY, Constants.NBT.TAG_FLOAT))
        {
            return is.getTagCompound().getFloat(TEMP_KEY);
        }

        return 0F;
    }

    public static void setTemperature(ItemStack is, float temp)
    {
        if (temp <= 0.0F)
        {
            if (is.hasTagCompound())
            {
                is.getTagCompound().removeTag(TEMP_KEY);
                if (is.getTagCompound().hasNoTags())
                {
                    is.setTagCompound(null);
                }
            }

            return;
        }

        if (!is.hasTagCompound())
        {
            is.setTagCompound(new NBTTagCompound());
        }

        is.getTagCompound().setFloat(TEMP_KEY, temp);
    }

    public static void incrementTemperature(ItemStack is, float by)
    {
        setTemperature(is, getTemperature(is) + by);
    }

    public static int getTemperatureIndex(float temp)
    {
        for (int i = 0; i < VALUES.size(); ++i)
        {
            TemperatureValue value = VALUES.get(i);
            if (value.matches(temp))
            {
                return value.value;
            }
        }

        return -1;
    }

    public static void tickItem(ItemStack is, boolean decrementTemperature)
    {
        if (decrementTemperature && !is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey(TEMP_KEY, Constants.NBT.TAG_FLOAT))
        {
            incrementTemperature(is, -0.5F);
        }
    }

    private static class TemperatureValue
    {
        private float min;
        private float max;
        private int value;

        public TemperatureValue(float min, float max, int value)
        {
            this.min = min;
            this.max = max;
            this.value = value;
        }

        public boolean matches(float val)
        {
            return val >= this.min && val <= this.max;
        }
    }
}

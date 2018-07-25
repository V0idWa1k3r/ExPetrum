package v0id.exp.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.metal.EnumMetal;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ItemMetalGeneric extends Item implements IOreDictEntry, IWeightProvider, IMeltableMetal
{
    public enum EnumGenericType
    {
        CLUMP(2F, Pair.of((byte)2, (byte)1), "clump"),
        SHEET(1F, IWeightProvider.DEFAULT_VOLUME, "sheet", "plate"),
        DOUBLE_SHEET(2F, Pair.of((byte)2, (byte)1), "doubleSheet", "doublePlate");

        EnumGenericType(float weight, Pair<Byte, Byte> volume, String... oreDictNames)
        {
            this.oreDictNames = oreDictNames;
            this.weight = weight;
            this.volume = volume;
        }

        private final String[] oreDictNames;
        private final float weight;
        private final Pair<Byte, Byte> volume;

        public String[] getOreDictNames()
        {
            return oreDictNames;
        }
    }

    public ItemMetalGeneric()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemMetalGeneric));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMetals);
        this.setHasSubtypes(true);
    }

    @Override
    public void registerOreDictNames()
    {
        for (EnumGenericType type : EnumGenericType.values())
        {
            for (EnumMetal metal : EnumMetal.values())
            {
                Arrays.stream(type.getOreDictNames()).forEach(name -> registerMetalOreDict(metal, name, metal.ordinal() + EnumMetal.values().length * type.ordinal()));
            }
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (EnumGenericType type : EnumGenericType.values())
        {
            for (EnumMetal metal : EnumMetal.values())
            {
                items.add(new ItemStack(this, 1, metal.ordinal() + type.ordinal() * EnumMetal.values().length));
            }
        }
    }

    public void registerMetalOreDict(EnumMetal metal, String prefix, int meta)
    {
        Stream.of(metal.getOreDictPostfix()).forEach(s -> OreDictionary.registerOre(prefix + Character.toUpperCase(s.charAt(0)) + s.substring(1), new ItemStack(this, 1, meta)));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return EnumGenericType.values()[item.getMetadata() / EnumMetal.values().length].weight;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return EnumGenericType.values()[item.getMetadata() / EnumMetal.values().length].volume;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + EnumGenericType.values()[stack.getMetadata() / EnumMetal.values().length].name().toLowerCase() + "." + EnumMetal.values()[stack.getMetadata() % EnumMetal.values().length].getName();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String s = Strings.EMPTY;
        float currentT = TemperatureUtils.getTemperature(stack);
        float meltingT = this.getMeltingTemperature(stack);
        if (currentT >= meltingT * 0.7F)
        {
            s += I18n.format("exp.txt.canWork");
        }

        if (currentT >= meltingT * 0.85F)
        {
            s += I18n.format("exp.sym.line") + I18n.format("exp.txt.canWeld");
        }

        if (currentT >= meltingT * 0.92F)
        {
            s += I18n.format("exp.sym.line") + I18n.format("exp.txt.danger");
        }

        if (!s.isEmpty())
        {
            tooltip.add(s);
        }
    }

    @Override
    public EnumMetal getMetal(ItemStack is)
    {
        return EnumMetal.values()[is.getMetadata() % EnumMetal.values().length];
    }

    @Override
    public float getMeltingTemperature(ItemStack is)
    {
        return EnumMetal.values()[is.getMetadata() % EnumMetal.values().length].getMeltingTemperature();
    }

    @Override
    public int getMetalAmound(ItemStack is)
    {
        return (EnumGenericType.values()[is.getMetadata() / EnumMetal.values().length] == EnumGenericType.SHEET ? 100 : 200) * is.getCount();
    }
}

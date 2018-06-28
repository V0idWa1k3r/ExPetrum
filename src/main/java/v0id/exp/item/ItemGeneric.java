package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

import java.util.stream.Stream;

public class ItemGeneric extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
    public enum EnumGenericType
    {
        CLAY("clay", 0.05F, IWeightProvider.DEFAULT_VOLUME, "clay", "itemClay"),
        TWINE("twine", 0.001F, IWeightProvider.DEFAULT_VOLUME, "twine", "plantTwine"),
        PLANT_FIBER("plant_fiber", 0.004F, IWeightProvider.DEFAULT_VOLUME, "fiber", "plantFiber"),
        LIGNITE("lignite", 0.05F, IWeightProvider.DEFAULT_VOLUME, "coal"),
        BITUMINOUS_COAL("bituminous_coal", 0.05F, IWeightProvider.DEFAULT_VOLUME, "coal"),
        ANTHRACITE("anthracite", 0.05F, IWeightProvider.DEFAULT_VOLUME, "coal"),
        ASH("ash", 0F, IWeightProvider.DEFAULT_VOLUME, "ash", "dustAsh");

        EnumGenericType(String name, float weight, Pair<Byte, Byte> volume, String... oreDictNames)
        {
            this.name = name;
            this.weight = weight;
            this.volume = volume;
            this.oreDictNames = oreDictNames;
        }

        private String name;
        private float weight;
        private Pair<Byte, Byte> volume;
        private String[] oreDictNames;

        public String getName()
        {
            return name;
        }
    }

    public ItemGeneric()
    {
        super();
        this.initItem();
    }

    @Override
    public void registerOreDictNames()
    {
        Stream.of(EnumGenericType.values()).forEach(s -> Stream.of(s.oreDictNames).forEach(name -> OreDictionary.registerOre(name, new ItemStack(this, 1, s.ordinal()))));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return EnumGenericType.values()[item.getMetadata()].weight;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return EnumGenericType.values()[item.getMetadata()].volume;
    }

    @Override
    public void initItem()
    {
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemGeneric));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscItems);
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (int i = 0; i < EnumGenericType.values().length; ++i)
        {
            subItems.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + EnumGenericType.values()[stack.getMetadata()].getName();
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack)
    {
        EnumGenericType type = EnumGenericType.values()[itemStack.getMetadata()];
        return type == EnumGenericType.LIGNITE ? 1000 : type == EnumGenericType.BITUMINOUS_COAL ? 1600 : type == EnumGenericType.ANTHRACITE ? 3200 : super.getItemBurnTime(itemStack);
    }
}

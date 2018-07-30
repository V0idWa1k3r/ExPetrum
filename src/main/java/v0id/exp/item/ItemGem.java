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
import v0id.api.exp.item.EnumGemType;

import java.util.Arrays;

public class ItemGem extends Item implements IWeightProvider, IOreDictEntry
{
    public ItemGem()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemGem));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabUnderground);
        this.setHasSubtypes(true);
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(EnumGemType.values()).forEach(e -> Arrays.stream(e.getOreDictNames()).forEach(name -> OreDictionary.registerOre(name, new ItemStack(this, 1, e.ordinal()))));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.1F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        Arrays.stream(EnumGemType.values()).forEach(e -> items.add(new ItemStack(this, 1, e.ordinal())));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + '.' + EnumGemType.values()[stack.getMetadata()].name().toLowerCase();
    }
}

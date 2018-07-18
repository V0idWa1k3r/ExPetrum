package v0id.exp.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

import java.util.Arrays;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public class ItemWoolCard extends Item implements IOreDictEntry, IWeightProvider
{
    public ItemWoolCard()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemWoolCard));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
        this.setHasSubtypes(true);
        this.setMaxDamage(12);
        this.setMaxStackSize(1);
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(ExPOreDict.itemWoolCard).forEach(name -> OreDictionary.registerOre(name, this));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.5F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack ret = itemStack.copy();
        ret.setItemDamage(ret.getItemDamage() + 1);
        if (ret.getItemDamage() >= ret.getMaxDamage())
        {
            return ItemStack.EMPTY;
        }

        return ret;
    }
}

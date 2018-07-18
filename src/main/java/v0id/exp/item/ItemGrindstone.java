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
public class ItemGrindstone extends Item implements IOreDictEntry, IWeightProvider
{
    public ItemGrindstone()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemGrindstone));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
        this.setHasSubtypes(true);
        this.setMaxDamage(256);
        this.setMaxStackSize(1);
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(ExPOreDict.itemGrindstone).forEach(name -> OreDictionary.registerOre(name, this));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 1F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }
}

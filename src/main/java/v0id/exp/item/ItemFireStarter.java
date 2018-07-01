package v0id.exp.item;

import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.IFireProvider;

import java.util.Arrays;

public class ItemFireStarter extends ItemFlintAndSteel implements IWeightProvider, IOreDictEntry, IInitializableItem, IFireProvider
{
    public ItemFireStarter()
    {
        super();
        this.initItem();
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(ExPOreDict.itemFireStarter).forEach(name -> OreDictionary.registerOre(name, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
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
    public void initItem()
    {
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemFireStarter));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
        this.setMaxDamage(7);
    }
}

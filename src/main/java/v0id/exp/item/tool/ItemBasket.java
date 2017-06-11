package v0id.exp.item.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.item.IInitializableItem;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public class ItemBasket extends Item implements IInitializableItem, IItemRegistryEntry, IOreDictEntry, IWeightProvider
{
    public ItemBasket()
    {
        super();
        this.initItem();
    }

    @Override
    public void registerOreDictNames()
    {

    }

    @Override
    public void initItem()
    {
        this.setRegistryName(ExPRegistryNames.itemBasket);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
        this.setHasSubtypes(true);
        this.setMaxDamage(256);
        ExPHandlerRegistry.put(this);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(this);
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }
}

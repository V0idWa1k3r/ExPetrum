package v0id.exp.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.ITuyere;
import v0id.api.exp.metal.EnumToolStats;

import java.util.EnumMap;
import java.util.Map;

public class ItemTuyere extends Item implements IWeightProvider, ITuyere
{
    public static final Map<EnumToolStats, ItemTuyere> items = new EnumMap<>(EnumToolStats.class);
    public final EnumToolStats stats;

    public ItemTuyere(EnumToolStats ets)
    {
        super();
        this.stats = ets;
        items.put(ets, this);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemTuyere + "_" + this.stats.getName()));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setMaxDamage(this.stats.getDurability());
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return this.stats.getWeight() * 1.5F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)3, (byte)2);
    }

    @Override
    public float getMeltingTemperature(ItemStack is)
    {
        return this.stats.getMaterial().getMeltingTemperature();
    }
}

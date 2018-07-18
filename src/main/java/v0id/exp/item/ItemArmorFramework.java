package v0id.exp.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.EnumArmorStats;
import v0id.api.exp.metal.EnumMetal;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemArmorFramework extends Item implements IWeightProvider
{
    public static final EntityEquipmentSlot[] SLOTS = new EntityEquipmentSlot[]{ EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
    public static final float[] slotModifiers = new float[]{ 0.15F, 0.4F, 0.3F, 0.15F };
    public ItemArmorFramework()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemArmorFramework));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMetals);
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (EnumArmorStats stats : EnumArmorStats.values())
        {
            for (EntityEquipmentSlot slot : SLOTS)
            {
                items.add(createFramework(stats, slot));
            }
        }
    }

    public static ItemStack createFramework(EnumArmorStats stats, EntityEquipmentSlot slot)
    {
        assert ArrayUtils.contains(SLOTS, slot) : "Only armor equipment slots are allowed!";
        return new ItemStack(ExPItems.armorFramework, 1, stats.ordinal() * 4 + (3 - (slot.ordinal() - 2)));
    }

    public static EntityEquipmentSlot getSlotFromFramework(ItemStack framework)
    {
        return SLOTS[framework.getMetadata() % 4];
    }

    public static EnumArmorStats getStatsFromFramework(ItemStack framework)
    {
        return EnumArmorStats.values()[framework.getMetadata() / 4];
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return getStatsFromFramework(item).weight * slotModifiers[item.getMetadata() % 4];
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        EntityEquipmentSlot slot = getSlotFromFramework(item);
        return slot == EntityEquipmentSlot.HEAD || slot == EntityEquipmentSlot.FEET ? Pair.of((byte)2, (byte)2) : Pair.of((byte)3, (byte)3);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + getStatsFromFramework(stack).name + "." + getSlotFromFramework(stack).getName();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String s = Strings.EMPTY;
        float currentT = TemperatureUtils.getTemperature(stack);
        EnumMetal metal = getStatsFromFramework(stack).associatedMetal;
        float meltingT = metal == null ? Float.MAX_VALUE : metal.getMeltingTemperature();
        if (currentT >= meltingT * 0.7F)
        {
            s += I18n.format("exp.txt.canWork");
        }

        if (!s.isEmpty())
        {
            tooltip.add(s);
        }
    }
}

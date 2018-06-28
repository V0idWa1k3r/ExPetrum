package v0id.exp.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMold extends Item implements IInitializableItem, IWeightProvider
{
    public enum MoldType
    {
        CLAY,
        CERAMIC,
        FULL
    }

    private boolean isIngot;

    public ItemMold(boolean isIngot)
    {
        super();
        this.isIngot = isIngot;
        this.initItem();
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.05F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)1);
    }

    @Override
    public void initItem()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(ExPCreativeTabs.tabMiscItems);
        this.setRegistryName(this.isIngot ? ExPRegistryNames.itemMoldIngot : ExPRegistryNames.itemMoldTool);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        if (this.isIngot)
        {
            for (int i = 0; i < 2 + EnumMetal.values().length; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
        else
        {
            for (int i = 0; i < EnumToolClass.values().length * MoldType.values().length; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (this.isIngot)
        {
            return super.getUnlocalizedName(stack) + "." + (stack.getMetadata() == 0 ? "clay" : stack.getMetadata() == 1 ? "ceramic" : "full");
        }

        return super.getUnlocalizedName(stack) + "." + MoldType.values()[stack.getMetadata() / EnumToolClass.values().length].name().toLowerCase() + "." + EnumToolClass.values()[stack.getMetadata() % EnumToolClass.values().length].name().toLowerCase();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this.isIngot && stack.getMetadata() > 1)
        {
            tooltip.add(I18n.format("exp.item.ingot.desc.type." + EnumMetal.values()[stack.getMetadata() - 2].name().toLowerCase()));
        }
    }
}

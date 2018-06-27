package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;

import java.util.stream.Stream;

public class ItemToolHead extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
	public ItemToolHead()
	{
		super();
		this.initItem();
	}

	public static ItemStack createToolHead(EnumToolClass type, EnumToolStats material)
    {
        return new ItemStack(ExPItems.toolHead, 1, type.ordinal() * EnumToolStats.values().length + material.ordinal());
    }

	@Override
	public float provideWeight(ItemStack item)
	{
		return EnumToolClass.values()[item.getMetadata() / EnumToolStats.values().length].getWeight() * EnumToolStats.values()[item.getMetadata() % EnumToolStats.values().length].getWeight() * 0.7F;
	}

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        EnumToolClass type = EnumToolClass.values()[stack.getMetadata() / EnumToolStats.values().length];
        EnumToolStats material = EnumToolStats.values()[stack.getMetadata() % EnumToolStats.values().length];
        return super.getUnlocalizedName(stack) + "." + type.name().toLowerCase() + "." + material.name().toLowerCase();
    }

    @Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		switch (EnumToolClass.values()[item.getMetadata() / EnumToolStats.values().length])
		{
            case BATTLEAXE:
            case HAMMER:
			{
				return Pair.of((byte)3, (byte)2);
			}
			
            case SCYTHE:
            case SHOVEL:
            case AXE:
            case PICKAXE:
            case SWORD:
			{
				return Pair.of((byte)2, (byte)2);
			}
			
            case HOE:
            case SAW:
            case SPEAR:
            case KNIFE:
			{
				return Pair.of((byte)1, (byte)2);
			}
			
			default:
			{
				return IWeightProvider.DEFAULT_VOLUME;
			}
		}
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemToolhead));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (tab != this.getCreativeTab())
		{
			return;
		}

        Stream.of(EnumToolClass.values()).forEach(tool -> Stream.of(EnumToolStats.values()).forEach(material -> {
            subItems.add(new ItemStack(this, 1, tool.ordinal() * EnumToolStats.values().length + material.ordinal()));
        }));
	}

	@Override
	public void registerOreDictNames()
	{
		for (int i = 0; i < ExPOreDict.itemToolHeads.length; ++i)
		{
			OreDictionary.registerOre(ExPOreDict.itemToolHeads[i], new ItemStack(this, 1, i));
		}
	}
}

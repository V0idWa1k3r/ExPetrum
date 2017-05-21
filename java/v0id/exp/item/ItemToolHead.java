package v0id.exp.item;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;

public class ItemToolHead extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry, IItemRegistryEntry
{
	public ItemToolHead()
	{
		super();
		this.initItem();
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		// TODO Auto-generated method stub
		return Pair.of((byte)3, (byte)1);
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.itemToolhead);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
		ExPHandlerRegistry.put(this);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < 7; ++i)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public void registerOreDictNames()
	{
		for (int i = 0; i < ExPOreDict.itemToolHeads.length; ++i)
		{
			OreDictionary.registerOre(ExPOreDict.itemToolHeads[i], new ItemStack(this, 1, i));
		}
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(this);
	}
}

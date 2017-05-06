package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

public class ItemToolHead extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
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
	public float provideVolume(ItemStack item)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.itemToolhead);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
		GameRegistry.register(this);
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
}

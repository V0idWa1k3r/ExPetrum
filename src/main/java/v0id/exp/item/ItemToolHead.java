package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.player.inventory.PlayerInventoryHelper;

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
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		switch (item.getMetadata())
		{
			case 0:
			{
				return Pair.of((byte)3, (byte)2);
			}
			
			case 1:
			case 2:
			{
				return Pair.of((byte)2, (byte)2);
			}
			
			case 3:
			case 4:
			{
				return Pair.of((byte)1, (byte)2);
			}
			
			default:
			{
				return PlayerInventoryHelper.defaultVolume;
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

		for (int i = 0; i < 7; ++i)
		{
			subItems.add(new ItemStack(this, 1, i));
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

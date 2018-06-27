package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ItemRock extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
	public ItemRock()
	{
		super();
		this.initItem();
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
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemRock));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabUnderground);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (tab != this.getCreativeTab())
		{
			return;
		}

		Stream.of(EnumRockClass.values()).forEach(c -> subItems.add(new ItemStack(this, 1, c.ordinal())));
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.itemRock).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.rockNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}
}

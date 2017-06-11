package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumMetal;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.player.inventory.PlayerInventoryHelper;

import java.util.stream.Stream;

public class ItemIngot extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry, IItemRegistryEntry
{
	public ItemIngot()
	{
		super();
		this.initItem();
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(this);
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(EnumMetal.values()).forEach(this::registerMetalOreDict);
	}
	
	public void registerMetalOreDict(EnumMetal metal)
	{
		Stream.of(metal.getOreDictPostfix()).forEach(s ->{
			OreDictionary.registerOre("ingot" + Character.toUpperCase(s.charAt(0)) + s.substring(1), new ItemStack(this, 1, metal.ordinal()));
		});
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
		return PlayerInventoryHelper.defaultVolume;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < EnumMetal.values().length; ++i)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.itemIngot);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabMetals);
		this.setHasSubtypes(true);
		ExPHandlerRegistry.put(this);
	}
}

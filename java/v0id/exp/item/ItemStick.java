package v0id.exp.item;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

public class ItemStick extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
	public ItemStick()
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
		this.setRegistryName(ExPRegistryNames.itemStick);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setHasSubtypes(true);
		GameRegistry.register(this);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		Stream.of(EnumTreeType.values()).forEach(c -> subItems.add(new ItemStack(itemIn, 1, c.ordinal())));
		Stream.of(EnumShrubType.values()).forEach(c -> subItems.add(new ItemStack(itemIn, 1, EnumTreeType.values().length + c.ordinal())));
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.itemStick).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.stickNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}
}

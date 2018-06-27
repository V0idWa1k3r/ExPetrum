package v0id.exp.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumMetal;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class ItemIngot extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
	public ItemIngot()
	{
		super();
		this.initItem();
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(EnumMetal.values()).forEach(this::registerMetalOreDict);
	}
	
	public void registerMetalOreDict(EnumMetal metal)
	{
		Stream.of(metal.getOreDictPostfix()).forEach(s -> OreDictionary.registerOre("ingot" + Character.toUpperCase(s.charAt(0)) + s.substring(1), new ItemStack(this, 1, metal.ordinal())));
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (tab != this.getCreativeTab())
		{
			return;
		}

		for (int i = 0; i < EnumMetal.values().length; ++i)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemIngot));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabMetals);
		this.setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("exp.item.ingot.desc.type." + EnumMetal.values()[stack.getMetadata()].name().toLowerCase()));
	}
}

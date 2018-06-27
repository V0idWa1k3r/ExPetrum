package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumBerry;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
		return 0.01F;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return IWeightProvider.DEFAULT_VOLUME;
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemStick));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (tab != this.getCreativeTab())
		{
			return;
		}

		Stream.of(EnumTreeType.values()).forEach(c -> subItems.add(new ItemStack(this, 1, c.ordinal())));
		Stream.of(EnumShrubType.values()).forEach(c -> subItems.add(new ItemStack(this, 1, EnumTreeType.values().length + c.ordinal())));
		Stream.of(EnumBerry.values()).forEach(c -> subItems.add(new ItemStack(this, 1, EnumTreeType.values().length + EnumShrubType.values().length + c.ordinal())));
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}

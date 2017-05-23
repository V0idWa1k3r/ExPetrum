package v0id.exp.item;

import java.util.EnumMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.item.food.IExPFood;
import v0id.api.exp.player.Nutrient;
import v0id.api.exp.world.Calendar;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;

public class ItemFood extends net.minecraft.item.ItemFood implements IInitializableItem, IOreDictEntry, IItemRegistryEntry, IExPFood
{
	public ItemFood()
	{
		super(0, false);
		this.initItem();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getEntry(stack).getUnlocalizedName();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < FoodEntry.allEntries.size(); ++i)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public float getCalories(ItemStack stack)
	{
		return this.getEntry(stack).getCaloriesRestored() * (this.getTotalWeight(stack) / 100);
	}

	@Override
	public EnumMap<Nutrient, Float> getNutrients(ItemStack stack)
	{
		EnumMap<Nutrient, Float> nutMap = new EnumMap(Nutrient.class);
		float weightMul = this.getTotalWeight(stack) / 100;
		this.getEntry(stack).getNutrientData().forEach((Nutrient n, Float f) -> nutMap.put(n, f * weightMul));
		return nutMap;
	}

	@Override
	public boolean skipHandlers(ItemStack stack)
	{
		return true;
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(this);
	}

	@Override
	public void registerOreDictNames()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void initItem()
	{
		this.setHasSubtypes(true);
		ExPHandlerRegistry.put(this);
		this.setRegistryName(ExPRegistryNames.itemFood);
		this.setMaxStackSize(1);
	}

	public float getTotalWeight(ItemStack is)
	{
		return is.getOrCreateSubCompound("exp.foodData").getFloat("weight");
	}
	
	public void setTotalWeight(ItemStack is, float f)
	{
		is.getOrCreateSubCompound("exp.foodData").setFloat("weight", f);
	}
	
	public float getTotalRot(ItemStack is)
	{
		return is.getOrCreateSubCompound("exp.foodData").getFloat("rot");
	}
	
	public void setTotalRot(ItemStack is, float f)
	{
		is.getOrCreateSubCompound("exp.foodData").setFloat("rot", f);
	}
	
	public Calendar getLastTickTime(ItemStack is)
	{
		Calendar ret = new Calendar();
		ret.deserializeNBT((NBTTagLong) is.getOrCreateSubCompound("exp.foodData").getTag("lastTick"));
		return ret;
	}
	
	public void setLastTickTime(ItemStack is, Calendar c)
	{
		is.getOrCreateSubCompound("exp.foodData").setTag("lastTick", c.serializeNBT());
	}
	
	public FoodEntry getEntry(ItemStack is)
	{
		return FoodEntry.allEntries.get(Math.min(is.getMetadata(), FoodEntry.allEntries.size() - 1));
	}
}

package v0id.exp.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.entity.EnumGender;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.item.food.IExPFood;
import v0id.api.exp.player.EnumFoodGroup;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.entity.impl.Chicken;
import v0id.exp.tile.TileNestingBox;
import v0id.exp.util.Helpers;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.List;

public class ItemFood extends net.minecraft.item.ItemFood implements IInitializableItem, IOreDictEntry, IExPFood, IContainerTickable, IWeightProvider
{
	private static Method nutritionCompat_eatFood;
	public ItemFood()
	{
		super(0, false);
		this.initItem();
		if (Loader.isModLoaded("nutrition"))
        {
            try
            {
                nutritionCompat_eatFood = Class.forName("v0id.exp.compat.NutritionCompat").getMethod("eatFood", EntityPlayer.class, FoodEntry.class, float.class);
            }
            catch (Exception e)
            {
                ExPMisc.modLogger.error("ExP was unable to initialize Nutrition compatibility!", e);
            }
        }
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + '.' + this.getEntry(stack).getUnlocalizedName();
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, world, tooltip, advanced);
		byte presType = this.getPreservationType(stack);
		if (presType == 1)
        {
            tooltip.add(I18n.format("exp.txt.pickled"));
        }

		DecimalFormat df = new DecimalFormat("#.#");
		DecimalFormat df1 = new DecimalFormat("#,###");
		tooltip.add(I18n.format("exp.txt.item.desc.rot", df.format((this.getTotalRot(stack) / this.getEntry(stack).getBaseHealth()) * 100)));
	    if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("fertilized"))
        {
            tooltip.add(I18n.format("exp.txt.fertilized"));
        }
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
        if (tab != this.getCreativeTab())
        {
            return;
        }

		for (int i = 0; i < FoodEntry.allEntries.size(); ++i)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public float getCalories(ItemStack stack)
	{
		return this.getEntry(stack).getCaloriesRestored() * (this.getTotalWeight(stack) / 100);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return this.getTotalRot(stack) / this.getEntry(stack).getBaseHealth();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

	@Override
	public EnumMap<EnumFoodGroup, Float> getFoodGroup(ItemStack stack)
	{
		EnumMap<EnumFoodGroup, Float> nutMap = new EnumMap<>(EnumFoodGroup.class);
		float weightMul = this.getTotalWeight(stack) / 100;
		this.getEntry(stack).getNutrientData().forEach((EnumFoodGroup n, Float f) -> nutMap.put(n, f * weightMul));
		return nutMap;
	}

	@Override
	public boolean skipHandlers(ItemStack stack)
	{
		return true;
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
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemFood));
		this.setMaxStackSize(1);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabFood);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("exp.foodData"))
		{
			this.initDefaultNBT(stack);
		}
		
		if (worldIn.getTotalWorldTime() % 500 == 0 && entityIn != null)
		{
			this.checkRot(stack, worldIn, entityIn.getPosition(), 1);
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 10;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return this.canEat(playerIn, playerIn.getHeldItem(handIn)) ? super.onItemRightClick(worldIn, playerIn, handIn) : new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			this.doEat((EntityPlayer) entityLiving, stack);
		}
		
		return stack;
	}

	public void initDefaultNBT(ItemStack is)
	{
		NBTTagCompound foodTag = new NBTTagCompound();
		foodTag.setFloat("weight", this.getEntry(is).getMaxWeight());
		foodTag.setFloat("rot", 0.1F);
		foodTag.setByte("preservationType", (byte)0);
		is.setTagCompound(is.hasTagCompound() ? is.getTagCompound() : new NBTTagCompound());
		is.getTagCompound().setTag("exp.foodData", foodTag);
	}
	
	public boolean canEat(EntityPlayer player, ItemStack is)
	{
		IExPPlayer p = IExPPlayer.of(player);
		return p.getCalories() <= 1980;
	}
	
	public void doEat(EntityPlayer player, ItemStack is)
	{
		IExPPlayer p = IExPPlayer.of(player);
		float missingCalories = 2000 - p.getCalories();
		FoodEntry entry = this.getEntry(is);
		float weight = this.getTotalWeight(is);
		
		// Player will not eat rotten food.
		float actual_weight = weight * (1 - (this.getTotalRot(is) / entry.getBaseHealth()));
		
		// All that is left is rot.
		if (actual_weight <= 0)
		{
			is.setCount(0);
			return;
		}
		
		float caloriesConsumed = Math.min(100, missingCalories);
		float weightNeeded = Math.min(caloriesConsumed / (entry.getCaloriesRestored() / 100), actual_weight);
		caloriesConsumed = weightNeeded * (entry.getCaloriesRestored() / 100);
		if (nutritionCompat_eatFood != null && !player.world.isRemote)
        {
            try
            {
                nutritionCompat_eatFood.invoke(null, player, entry, caloriesConsumed);
            }
            catch (Exception e)
            {
                ExPMisc.modLogger.error("ExP was unable to provide Nutrition compatibility report this to V0id!", e);
            }
        }

		p.setCalories(p.getCalories() + caloriesConsumed);
		if (actual_weight - weightNeeded <= 0)
		{
			is.setCount(0);
			return;
		}
		
		this.setTotalWeight(is, weight - weightNeeded);
		
		// Preserve rot values as the player is not eating rotten parts.
		float rotValue = weight * this.getTotalRot(is);
		float newRotValue = rotValue / (weight - weightNeeded);
		this.setTotalRot(is, newRotValue);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}

	public void checkRot(ItemStack is, World w, BlockPos at, float callerMultiplier)
	{
		if (this.getTotalWeight(is) == 0)
		{
			is.setCount(0);
			return;
		}
		
		Calendar today = IExPWorld.of(w).today();
		Calendar last = this.getLastTickTime(is);
		if (last.getTime() == 0)
		{
			this.setLastTickTime(is, today);
			return;
		}
		
		long skipped = today.getTime() - last.getTime();
		float rot_current = Math.max(this.getTotalRot(is), 0.1F);
		long tIndex = skipped / 1000;
		if (tIndex > 0)
		{
			this.setLastTickTime(is, today);
		}
		
		float rot_real = rot_current;
		float rot_multiplier = 1.1F * this.getEntry(is).getRotMultiplier();
		while (tIndex-- > 0)
		{
			rot_real *= 1.1;
		}
		
		float rot_added = rot_real - rot_current;
		rot_added *= this.getItemRotMultiplier(is) * this.getTemperatureMultiplier(Helpers.getTemperatureAt(w, at)) * callerMultiplier * rot_multiplier;
		if (rot_added > 0)
		{
			float rot = rot_current + rot_added;
			if (rot > this.getEntry(is).getBaseHealth())
			{
				is.setCount(0);
			}
			else
			{
				this.setTotalRot(is, rot);
			}
		}
	}
	
	public float getTemperatureMultiplier(float temp)
	{
		temp = MathHelper.clamp(temp, -60, 60);
		return (float) (0.3 * (60 + temp) / 120 + Math.pow((temp + 60) / 120, 2));
	}

	public float getTotalWeight(ItemStack is)
	{
		return is.getOrCreateSubCompound("exp.foodData").getFloat("weight");
	}
	
	public void setTotalWeight(ItemStack is, float f)
	{
		is.getOrCreateSubCompound("exp.foodData").setFloat("weight", f);
	}

	public byte getPreservationType(ItemStack is)
	{
		return is.getOrCreateSubCompound("exp.foodData").getByte("preservationType");
	}

	public void setPreservationType(ItemStack is, byte b)
	{
		is.getOrCreateSubCompound("exp.foodData").setByte("preservationType", b);
	}

	public float getTotalRot(ItemStack is)
	{
		if (!is.hasTagCompound() || !is.getTagCompound().hasKey("exp.foodData"))
		{
			this.initDefaultNBT(is);
		}
		
		return is.getOrCreateSubCompound("exp.foodData").getFloat("rot");
	}
	
	public void setTotalRot(ItemStack is, float f)
	{
		is.getOrCreateSubCompound("exp.foodData").setFloat("rot", f);
	}
	
	public float getItemRotMultiplier(ItemStack is)
	{
		return is.getOrCreateSubCompound("exp.foodData").hasKey("rotMultiplier", NBT.TAG_FLOAT) ? is.getOrCreateSubCompound("exp.foodData").getFloat("rotMultiplier") : 1;
	}
	
	public void setItemRotMultiplier(ItemStack is, float f)
	{
		is.getOrCreateSubCompound("exp.foodData").setFloat("rotMultiplier", f);
	}
	
	public Calendar getLastTickTime(ItemStack is)
	{
		Calendar ret = new Calendar();
		if (is.getOrCreateSubCompound("exp.foodData").hasKey("lastTick"))
		{
			ret.deserializeNBT((NBTTagLong) is.getOrCreateSubCompound("exp.foodData").getTag("lastTick"));
		}
		
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

	@Override
	public void onContainerTick(ItemStack is, World w, BlockPos pos, TileEntity container)
	{
		if (w.getTotalWorldTime() % 500 == 0)
		{
			this.checkRot(is, w, pos, 1);
		}

		if (container instanceof TileNestingBox && is.hasTagCompound() && is.getTagCompound().getBoolean("fertilized"))
        {
            is.getTagCompound().setInteger("birthProgress", is.getTagCompound().getInteger("birthProgress") + 1);
            if (is.getTagCompound().getInteger("birthProgress") >= 3 * IExPWorld.of(w).today().ticksPerDay && !w.isRemote)
            {
                Chicken.ChickenStats cStats = new Chicken.ChickenStats();
                cStats.deserializeNBT(is.getTagCompound().getCompoundTag("statsOffspring"));
                Chicken c = new Chicken(w);
                c.setPosition(pos.getX() + 0.5F, pos.getY() + 0.3F, pos.getZ() + 0.5F);
                c.animalCapability.setAge(0);
                c.animalCapability.setGender(w.rand.nextBoolean() ? EnumGender.FEMALE : EnumGender.MALE);
                c.animalCapability.setStats(cStats);
                c.animalCapability.setFamiliarity(0);
                c.animalCapability.setLastTickTime(IExPWorld.of(w).today().getTime());
                c.animalCapability.setHome(pos);
                w.spawnEntity(c);
                is.shrink(1);
            }
        }
	}
	
	public static int getMetadataFromCrop(EnumCrop crop)
	{
		switch (crop)
		{
			case MAIZE:
			{
				return FoodEntry.CORN.getId();
			}
			
			case RICE:
			{
				return FoodEntry.RICE.getId();
			}
			
			case WHEAT:
			{
				return FoodEntry.WHEAT.getId();
			}
			
			case BARLEY:
			{
				return FoodEntry.BARLEY.getId();
			}
			
			case SORGHUM:
			{
				return FoodEntry.SORGHUM.getId();
			}
			
			case MILLET:
			{
				return FoodEntry.MILLET.getId();
			}
			
			case OAT:
			{
				return FoodEntry.OAT.getId();
			}
			
			case RYE:
			{
				return FoodEntry.RYE.getId();
			}
			
			case CABBAGE:
			{
				return FoodEntry.CABBAGE.getId();
			}
			
			case TURNIP:
			{
				return FoodEntry.TURNIP.getId();
			}
			
			case RADISH:
			{
				return FoodEntry.RADISH.getId();
			}
			
			case CARROT:
			{
				return FoodEntry.CARROT.getId();
			}
			
			case PARSNIP:
			{
				return FoodEntry.PARSNIP.getId();
			}
			
			case BEETROOT:
			{
				return FoodEntry.BEETROOT.getId();
			}
			
			case LETTUCE:
			{
				return FoodEntry.LETTUCE.getId();
			}
			
			case BEANS:
			{
				return FoodEntry.BEANS.getId();
			}
			
			case PEAS:
			{
				return FoodEntry.PEAS.getId();
			}
			
			case POTATO:
			{
				return FoodEntry.POTATO.getId();
			}
			
			case EGGPLANT:
			{
				return FoodEntry.EGGPLANT.getId();
			}
			
			case TOMATO:
			{
				return FoodEntry.TOMATO.getId();
			}
			
			case CUCUMBER:
			{
				return FoodEntry.CUCUMBER.getId();
			}
			
			case PUMPKIN:
			{
				return FoodEntry.PUMPKIN.getId();
			}
			
			case ONION:
			{
				return FoodEntry.ONION.getId();
			}
			
			case GARLIC:
			{
				return FoodEntry.GARLIC.getId();
			}
			
			case LEEK:
			{
				return FoodEntry.LEEK.getId();
			}
			
			case SPINACH:
			{
				return FoodEntry.SPINACH.getId();
			}
			
			case SWEET_POTATO:
			{
				return FoodEntry.SWEET_POTATO.getId();
			}
			
			case CASSAVA:
			{
				return FoodEntry.CASSAVA.getId();
			}
			
			case PEPPER:
			case DEAD:
			default:
			{
				return -1;
			}
		}
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getTotalWeight(item) / 1000;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return IWeightProvider.DEFAULT_VOLUME;
	}
}

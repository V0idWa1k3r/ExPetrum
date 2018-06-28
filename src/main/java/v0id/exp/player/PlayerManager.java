package v0id.exp.player;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPDamageMappings;
import v0id.api.exp.data.ExPDamageMappings.DamageMapping;
import v0id.api.exp.item.food.FoodManager;
import v0id.api.exp.item.food.IExPFood;
import v0id.api.exp.player.BodyPart;
import v0id.api.exp.player.FoodGroup;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.util.temperature.TemperatureUtils;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.List;

public class PlayerManager
{
	public static final DamageSource expDeathCause = new DamageSource(null){
		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
	    {
			return new TextComponentString("");
	    }
		
		@Override
		public boolean canHarmInCreative()
	    {
			return true;
	    }
	};
	public static final Field foodExhaustionLevelFld = FoodStats.class.getDeclaredFields()[2];
	
	static
	{
		try
		{
			foodExhaustionLevelFld.setAccessible(true);
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to reflect player's FoodStats!", true);
		}
	}
	
	public static float getExhaustion(EntityPlayer of)
	{
		FoodStats stats = of.getFoodStats();
		if (!foodExhaustionLevelFld.isAccessible())
		{
			foodExhaustionLevelFld.setAccessible(true);
		}
		
		try
		{
			return foodExhaustionLevelFld.getFloat(stats);
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to reflect player's FoodStats!", true);
			return -1;
		}
	}
	
	@SuppressWarnings("SameParameterValue")
	public static void setExhaustion(EntityPlayer of, float f)
	{
		FoodStats stats = of.getFoodStats();
		if (!foodExhaustionLevelFld.isAccessible())
		{
			foodExhaustionLevelFld.setAccessible(true);
		}
		
		try
		{
			foodExhaustionLevelFld.setFloat(stats, f);
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to reflect player's FoodStats!", true);
		}
	}
	
	public static void tick(EntityPlayer player, IExPPlayer data, int skippedTicks)
	{
		if (!player.world.isRemote)
		{
			if (data.getCurrentHealth() <= 0)
			{
				data.resetData();
				while (player.isEntityAlive())
				{
					player.attackEntityFrom(expDeathCause, Float.MAX_VALUE);
				}
			}
			
			handleHunger(player, data, skippedTicks);
			handleThirst(player, data, skippedTicks);
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                TemperatureUtils.tickItem(player.inventory.getStackInSlot(i), true);
            }
		}
	}
	
	public static void handleThirst(EntityPlayer player, IExPPlayer data, int skippedTicks)
	{
		if (skippedTicks == 0)
		{
			handleThirstChange(player, data, 1);
		}
		else
		{
			for (int i = 0; i < skippedTicks; ++i)
			{
				handleThirstChange(player, data, 0.5F);
			}
		}
	}
	
	public static void handleThirstChange(EntityPlayer player, IExPPlayer data, float modifier)
	{
		float tChange = 0.125F;
		if (modifier == 1)
		{
			float ex = getExhaustion(player);
			if (ex > 0)
			{
				tChange += ex;
				setExhaustion(player, 0);
			}
		}
		
		if (!player.capabilities.isCreativeMode)
		{
			data.setThirst(data.getThirst() - tChange * modifier, true);
		}
	}
	
	public static void handleHunger(EntityPlayer player, IExPPlayer data, int skippedTicks)
	{
		player.getFoodStats().setFoodLevel(data.getCalories() < 2000 ? 19 : 20);
		if ((data.getCalories() <= 0 || data.getThirst() <= 0) && player.ticksExisted % 40 == 0)
		{
			player.attackEntityFrom(DamageSource.STARVE, 1);
		}
		
		if (skippedTicks == 0)
		{
			if (data.getCurrentHealth() < data.getMaxHealth(true) && player.ticksExisted % 100 == 0)
			{
				handleHealthRegen(data, 1);
			}
			
			handleHungerChange(player, data, 1);
		}
		else
		{
			for (int i = 0; i < skippedTicks; ++i)
			{
				if (data.getCurrentHealth() < data.getMaxHealth(true) && i % 100 == 0)
				{
					handleHealthRegen(data, 0.5F);
				}
				
				handleHungerChange(player, data, 0.5f);
			}
		}
	}
	
	public static void handlePlayerEatenFood(EntityPlayer player, IExPPlayer data, ItemStack stack)
	{
		Item i = stack.getItem();
		if (i instanceof IExPFood)
		{
			if (((IExPFood)i).skipHandlers(stack))
			{
				return;
			}
			
			IExPFood food = (IExPFood) i;
			applyFoodStats(player, data, food.getCalories(stack), food.getFoodGroup(stack));
		}
		else
		{
			Pair<Float, EnumMap<FoodGroup, Float>> foodData = FoodManager.provideFoodStats(stack);
			if (foodData != null)
			{
				applyFoodStats(player, data, foodData.getKey(), foodData.getRight());
			}
		}
	}
	
	public static void applyFoodStats(EntityPlayer player, IExPPlayer data, float calories, EnumMap<FoodGroup, Float> nutrients)
	{
		data.setCalories(Math.min(2000F, data.getCalories() + calories));
		nutrients.forEach((FoodGroup n, Float f) -> data.setNutritionLevel(Math.min(100, data.getNutritionLevel(n) + f), n));
	}
	
	public static void handlePlayerPlaceBlock(EntityPlayer player, IExPPlayer data)
	{
		if (!player.capabilities.isCreativeMode)
		{
			data.setCalories(data.getCalories() - 0.1F);
		}
	}
	
	public static void handlePlayerBrokeBlock(EntityPlayer player, IExPPlayer data)
	{
		if (!player.capabilities.isCreativeMode)
		{
			data.setCalories(data.getCalories() - 1);
		}
	}
	
	public static void handleHungerChange(EntityPlayer of, IExPPlayer data, float multiplier)
	{
		float cRemoved = 0.05f;
		if (of.isSprinting() && multiplier == 1)
		{
			cRemoved *= 2;
		}
		
		cRemoved *= multiplier;
		if (!of.capabilities.isCreativeMode)
		{
			data.setCalories(data.getCalories() - cRemoved);
			float cCurrent = data.getCalories();
			for (FoodGroup n : FoodGroup.values())
			{
				data.setNutritionLevel(Math.max(0, data.getNutritionLevel(n) - (cCurrent >= 500 ? 0.001F : 0.005F) * multiplier), n);
			}
		}
	}

	public static void handleHealthRegen(IExPPlayer data, float multiplier)
	{
		// TODO better health regen mechanics!
		float hpRestored = 10;
		hpRestored /= multiplier;
		if (hpRestored > 0)
		{
			data.setHealth(data.getCurrentHealth() + hpRestored, false, true);
		}
	}
	
	public static void takeDamage(EntityPlayer player, DamageSource source, float amount)
	{
		final float baseAmount = amount;
		IExPPlayer data = IExPPlayer.of(player);
		List<DamageMapping> mappings = ExPDamageMappings.provide(source);
		BodyPart part = WeightedRandom.getRandomItem(player.world.rand, mappings).getPart();
		EntityEquipmentSlot armorCheck = part == BodyPart.HEAD ? EntityEquipmentSlot.HEAD : part == BodyPart.ARM_LEFT || part == BodyPart.ARM_RIGHT || part == BodyPart.BODY ? EntityEquipmentSlot.CHEST : player.world.rand.nextBoolean() ? EntityEquipmentSlot.LEGS : EntityEquipmentSlot.FEET;
		ItemStack armorStack = player.getItemStackFromSlot(armorCheck);
		if (!armorStack.isEmpty())
		{
			amount = handleArmorProtection(player, source, amount, armorStack, part, armorCheck);
		}
		
		float partDamage = player.world.rand.nextFloat();
		// No damage reduction was applied, player has no armor
		if (baseAmount == amount)
		{
			partDamage *= 10;
		}
		
		data.setState(part, Math.max(0, data.getState(part) - partDamage));
		// TODO bleeding, bones breaking
		float actualDamage = amount * (200 + player.world.rand.nextFloat() * 50 - player.world.rand.nextFloat() * 50);
		data.setHealth(data.getCurrentHealth() - actualDamage, true, true);
		
		// Player died
		if (data.getCurrentHealth() <= 0)
		{
			player.sendMessage(source.getDeathMessage(player));
		}
	}
	
	public static float handleArmorProtection(EntityPlayer player, DamageSource source, float amount, ItemStack armor, BodyPart damaged, EntityEquipmentSlot slotDamaged)
	{
		if (source.isUnblockable())
		{
			return amount;
		}
		
		Item armorItem = armor.getItem();
		if (armorItem instanceof ISpecialArmor)
		{
			ISpecialArmor specialArmor = (ISpecialArmor) armorItem;
			float value = ArmorProperties.applyArmor(player, NonNullList.withSize(1, armor), source, amount);
			specialArmor.damageArmor(player, armor, source, (int) amount / 2, slotDamaged.getSlotIndex());
			return value;
		}
		else
		{
			Multimap<String, AttributeModifier> modifiers = armor.getAttributeModifiers(slotDamaged);
			float toughnessMod = 1;
			for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName()))
			{
				if (mod.getOperation() == 0)
				{
					toughnessMod += mod.getAmount();
				}
				else
				{
					toughnessMod *= mod.getAmount();
				}
			}

			if (player instanceof EntityPlayerMP)
			{
				if (armor.attemptDamageItem((int) (amount / 2), player.world.rand, (EntityPlayerMP) player))
				{
					armor.setCount(0);
				}
			}
			
			return amount * (1 / toughnessMod);
		}
	}
}

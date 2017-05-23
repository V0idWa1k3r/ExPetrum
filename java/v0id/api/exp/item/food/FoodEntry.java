package v0id.api.exp.item.food;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.realmsclient.util.Pair;

import v0id.api.exp.player.Nutrient;

public class FoodEntry
{
	// Essentially metadata for the item. Use in your model registering.
	private final int id;
	
	// Per 100 grams
	private Map<Nutrient, Float> nutrientData = Maps.newHashMap();
	private float caloriesRestored;
	
	// Volume in inventory
	private Pair<Byte, Byte> volume = Pair.of((byte)1, (byte)1);
	
	// In grams
	private float maxWeight = 10000;
	
	// With a multiplier of 1.0 and health of 100 this food will rot in 1 month
	private float rotMultiplier = 1.0F;
	private float baseHealth = 100;
	
	// Display name
	private String unlocalizedName;
	
	public static final List<FoodEntry> allEntries = Lists.newArrayList();
	
	public FoodEntry()
	{
		this.id = index++;
		allEntries.add(this);
	}
	
	public FoodEntry withName(String s)
	{
		this.setUnlocalizedName(s);
		return this;
	}
	
	public FoodEntry withHealth(float f)
	{
		this.setBaseHealth(f);
		return this;
	}
	
	public FoodEntry withRotMultiplier(float f)
	{
		this.setMaxWeight(f);
		return this;
	}
	
	public FoodEntry withMaxWeight(float f)
	{
		this.setMaxWeight(f);
		return this;
	}
	
	public FoodEntry withVolume(byte vx, byte vy)
	{
		this.setVolume(Pair.of(vx, vy));
		return this;
	}
	
	public FoodEntry withVolume(Pair<Byte, Byte> volume)
	{
		this.setVolume(volume);
		return this;
	}
	
	public FoodEntry withCalories(float f)
	{
		this.setCaloriesRestored(f);
		return this;
	}
	
	public FoodEntry withNutrient(Nutrient n, Float f)
	{
		this.getNutrientData().put(n, f);
		return this;
	}
	
	public FoodEntry withNutrients(Map<Nutrient, Float> map)
	{
		this.setNutrientData(map);
		return this;
	}
	
	public float getCaloriesRestored()
	{
		return this.caloriesRestored;
	}

	public void setCaloriesRestored(float caloriesRestored)
	{
		this.caloriesRestored = caloriesRestored;
	}

	public int getId()
	{
		return this.id;
	}

	public Map<Nutrient, Float> getNutrientData()
	{
		return this.nutrientData;
	}

	public void setNutrientData(Map<Nutrient, Float> nutrientData)
	{
		this.nutrientData = nutrientData;
	}

	public Pair<Byte, Byte> getVolume()
	{
		return this.volume;
	}

	public void setVolume(Pair<Byte, Byte> volume)
	{
		this.volume = volume;
	}

	public float getMaxWeight()
	{
		return this.maxWeight;
	}

	public void setMaxWeight(float maxWeight)
	{
		this.maxWeight = maxWeight;
	}

	public float getRotMultiplier()
	{
		return this.rotMultiplier;
	}

	public void setRotMultiplier(float rotMultiplier)
	{
		this.rotMultiplier = rotMultiplier;
	}

	public float getBaseHealth()
	{
		return this.baseHealth;
	}

	public void setBaseHealth(float baseHealth)
	{
		this.baseHealth = baseHealth;
	}

	public String getUnlocalizedName()
	{
		return this.unlocalizedName;
	}

	public void setUnlocalizedName(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
	}

	private static int index;
	
	// Mind the multiplier of 4. It is a 'balance multiplier' as I call it. Very unrealistic but needed so it is not frustrating to gather food.
	// Without it you would need 212 crops growing CONSTANTLY to even allow bare SURVIVAL.
	// Yeah. Really puts those huge crop fields in real life into perspective as I took all numbers from wiki.
	public static final FoodEntry BARLEY = new FoodEntry()
			.withCalories(354 * 4);
}

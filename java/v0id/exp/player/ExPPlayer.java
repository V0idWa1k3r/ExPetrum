package v0id.exp.player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants.NBT;
import v0id.api.core.util.nbt.NBTChain;
import v0id.api.core.util.nbt.NBTList;
import v0id.api.exp.player.BodyPart;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.player.IDisease;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.player.IModifier;
import v0id.api.exp.player.ModifierCollection;
import v0id.api.exp.player.Nutrient;
import v0id.exp.net.PacketHandlerPlayerData;

public class ExPPlayer implements IExPPlayer
{
	public boolean serverIsDirty;
	public boolean clientIsDirty = true;
	public boolean isRemote;
	public EntityPlayer owner;
	public float health;
	public float maxHealth;
	public Multimap<ModifierCollection, IModifier> modifiers = TreeMultimap.create();
	public float calories;
	public Map<Nutrient, Float> nutritionLevels = Maps.newEnumMap(Nutrient.class);
	public float thirst;
	public float maxThirst;
	public float temperature;
	public Map<BodyPart, Float> partsState = Maps.newEnumMap(BodyPart.class);
	public Multimap<BodyPart, String> partsData = HashMultimap.create();
	public List<IDisease> diseases = Lists.newArrayList();
	public boolean isFemale;
	public EnumPlayerProgression stage = EnumPlayerProgression.STONE_AGE;
	
	public boolean health_isDirty;
	public boolean modifiers_isDirty;
	public boolean maxHealth_isDirty;
	public boolean calories_isDirty;
	public boolean nutritionLevels_isDirty;
	public boolean thirst_isDirty;
	public boolean maxThirst_isDirty;
	public boolean temperature_isDirty;
	public boolean partsState_isDirty;
	public boolean partsData_isDirty;
	public boolean diseases_isDirty;
	public boolean stage_isDirty;
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		this.setAllDirty(true);
		return this.serializePartialNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.deserializePartialNBT(nbt);
	}

	public NBTTagCompound serializePartialNBT()
	{
		NBTTagCompound ret = new NBTTagCompound();
		ret.setString("owner", this.owner.getName());
		if (this.health_isDirty)
		{
			ret.setFloat("health", this.health);
		}
		
		if (this.maxHealth_isDirty)
		{
			ret.setFloat("maxHealth", this.maxHealth);
		}
		
		if (this.modifiers_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			for (Map.Entry<ModifierCollection, IModifier> modEntry : this.modifiers.entries())
			{
				if (this.modifiers_isDirty)
				{
					NBTTagCompound tag = modEntry.getValue().serializeNBT();
					tag.setByte("_collection", (byte) modEntry.getKey().ordinal());
					tag.setString("_clazz", modEntry.getValue().getClass().getName());
					lst.appendTag(tag);
				}
			}
			
			ret.setTag("modifiers", lst);
		}
		
		if (this.calories_isDirty)
		{
			ret.setFloat("calories", this.calories);
		}
		
		if (this.nutritionLevels_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			for (Map.Entry<Nutrient, Float> nutEntry : this.nutritionLevels.entrySet())
			{
				NBTTagFloat tag = new NBTTagFloat(nutEntry.getValue());
				lst.appendTag(tag);
			}
			
			ret.setTag("nutritionLevels", lst);
		}
		
		if (this.thirst_isDirty)
		{
			ret.setFloat("thirst", this.thirst);
		}
		
		if (this.maxThirst_isDirty)
		{
			ret.setFloat("maxThirst", this.maxThirst);
		}
		
		if (this.temperature_isDirty)
		{
			ret.setFloat("temperature", this.temperature);
		}
		
		if (this.partsState_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			for (Map.Entry<BodyPart, Float> partEntry : this.partsState.entrySet())
			{
				NBTTagFloat tag = new NBTTagFloat(partEntry.getValue());
				lst.appendTag(tag);
			}
			
			ret.setTag("partsState", lst);
		}
		
		if (this.partsData_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			for (Map.Entry<BodyPart, String> partDataEntry : this.partsData.entries())
			{
				NBTTagCompound tag = NBTChain.startChain().withByte("part", (byte) partDataEntry.getKey().ordinal()).withString("value", partDataEntry.getValue()).endChain();
				lst.appendTag(tag);
			}
			
			ret.setTag("partsData", lst);
		}
		
		if (this.diseases_isDirty)
		{
			NBTTagList lst = new NBTTagList();
			for (IDisease disease : this.diseases)
			{
				NBTTagCompound tag = disease.serializeNBT();
				tag.setString("_clazz", disease.getClass().getName());
				lst.appendTag(tag);
			}
			
			ret.setTag("diseases", lst);
		}
		
		ret.setBoolean("isFemale", this.isFemale);
		
		if (this.stage_isDirty)
		{
			ret.setByte("stage", (byte) this.stage.ordinal());
		}
		
		this.setAllDirty(false);
		return ret;
	}
	
	public void deserializePartialNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("health"))
		{
			this.health = nbt.getFloat("health");
		}
		
		if (nbt.hasKey("maxHealth"))
		{
			this.maxHealth = nbt.getFloat("maxHealth");
		}
		
		if (nbt.hasKey("modifiers"))
		{
			this.modifiers.clear();
			NBTTagList lst = nbt.getTagList("modifiers", NBT.TAG_COMPOUND);
			for (NBTTagCompound tag : new NBTList<NBTTagCompound>(lst))
			{
				try
				{
					Class<IModifier> clazz = (Class<IModifier>) Class.forName(tag.getString("_clazz"));
					IModifier instance = clazz.newInstance();
					instance.deserializeNBT(tag);
					this.modifiers.put(ModifierCollection.values()[tag.getByte("_collection")], instance);
				}
				catch (Exception ex)
				{
					continue;
				}
			}
		}
		
		if (nbt.hasKey("calories"))
		{
			this.calories = nbt.getFloat("calories");
		}
		
		if (nbt.hasKey("nutritionLevels"))
		{
			NBTTagList lst = nbt.getTagList("nutritionLevels", NBT.TAG_FLOAT);
			for (int i = 0; i < lst.tagCount(); ++i)
			{
				this.nutritionLevels.put(Nutrient.values()[i], lst.getFloatAt(i));
			}
		}
		
		if (nbt.hasKey("thirst"))
		{
			this.thirst = nbt.getFloat("thirst");
		}
		
		if (nbt.hasKey("maxThirst"))
		{
			this.maxThirst = nbt.getFloat("maxThirst");
		}
		
		if (nbt.hasKey("temperature"))
		{
			this.temperature = nbt.getFloat("temperature");
		}
		
		if (nbt.hasKey("partsState"))
		{
			NBTTagList lst = nbt.getTagList("partsState", NBT.TAG_FLOAT);
			for (int i = 0; i < lst.tagCount(); ++i)
			{
				this.partsState.put(BodyPart.values()[i], lst.getFloatAt(i));
			}
		}
		
		if (nbt.hasKey("partsData"))
		{
			this.partsData.clear();
			NBTList.<NBTTagCompound>of(nbt.getTagList("partsData", NBT.TAG_COMPOUND)).forEach(tag -> this.partsData.put(BodyPart.values()[tag.getByte("part")], tag.getString("value")));
		}
		
		if (nbt.hasKey("diseases"))
		{
			this.diseases.clear();
			for (NBTTagCompound tag : NBTList.<NBTTagCompound>of(nbt.getTagList("diseases", NBT.TAG_COMPOUND)))
			{
				try
				{
					Class<IDisease> clazz = (Class<IDisease>) Class.forName(tag.getString("_clazz"));
					IDisease disease = clazz.newInstance();
					disease.deserializeNBT(tag);
					this.diseases.add(disease);
				}
				catch(Exception ex)
				{
					continue;
				}
			}
		}
		
		this.isFemale = nbt.getBoolean("isFemale");
		
		if (nbt.hasKey("stage"))
		{
			this.stage = EnumPlayerProgression.values()[nbt.getByte("stage")];
		}
	}
	
	@Override
	public void sendNBT()
	{
		if (!this.isRemote)
		{
			this.serverIsDirty = true;
		}
	}

	@Override
	public void requestNBT()
	{
		if (this.isRemote)
		{
			this.clientIsDirty = true;
		}
	}

	@Override
	public EntityPlayer getOwner()
	{
		return this.owner;
	}

	@Override
	public float getCurrentHealth()
	{
		return this.health;
	}

	@Override
	public float getMaxHealth(boolean includeModifiers)
	{
		if (!includeModifiers)
		{
			return this.maxHealth;
		}
		else
		{
			float ret = this.maxHealth;
			for (IModifier mod : this.modifiers.get(ModifierCollection.MAX_HEALTH))
			{
				ret = this.applyModifier(ret, mod);
			}
			
			return ret;
		}
	}

	@Override
	public void setHealth(float newValue, boolean fatal, boolean clamp)
	{
		this.health_isDirty |= this.health != newValue;
		this.serverIsDirty |= this.health_isDirty;
		this.health = clamp ? MathHelper.clamp(newValue, fatal ? 0 : 1, this.getMaxHealth(true)) : fatal ? newValue : Math.max(1, newValue);
	}

	@Override
	public void setBaseMaxHealth(float newValue)
	{
		assert newValue > 0 : new IllegalArgumentException("Maximum health can't be 0 or negative!");
		this.maxHealth_isDirty |= this.maxHealth != newValue;
		this.serverIsDirty |= this.maxHealth_isDirty;
		this.maxHealth = newValue;
	}

	@Override
	public void addModifier(IModifier mod, ModifierCollection collection)
	{
		this.modifiers.put(collection, mod);
		this.modifiers_isDirty = true;
		this.serverIsDirty |= this.modifiers_isDirty;
	}

	@Override
	public ImmutableCollection<IModifier> getAllModifiers(ModifierCollection collection)
	{
		return ImmutableList.copyOf(this.modifiers.get(collection));
	}

	@Override
	public float getCalories()
	{
		return this.calories;
	}

	@Override
	public float getNutritionLevel(Nutrient nutrient)
	{
		return this.nutritionLevels.get(nutrient);
	}

	@Override
	public void setCalories(float newValue)
	{
		this.calories_isDirty |= newValue != this.calories;
		this.serverIsDirty |= this.calories_isDirty;
		this.calories = newValue;
	}

	@Override
	public void setNutritionLevel(float newValue, Nutrient nutrient)
	{
		this.nutritionLevels_isDirty |= newValue != this.nutritionLevels.get(nutrient);
		this.serverIsDirty |= this.nutritionLevels_isDirty;
		this.nutritionLevels.put(nutrient, newValue);
	}

	@Override
	public float getThirst()
	{
		return this.thirst;
	}

	@Override
	public void setThirst(float newVal, boolean clamp)
	{
		this.thirst_isDirty |= newVal != this.thirst;
		this.serverIsDirty |= this.thirst_isDirty;
		this.thirst = clamp ? MathHelper.clamp(newVal, 0, this.getMaxThirst(true)) : newVal;
	}

	@Override
	public float getMaxThirst(boolean includeModifiers)
	{
		if (!includeModifiers)
		{
			return this.maxThirst;
		}
		
		float ret = this.maxThirst;
		for (IModifier mod : this.modifiers.get(ModifierCollection.MAX_THIRST))
		{
			ret = this.applyModifier(ret, mod);
		}
		
		return ret;
	}

	@Override
	public float getCurrentTemperature()
	{
		return this.temperature;
	}

	@Override
	public void setCurrentTemperature(float newVal)
	{
		this.temperature_isDirty |= newVal != this.temperature;
		this.serverIsDirty |= this.temperature_isDirty;
		this.temperature = newVal;
	}

	@Override
	public float getState(BodyPart part)
	{
		return this.partsState.get(part);
	}

	@Override
	public void setState(BodyPart part, float newVal)
	{
		this.partsState_isDirty |= newVal != this.partsState.get(part);
		this.serverIsDirty |= this.partsState_isDirty;
		this.partsState.put(part, newVal);
	}

	@Override
	public Collection<String> getAttachedData(BodyPart part)
	{
		return this.partsData.values();
	}

	@Override
	public void notifyOfAttachedDataChange()
	{
		this.partsData_isDirty = true;
	}

	@Override
	public ImmutableCollection<IDisease> getDiseases()
	{
		return ImmutableList.copyOf(this.diseases);
	}

	@Override
	public void addDisease(IDisease toAdd)
	{
		this.diseases_isDirty = this.serverIsDirty = true;
		this.diseases.add(toAdd);
	}

	@Override
	public void removeDisease(IDisease toRemove)
	{
		this.diseases_isDirty = this.serverIsDirty = true;
		this.diseases.remove(toRemove);
	}

	@Override
	public void onTick()
	{
		if (this.owner.world.isRemote && this.clientIsDirty)
		{
			this.clientIsDirty = false;
			PacketHandlerPlayerData.sendRequestPacket();
		}
		
		if (!this.owner.world.isRemote && this.serverIsDirty)
		{
			this.serverIsDirty = false;
			PacketHandlerPlayerData.sendSyncPacket((EntityPlayerMP) this.owner);
		}
	}

	@Override
	public boolean isFemale()
	{
		return this.isFemale;
	}
	
	public float applyModifier(float base, IModifier mod)
	{
		switch (mod.getOperator(this))
		{
			case ADD:
			{
				return base + mod.getValue(this);
			}
			case SUBTRACT:
			{
				return base - mod.getValue(this);
			}
			case MULTIPLY:
			{
				return base * mod.getValue(this);
			}
			case DIVIDE:
			{
				return base / mod.getValue(this);
			}
			case POW:
			{
				return (float) Math.pow(base, mod.getValue(this));
			}
			case ROOT:
			{
				return (float) Math.pow(base, 1D / mod.getValue(this));
			}
			default:
			{
				return base;
			}
		}
	}
	
	public void setAllDirty(boolean b)
	{
		this.health_isDirty = this.modifiers_isDirty = this.maxHealth_isDirty = this.calories_isDirty = this.nutritionLevels_isDirty = this.thirst_isDirty = this.maxThirst_isDirty = this.temperature_isDirty = this.partsState_isDirty = this.partsData_isDirty = this.diseases_isDirty = this.stage_isDirty = b;
	}
	
	public static ExPPlayer createDefault()
	{
		ExPPlayer ret = new ExPPlayer();
		for (Nutrient n : Nutrient.values())
		{
			ret.nutritionLevels.put(n, 100f);
		}
		
		ret.health = ret.maxHealth = 5000f;
		ret.calories = 2000;
		ret.thirst = ret.maxThirst = 3000;
		ret.temperature = 36.6F;
		for (BodyPart part : BodyPart.values())
		{
			ret.partsState.put(part, 100f);
			ret.partsData.put(part, "");
		}
		
		return ret;
	}
	
	public static ExPPlayer createDefaultWithOwner(final EntityPlayer owner)
	{
		final ExPPlayer defaultImpl = createDefault();
		defaultImpl.owner = owner;
		defaultImpl.isRemote = !(owner instanceof EntityPlayerMP);
		return defaultImpl;
	}

	@Override
	public EnumPlayerProgression getProgressionStage()
	{
		return this.stage;
	}

	@Override
	public void triggerStage(EnumPlayerProgression progression)
	{
		if (progression.ordinal() - this.stage.ordinal() == 1)
		{
			this.serverIsDirty = this.stage_isDirty = true;
			this.stage = progression;
		}
	}
}

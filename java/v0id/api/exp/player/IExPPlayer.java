package v0id.api.exp.player;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IExPPlayer extends INBTSerializable<NBTTagCompound>
{
	/**
	 * Marks the data as 'needs to send some or all of it's data to client'.
	 * <br>Only functional on server.
	 * <br>Default implementation is thread safe
	 */
	void sendNBT();
	
	/**
	 * Marks the data as 'needs to recieve all of it's data from the server'
	 * <br>Only functional on client
	 * <br>Default implementation is thread safe
	 */
	void requestNBT();
	
	/**
	 * Gets the player that holds this data. Usually this is pointless as the data is a capability.
	 * <br> Default implementation will distinguish between server and client and return EntityPlayerMP or EntityPlayerSP(EntityOtherPlayerMP) respectively.
	 * @return The player holding this data
	 */
	EntityPlayer getOwner();
	
	/**
	 * Gets the current health of the player.
	 * @return The health of the player
	 */
	float getCurrentHealth();
	
	/**
	 * Gets the maximum health of the player.
	 * @param includeModifiers : should modifiers for max health be included in the calculation or should the base max health be returned?
	 * @return The max health value of the player
	 */
	float getMaxHealth(boolean includeModifiers);
	
	/**
	 * Sets the health of the player to the value passed.
	 * @param newValue : the new health to set current health to.
	 * @param fatal : if the newValue is &lt;= 0 (less or equal) should the player die
	 * @param clamp : should perform {@link net.minecraft.util.math.MathHelper#clamp} before setting the value
	 */
	void setHealth(float newValue, boolean fatal, boolean clamp);
	
	/**
	 * Sets the base max health of the player to a specific value and updates all modifiers. 
	 * <br> Refrain from using this method directly, use modifiers.
	 * @param newValue : the new max health of the player. Must at least be 1
	 */
	void setBaseMaxHealth(float newValue);
	
	/**
	 * Adds a modifier to the list of modifiers. Sorts the values and recalculates.
	 * <br> There currently is no way apart reflection of removing the modifier!
	 * @param mod : the modifier to add
	 * @param collection : the collection to add modifier to
	 */
	void addModifier(IModifier mod, ModifierCollection collection);

	/**
	 * Gets a view of all current modifiers in a collection. 
	 * @param collection : the collection to get a view of
	 * @return An ImmutableCollection of current modifiers
	 */
	ImmutableCollection<IModifier> getAllModifiers(ModifierCollection collection);
	
	/**
	 * Getter for the current player's calories (food)
	 * @return An amount of calories player has
	 */
	float getCalories();
	
	/**
	 * Getter for the specific nutrition level.
	 * @param nutrient : the Nutrient to get the level of
	 * @return The level of a specific nutrient
	 */
	float getNutritionLevel(Nutrient nutrient);
	
	/**
	 * Setter for the player's calories (food)
	 * @param newValue : the value to set
	 */
	void setCalories(float newValue);
	
	/**
	 * Setter for the specific nutrition level
	 * @param newValue : the value to set
	 * @param nutrient : the nutrient to set the level of
	 */
	void setNutritionLevel(float newValue, Nutrient nutrient);
	
	/**
	 * Getter for player's thirst level.
	 * @return The thirst level for the player
	 */
	float getThirst();
	
	/**
	 * Setter for the player's thirst level
	 * @param newVal : the value to set
	 * @param clamp : should clamp the newVal between 0 and max?
	 */
	void setThirst(float newVal, boolean clamp);
	
	/**
	 * Getter for the player's max thirst level
	 * @param includeModifiers : should max thirst level modifiers be included in the calculation?
	 * @return The maximum thirst level for the player
	 */
	float getMaxThirst(boolean includeModifiers);
	
	/**
	 * Getter for the player's body temperature. This includes modifiers by default.
	 * @return The current player's body temperature
	 */
	float getCurrentTemperature();
	
	/**
	 * Setter for the player's body temperature. Please use modifiers instead of this method.
	 * @param newVal : the value to set
	 */
	void setCurrentTemperature(float newVal);
	
	/**
	 * Getter for the state(health) of a specific body part of the player.
	 * @param part : the part to get the state of
	 * @return A state (health) of a specified part.
	 */
	float getState(BodyPart part);
	
	/**
	 * Setter for the state(health) of a specific body part of the player.
	 * @param part : the part to set the state for
	 * @param newVal : new value to set
	 */
	void setState(BodyPart part, float newVal);
	
	/**
	 * Getter of the data of a specific body part.
	 * <br>It is simply an list of strings to store any data whatsoever(like broken state for example).
	 * @param part : the part to get the state of
	 * @return A mutable(modifiable) collection of data for the part.
	 */
	Collection<String> getAttachedData(BodyPart part);
	
	/**
	 * Notfies the server that the data for the specified body part has changed and needs sync.
	 * <br>Please call manually every time you modify the List!
	 * <br>Note that the state is synchronized automatically and does not need refreshing via this method.
	 */
	void notifyOfAttachedDataChange();
	
	/**
	 * Getter for the diseases the player has
	 * @return An immutable collection of diseases
	 */
	ImmutableCollection<IDisease> getDiseases();
	
	/**
	 * Adds a disease to the player's collection of diseases
	 * @param toAdd : the disease to add
	 */
	void addDisease(IDisease toAdd);
	
	/**
	 * Removes a disease from the player's collection of diseases
	 * @param toRemove : the disease to remove
	 */
	void removeDisease(IDisease toRemove);
	
	/**
	 * Happens each tick
	 */
	void onTick();
	
	/**
	 * Is the player character a female?
	 * @return A boolean value indicating whether player's character is a female or not
	 */
	boolean isFemale();
	
	/**
	 * Gets the progression stage of the player.
	 * @return The stage the player is considered to be at
	 */
	EnumPlayerProgression getProgressionStage();
	
	/**
	 * If the player is 1 stage below the specified progression age then sets the player's progression to that stage.
	 * <br> Example: If the player is at stone age and this is triggered with BRONZE_AGE nothing will happen as the player is 2 stages behind.
	 * <br> If the player's stage however is CHALCOLITHIC and the same BRONZE_AGE is triggered the player's age will indeed be set to bronze.
	 * @param progression : the stage to try to trigger.
	 */
	void triggerStage(EnumPlayerProgression progression);
	
	/**
	 * Resets the data back to default values. Used upon player's death
	 */
	void resetData();
	
	/**
	 * Handles the situation where some ticks have been skipped by the server (most likely the player slept)
	 * @param skipped - the amount of ticks skipped
	 */
	void skipTicks(int skipped);
	
	// ==========================================Future additions========================================================//
	/*
	float getEnergy();
	
	void setEnergy(float newVal, boolean clamp);
	
	SkillTable getSkillTable();
	
	void notifySkillTableChange();
	*/
	
	/**
	 * A simple helper to get the data of a specified player
	 * @param p : the player to get the data of
	 * @return The data attached to a specified player
	 */
	public static IExPPlayer of(EntityPlayer p)
	{
		return p.getCapability(ExPPlayerCapability.playerCap, null);
	}
}

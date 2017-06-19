package v0id.api.exp.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import v0id.api.exp.player.FoodGroup;
import v0id.api.exp.world.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 * A capability interface for animals and other creatures(ExPetrum considers all creatures added by itself to be animals so they all have this capability)
 * @author V0idWa1k3r
 */
public interface IAnimal extends INBTSerializable<NBTTagCompound>
{
    /**
     * Gets the entity this capability is bound to.
     * @return - The entity this capability is bound to.
     */
    EntityLivingBase getOwner();

    /**
     * Fired as the entity ticks to do animal logic.
     */
    void onTick();

    /**
     * Gets the gender of this animal. This value is synced to the client.
     * @return - The gender of this animal
     */
    EnumGender getGender();

    /**
     * Sets the gender of this animal.
     * @param newGender : The gender to set.
     */
    void setGender(EnumGender newGender);

    /**
     * Gets the age of this animal. This value is synced to the client.
     * @return - The age of this animal in ticks.
     */
    long getAge();

    /**
     * Sets the age of this animal.
     * @param newAge : the age to set in ticks.
     */
    void setAge(long newAge);

    /**
     * Gets the chance that this animal dies each morning. This is affected by {@link #getAge() animal's age}, {@link #getThirst() animal's thirst level}, {@link #getFood() animal's hunger level} and {@link #isSick() whether the animal is sick or not}
     * @return - The chance [0-1] of this animal to die next morning.
     */
    float getDailyDeathChance();

    /**
     * Gets the last tick this animal ticked at. Used for handling sudden time changes.
     * @return - The last tick this animal ticked at.
     */
    Calendar getLastTickTime();

    /**
     * Sets the last tick this animal ticked at.
     * @param lastTickTime : the tick to set.
     */
    void setLastTickTime(long lastTickTime);

    /**
     * Is this capability changed and needs to be synced to the clients(only accounts for the values the client needs to know)
     * @return - a boolean value indicating whether this entity needs to be synced to the clients
     */
    boolean isDirty();

    /**
     * Gets the individual familiarity(reputation) level this entity has towards a certain player. This has nothing to do with pack's reputation of the player though both methods are used together in most cases. This value is synced to the appropriate client(the client is aware of the familiarity levels towards itself but not other clients).
     * @param of : the player to set the familiarity of
     * @return - the current level of familiarity this entity has with the player [-100 - 100]
     */
    float getFamiliarity(@Nonnull EntityPlayer of);

    /**
     * Gets the max familiarity level this entity can have towards players. This value is defined in specific entity's implementation and is usually 100 for domesticated animals.
     * @return - The max familiarity level this entity can have towards players [-100 - 100].
     */
    float getMaxFamiliarity();

    /**
     * Sets the familiarity levels towards a specified player. This is clamped in [-100 - {@link #getMaxFamiliarity()} max] bounds.
     * @param of : the player to set the familiarity of.
     * @param newFamiliarity : the new value.
     */
    void setFamiliarity(EntityPlayer of, float newFamiliarity);

    /**
     * Gets the position this animal considers 'home' and will be coming to periodically. For wild animals it is their nest/current migration point. For domesticated animals this is the place they slept at last night. For animals that are not domesticated but belong to player's pack it is player's last known position.
     * @return - The position this animal considers 'home'.
     */
    BlockPos getHome();

    /**
     * Sets the new 'home' position for this animal. See {@link #getHome() getHome()} for more documentation.
     * @param newPos : the new 'home' position to set.
     */
    void setHome(BlockPos newPos);

    /**
     * Gets the pack info of this entity. This can be null for solitary entities!
     * @return - The pack info for this animal if it belongs to any pack, null otherwise.
     */
    @Nullable
    IPackInfo getPack();

    /**
     * Sets the pack this entity belongs to, removing it from the pack it previously belonged to if any. Calls appropriate methods at {@link v0id.api.exp.entity.IPackInfo IPackInfo}.
     * @param pack : the new pack this entity should belong to. Can be null to make this animal solitary.
     */
    void setPack(@Nullable IPackInfo pack);

    /**
     * Gets the food groups this animal considers 'favourite'. Usually [VEGETABLE, GRAIN] for herbivores and [PROTEIN] for carnivores but some exceptions exist. This is defined in a particular entity's implementation and the client is aware of that definement.
     * @return - The array of food this entity will eat. Will always contain at least 1 entry!
     */
    FoodGroup[] getFavouriteFood();

    /**
     * Checks if the entity can be bred with other entity(they are of the same implementation, different genders, fertile and are not currently pregnant)
     * @param other : the other partner to check.
     * @return - a boolean value indicating whether this entity can be bred with another entity.
     */
    boolean canBreed(EntityLivingBase other);

    /**
     * Performs the breeding of this entity with another one specified, making the female one pregnant and running all offspring calculations.
     * @param other : the entity to breed with.
     */
    void breed(EntityLivingBase other);

    /**
     * Checks if this entity is currently pregnant. Will always return false for males. This value is synced to the client.
     * @return - a boolean value indicating whether this entity is pregnant.
     */
    boolean isPregnant();

    /**
     * Gets the stats that the offspring of this entity will have. The size of the array is equal to the amount of offspring. This will return null if the entity is not pregnant.
     * @return - the current offspring stats or null if this entity is not pregnant.
     */
    @Nullable
    IAnimalStats[] getOffspringStats();

    /**
     * The same as {@link #breed(EntityLivingBase) breed} but will not run any checks like age and gender conditions and simply make the entity pregnant if at all possible.
     * @param partner : the partner to breed with.
     */
    void setPregnant(EntityLivingBase partner);

    /**
     * Gets the stats of this animal. Parts of this value may be synced with the client depending on the IAnimalStats implementation.
     * @return - The stats of this animal.
     */
    IAnimalStats getStats();

    /**
     * Sets the stats of this animal. This may throw an {@link java.lang.IllegalArgumentException IllegalArgumentException} if the implementation being set is not supported by this animal implementation.
     * @param stats : the new stats to set.
     * @throws IllegalArgumentException if the implementation of the stats parameters is not supported by the animal implementation or if the stats parameter is null.
     */
    void setStats(IAnimalStats stats) throws IllegalArgumentException;

    /**
     * Gets the amount of ticks left before this animal will give birth. This value is synced to the client.
     * @return : The amount of ticks left before this animal will give birth.
     */
    int getPregnancyTicksLeft();

    /**
     * Makes this entity give birth if it is pregnant creating the offspring.
     */
    void giveBirth();

    /**
     * Gets the food/hunger level this entity has.
     * @return : The food/hunger level this entity has [0 - 100]
     */
    float getFood();

    /**
     * Gets the thirst level this entity has.
     * @return : the thirst level this entity has [0 - 100]
     */
    float getThirst();

    /**
     * Sets the food level for this entity.
     * @param newValue : the new food level. Will be clamped to [0 - 100]
     */
    void setFood(float newValue);

    /**
     * Sets the thirst level for this entity.
     * @param newValue : the new thirst level. Will be clamped to [0 - 100]
     */
    void setThirst(float newValue);

    /**
     * Checks if this entity was interacted with today by the player. This value indicates whether this entity was familiarized by the player today and differs for different animal implementations. Some implementations may always return false here.
     * @param player : the player to check.
     * @return - a boolean value indicating whether this entity was interacted with today by the player.
     */
    boolean wasInteractedWith(@Nonnull EntityPlayer player);

    /**
     * Performs the interaction for this entity by the player. This is NOT the player right-clicking the entity but rather the checks and logic as the player finishes familiarizing action for the entity.
     * @param player : the player to perform the interaction for.
     */
    void interact(EntityPlayer player);

    /**
     * Checks if this entity is sick or not. Sick entities have higher chance of {@link #getDailyDeathChance() daily death}.
     * @return - a boolean value indicating whether this entity is sick.
     */
    boolean isSick();

    /**
     * Sets the entity sick or not depending on the argument passed. Sick entities have higher chance of {@link #getDailyDeathChance() daily death}.
     * @param isSick : the new sickness value
     */
    void setSick(boolean isSick);

    /**
     * Checks if this entity is domesticated by the player. Domesticated animals have different AI regarding the player and usually have a 100 max reputation level. Their interactions also differ and their {@link #getPack() pack} is always null.
     * @return - a boolean value indicating whether this entity is domesticated.
     */
    boolean isDomesticated();

    /**
     * Performs all checks and logic for the new day starting. This will get fired even if the time was skipped.
     */
    void onNewDayStarted();

    /**
     * A simple getter for the capability of the entity given.
     * @param entityLivingBase : the entity to get the capability of.
     * @return - this capability implementation for the entity supplied.
     */
    @Nullable
    static IAnimal of(@Nonnull EntityLivingBase entityLivingBase)
    {
        return entityLivingBase.hasCapability(ExPAnimalCapability.animalCap, null) ? entityLivingBase.getCapability(ExPAnimalCapability.animalCap, null) : null;
    }
}

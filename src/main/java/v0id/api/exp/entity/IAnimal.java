package v0id.api.exp.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import v0id.api.exp.player.FoodGroup;
import v0id.api.exp.world.Calendar;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 */
public interface IAnimal extends INBTSerializable<NBTTagCompound>
{
    EntityLivingBase getOwner();

    void onTick();

    EnumGender getGender();

    void setGender(EnumGender newGender);

    long getAge();

    void setAge(long newAge);

    float getDailyDeathChance();

    Calendar getLastTickTime();

    void setLastTickTime(long lastTickTime);

    boolean isDirty();

    float getFamiliarity(EntityPlayer of);

    float getMaxFamiliarity();

    void setFamiliarity(EntityPlayer of, float newFamiliarity);

    BlockPos getHome();

    void setHome(BlockPos newPos);

    IPackInfo getPack();

    void setPack(IPackInfo pack);

    FoodGroup[] getFavouriteFood();

    boolean canBreed(EntityLivingBase other);

    void breed(EntityLivingBase other);

    boolean isPregnant();

    void setPregnant(EntityLivingBase partner);

    IAnimalStats getStats();

    void setStats(IAnimalStats stats);

    int getPregnancyTicksLeft();

    void giveBirth();

    float getFood();

    float getThirst();

    void setFood(float newValue);

    void setThirst(float newValue);

    boolean wasInteractedWith(EntityPlayer player);

    void interact(EntityPlayer player);

    boolean isSick();

    void setSick(boolean isSick);

    boolean isDomesticated();

    void onNewDayStarted();

    @Nullable
    static IAnimal of(EntityLivingBase entityLivingBase)
    {
        return entityLivingBase.hasCapability(ExPAnimalCapability.animalCap, null) ? entityLivingBase.getCapability(ExPAnimalCapability.animalCap, null) : null;
    }
}

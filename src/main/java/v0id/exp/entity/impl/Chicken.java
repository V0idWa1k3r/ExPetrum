package v0id.exp.entity.impl;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.entity.EnumGender;
import v0id.api.exp.entity.IAnimalStats;
import v0id.api.exp.player.FoodGroup;
import v0id.exp.entity.EntityAnimal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class Chicken extends EntityAnimal
{
    public Chicken(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.4F);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public float getEyeHeight()
    {
        return this.height;
    }

    @Nullable
    @Override
    public ResourceLocation getMainTexture(float partialTicks)
    {
        return ExPTextures.entityChickenWildGeneric;
    }

    @Override
    public ResourceLocation getColorableFeaturesTexture(float partialTicks)
    {
        return ExPTextures.entityChickenWildColor;
    }

    @Override
    public ResourceLocation getGenderSpecificTextures(float partialTicks)
    {
        return this.animalCapability.getGender() == EnumGender.MALE ? ExPTextures.entityChickenWildMale : null;
    }

    @Override
    public float[] getFeatureColors(float partialTicks)
    {
        return new float[]{ 1, 1, 1 };
    }

    @Override
    public long getAdulthoodAge()
    {
        return 24000 * 10;
    }

    @Override
    public float getMaxFamiliarity()
    {
        return 0;
    }

    @Override
    public FoodGroup[] getFavouriteFood()
    {
        return new FoodGroup[]{ FoodGroup.GRAIN };
    }

    @Override
    public long getRandomPregnancyTicks()
    {
        return 0;
    }

    @Override
    public IAnimalStats getOrCreateStats()
    {
        return new ChickenStats();
    }

    @Override
    public void setStats(IAnimalStats newStats) throws IllegalArgumentException
    {

    }

    @Override
    public void processInteraction(EntityPlayer interactor)
    {

    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.animalCapability.setGender(this.world.rand.nextBoolean() ? EnumGender.MALE : EnumGender.FEMALE);
        this.animalCapability.setAge(24000 * 30 + this.world.rand.nextInt(24000 * 90));
        return livingdata;
    }

    public static class ChickenStats implements IAnimalStats
    {
        @Override
        public IAnimalStats mix(@Nonnull IAnimalStats other)
        {
            return this;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            return new NBTTagCompound();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {

        }
    }
}

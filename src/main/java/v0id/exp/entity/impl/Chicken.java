package v0id.exp.entity.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
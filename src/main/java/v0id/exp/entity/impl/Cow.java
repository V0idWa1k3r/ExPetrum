package v0id.exp.entity.impl;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.entity.EnumGender;
import v0id.api.exp.entity.IAnimalStats;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.player.EnumFoodGroup;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.entity.EntityAnimal;
import v0id.exp.entity.ExPAITempt;
import v0id.exp.item.ItemFood;
import v0id.exp.item.ItemGeneric;
import v0id.exp.item.ItemWoodenBucket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

public class Cow extends EntityAnimal
{
    private CowStats stats;
    private int milkTimer;

    public Cow(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new ExPAITempt(this, 1, false, EnumFoodGroup.GRAIN));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
    }

    @Nullable
    @Override
    public ResourceLocation getMainTexture(float partialTicks)
    {
        return ExPTextures.entityCowGeneric;
    }

    @Override
    public ResourceLocation getColorableFeaturesTexture(float partialTicks)
    {
        return ExPTextures.entityCowColor;
    }

    @Override
    public ResourceLocation getGenderSpecificTextures(float partialTicks)
    {
        return this.animalCapability.getGender() == EnumGender.FEMALE ? ExPTextures.entityCowFemale : ExPTextures.entityCowMale;
    }

    @Override
    public float[] getFeatureColors(float partialTicks)
    {
        return new float[]{ 1, 1, 1 };
    }

    @Override
    public long getAdulthoodAge()
    {
        return 24000 * 30;
    }

    @Override
    public float getMaxFamiliarity()
    {
        return 100;
    }

    @Override
    public EnumFoodGroup[] getFavouriteFood()
    {
        return new EnumFoodGroup[]{ EnumFoodGroup.GRAIN };
    }

    @Override
    public long getRandomPregnancyTicks()
    {
        Calendar today = IExPWorld.of(this.world).today();
        return today.ticksPerMonth + this.world.rand.nextInt((int) today.ticksPerDay * 10);
    }

    @Override
    public int getOffspringAmount()
    {
        return this.world.rand.nextFloat() < 0.2F ? 2 : 1;
    }

    @Override
    public void giveBirth(IAnimalStats stats)
    {
        Cow c = new Cow(this.world);
        c.animalCapability.setHome(this.getPosition());
        c.animalCapability.setLastTickTime(IExPWorld.of(this.world).today().getTime());
        c.animalCapability.setFamiliarity(0);
        c.animalCapability.setStats(stats);
        c.animalCapability.setGender(this.world.rand.nextBoolean() ? EnumGender.FEMALE : EnumGender.MALE);
        c.animalCapability.setAge(0);
        c.setPosition(this.posX, this.posY, this.posZ);
        if (!this.world.isRemote)
        {
            this.world.spawnEntity(c);
        }
    }

    @Override
    public IAnimalStats getOrCreateStats()
    {
        if (this.stats == null)
        {
            this.stats = new CowStats();
        }

        return this.stats;
    }

    @Override
    public IAnimalStats createNewStats()
    {
        return new CowStats();
    }

    @Override
    public void setStats(IAnimalStats newStats) throws IllegalArgumentException
    {
        if (newStats instanceof CowStats)
        {
            this.stats = (CowStats) newStats;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void processInteraction(EntityPlayer interactor)
    {
        if (!this.processDefaultInteraction(interactor) && !this.world.isRemote)
        {
            ItemStack is = interactor.getHeldItem(EnumHand.MAIN_HAND);
            if (this.milkTimer <= 0 && is.getItem() instanceof ItemWoodenBucket && (is.getMetadata() == 0 || is.getMetadata() == 3) && this.animalCapability.getGender() == EnumGender.FEMALE)
            {
                if (this.animalCapability.getFamiliarity() >= 75F)
                {
                    ItemWoodenBucket bucket = (ItemWoodenBucket) is.getItem();
                    int currentFluid = bucket.getWater(is);
                    if (currentFluid < 10)
                    {
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_COW_MILK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        bucket.setWaterType(is, ExPFluids.milk);
                        bucket.setWater(is, currentFluid + 1);
                        this.milkTimer = (int) IExPWorld.of(this.world).today().ticksPerDay;
                    }
                }
                else
                {
                    interactor.sendMessage(new TextComponentTranslation("exp.txt.notAllowed").setStyle(new Style().setItalic(true)));
                }
            }
        }
    }

    @Override
    public void handleElapsedTicks(long elapsed)
    {
        this.interactionTimer -= elapsed;
        this.milkTimer -= elapsed;
        if (this.world.rand.nextFloat() < 0.03F && this.animalCapability.getAge() % 200 == 0 && this.animalCapability.getGender() == EnumGender.FEMALE && !this.animalCapability.isPregnant() && this.animalCapability.getFamiliarity() >= 10)
        {
            List<Cow> cows = this.world.getEntitiesWithinAABB(Cow.class, new AxisAlignedBB(this.posX - 12, this.posY - 2, this.posZ - 12, this.posX + 12, this.posY + 2, this.posZ + 12));
            for (Cow c : cows)
            {
                if (c.animalCapability.getGender() == EnumGender.MALE)
                {
                    RayTraceResult rtr = this.world.rayTraceBlocks(this.getPositionVector().addVector(0, 1, 0), c.getPositionVector().addVector(0, 1, 0));
                    if (rtr == null || rtr.typeOfHit == RayTraceResult.Type.MISS && this.animalCapability.canBreed(c))
                    {
                        this.animalCapability.breed(c);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        long age = this.animalCapability.getAge();
        this.dropItem(new ItemStack(Items.BONE, 1 + this.world.rand.nextInt(6), 0));
        ItemStack beef = new ItemStack(ExPItems.food, 1, FoodEntry.BEEF_RAW.getId());
        ItemFood food = (ItemFood)beef.getItem();
        food.setTotalWeight(beef, age < this.getAdulthoodAge() ? 100 + this.world.rand.nextInt(100) : 1500 + this.world.rand.nextInt(3000));
        food.setTotalRot(beef, 0);
        food.setLastTickTime(beef, new Calendar(IExPWorld.of(this.world).today().getTime()));
        int hides = age < this.getAdulthoodAge() / 2 ? 1 : age < this.getAdulthoodAge() ? 3 : 8;
        this.dropItem(new ItemStack(ExPItems.generic, hides + this.world.rand.nextInt(hides), ItemGeneric.EnumGenericType.HIDE.ordinal()));
        this.dropItem(beef);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_COW_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected float getSoundPitch()
    {
        return this.animalCapability.getAge() < this.getAdulthoodAge() ? 2 - this.animalCapability.getAge() / this.getAdulthoodAge() : super.getSoundPitch();
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.animalCapability.setGender(this.world.rand.nextBoolean() ? EnumGender.MALE : EnumGender.FEMALE);
        this.animalCapability.setAge(24000 * 60 + this.world.rand.nextInt(24000 * 180));
        return livingdata;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("milkTimer", this.milkTimer);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.milkTimer = compound.getInteger("milkTimer");
    }

    public static class CowStats implements IAnimalStats
    {
        @Override
        public IAnimalStats mix(@Nonnull IAnimalStats other) throws InvalidParameterException
        {
            return new CowStats();
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

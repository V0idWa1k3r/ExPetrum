package v0id.exp.entity.impl;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.entity.EnumGender;
import v0id.api.exp.entity.IAnimalStats;
import v0id.api.exp.item.IShears;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.player.EnumFoodGroup;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.entity.EntityAnimal;
import v0id.exp.entity.ExPAITempt;
import v0id.exp.item.ItemFood;
import v0id.exp.item.ItemGeneric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

public class Sheep extends EntityAnimal
{
    public static final DataParameter<Integer> PARAM_WOOL_TICKS = EntityDataManager.createKey(Sheep.class, DataSerializers.VARINT);
    private SheepStats stats;

    public Sheep(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(PARAM_WOOL_TICKS, 0);
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
        return ExPTextures.entitySheepGeneric;
    }

    @Override
    public ResourceLocation getColorableFeaturesTexture(float partialTicks)
    {
        return ExPTextures.entitySheepColor;
    }

    @Override
    public ResourceLocation getGenderSpecificTextures(float partialTicks)
    {
        return this.animalCapability.getGender() == EnumGender.MALE ? ExPTextures.entitySheepMale : null;
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
        Sheep c = new Sheep(this.world);
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
            this.stats = new SheepStats();
        }

        return this.stats;
    }

    @Override
    public IAnimalStats createNewStats()
    {
        return new SheepStats();
    }

    @Override
    public void setStats(IAnimalStats newStats) throws IllegalArgumentException
    {
        if (newStats instanceof SheepStats)
        {
            this.stats = (SheepStats) newStats;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void processInteraction(EntityPlayer interactor)
    {
        if (!this.processDefaultInteraction(interactor) && !this.world.isRemote && this.getDataManager().get(PARAM_WOOL_TICKS) <= 0)
        {
            ItemStack is = interactor.getHeldItem(EnumHand.MAIN_HAND);
            if (is.getItem() instanceof IShears || is.getItem() instanceof ItemShears)
            {
                if (this.animalCapability.getFamiliarity() >= 75)
                {
                    int amt = is.getItem() instanceof IShears ? ((IShears) is.getItem()).getWoolAmount(this, is) : 2;
                    this.dropItem(new ItemStack(ExPItems.generic, amt, ItemGeneric.EnumGenericType.WOOL.ordinal()));
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL, 1, 1);
                    is.damageItem(1, interactor);
                    this.getDataManager().set(PARAM_WOOL_TICKS, (int) IExPWorld.of(this.world).today().ticksPerDay * 10);
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
        if (!this.world.isRemote)
        {
            this.getDataManager().set(PARAM_WOOL_TICKS, Math.max(0, this.getDataManager().get(PARAM_WOOL_TICKS) - (int)elapsed));
        }
        if (this.world.rand.nextFloat() < 0.03F && this.animalCapability.getAge() % 200 == 0 && this.animalCapability.getGender() == EnumGender.FEMALE && !this.animalCapability.isPregnant() && this.animalCapability.getFamiliarity() >= 10)
        {
            List<Sheep> sheep = this.world.getEntitiesWithinAABB(Sheep.class, new AxisAlignedBB(this.posX - 12, this.posY - 2, this.posZ - 12, this.posX + 12, this.posY + 2, this.posZ + 12));
            for (Sheep c : sheep)
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
        ItemStack lamb = new ItemStack(ExPItems.food, 1, FoodEntry.LAMB_RAW.getId());
        ItemFood food = (ItemFood)lamb.getItem();
        food.setTotalWeight(lamb, age < this.getAdulthoodAge() ? 100 + this.world.rand.nextInt(100) : 900 + this.world.rand.nextInt(1200));
        food.setTotalRot(lamb, 0);
        food.setLastTickTime(lamb, new Calendar(IExPWorld.of(this.world).today().getTime()));
        int hides = age < this.getAdulthoodAge() / 2 ? 1 : age < this.getAdulthoodAge() ? 2 : 5;
        this.dropItem(new ItemStack(ExPItems.generic, hides + this.world.rand.nextInt(hides), ItemGeneric.EnumGenericType.HIDE.ordinal()));
        this.dropItem(lamb);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected float getSoundPitch()
    {
        return this.animalCapability.getAge() < this.getAdulthoodAge() ? 2 - this.animalCapability.getAge() / this.getAdulthoodAge() : super.getSoundPitch();
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
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
        compound.setInteger("woolTimer", this.getDataManager().get(PARAM_WOOL_TICKS));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.getDataManager().set(PARAM_WOOL_TICKS, compound.getInteger("woolTimer"));
    }

    public static class SheepStats implements IAnimalStats
    {
        @Override
        public IAnimalStats mix(@Nonnull IAnimalStats other) throws InvalidParameterException
        {
            return new SheepStats();
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

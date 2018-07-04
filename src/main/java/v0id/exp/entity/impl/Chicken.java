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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.entity.EnumGender;
import v0id.api.exp.entity.IAnimalStats;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.player.EnumFoodGroup;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.block.BlockNestingBox;
import v0id.exp.entity.EntityAnimal;
import v0id.exp.entity.ExPAITempt;
import v0id.exp.item.ItemFood;
import v0id.exp.tile.TileNestingBox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class Chicken extends EntityAnimal
{
    private ChickenStats stats;
    private int eggTimer;
    private BlockPos nestingBoxPos;

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
        this.tasks.addTask(2, new ExPAITempt(this, 1, false, EnumFoodGroup.GRAIN));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
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
        return 100F;
    }

    @Override
    public EnumFoodGroup[] getFavouriteFood()
    {
        return new EnumFoodGroup[]{ EnumFoodGroup.GRAIN };
    }

    @Override
    public long getRandomPregnancyTicks()
    {
        return 0;
    }

    @Override
    public int getOffspringAmount()
    {
        return 1;
    }

    @Override
    public void giveBirth(IAnimalStats stats)
    {
    }

    @Override
    public IAnimalStats getOrCreateStats()
    {
        if (this.stats == null)
        {
            this.stats = new ChickenStats();
        }

        return this.stats;
    }

    @Override
    public void setStats(IAnimalStats newStats) throws IllegalArgumentException
    {
        if (newStats instanceof ChickenStats)
        {
            this.stats = (ChickenStats) newStats;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void processInteraction(EntityPlayer interactor)
    {
        this.processDefaultInteraction(interactor);
    }

    @Override
    public void handleElapsedTicks(long elapsed)
    {
        this.eggTimer += elapsed;
        this.interactionTimer -= elapsed;
        if (this.eggTimer >= ((ChickenStats)this.getOrCreateStats()).eggTimer && this.animalCapability.getAge() >= this.getAdulthoodAge())
        {
            this.eggTimer = 0;
            this.makeEgg();
        }

        if (this.nestingBoxPos == null || this.nestingBoxPos.distanceSq(this.getPosition()) >= 1024)
        {
            BlockPos randomPos = this.getPosition().add(this.world.rand.nextInt(16) - this.world.rand.nextInt(16), this.world.rand.nextInt(2) - this.world.rand.nextInt(2),this.world.rand.nextInt(16) - this.world.rand.nextInt(16));
            if (this.world.isBlockLoaded(randomPos) && this.world.getBlockState(randomPos).getBlock() instanceof BlockNestingBox)
            {
                this.nestingBoxPos = randomPos;
            }
        }
    }

    public void makeEgg()
    {
        if (this.nestingBoxPos != null && this.nestingBoxPos.distanceSq(this.getPosition()) < 1024 && this.animalCapability.getGender() == EnumGender.FEMALE && this.animalCapability.getFamiliarity() >= 25F)
        {
            TileEntity tile = this.world.getTileEntity(this.nestingBoxPos);
            if (tile instanceof TileNestingBox)
            {
                for (int i = 0; i < ((TileNestingBox) tile).inventory.getSlots(); ++i)
                {
                    if (((TileNestingBox) tile).inventory.getStackInSlot(i).isEmpty())
                    {
                        ItemStack item = new ItemStack(ExPItems.food, 1, FoodEntry.EGG.getId());
                        ItemFood food = (ItemFood)item.getItem();
                        food.setTotalWeight(item, ((ChickenStats)this.getOrCreateStats()).eggWeight);
                        food.setLastTickTime(item, new Calendar(IExPWorld.of(this.world).today().getTime()));
                        food.setTotalRot(item, 0.0F);
                        List<Chicken> nearbyChickens = this.world.getEntitiesWithinAABB(Chicken.class, new AxisAlignedBB(this.posX - 12, this.posY - 2, this.posZ - 12, this.posX + 12, this.posY + 2, this.posZ + 12));
                        for (Chicken c : nearbyChickens)
                        {
                            if (c.animalCapability.getGender() == EnumGender.MALE)
                            {
                                item.getTagCompound().setBoolean("fertilized", true);
                                item.getTagCompound().setTag("statsOffspring", c.stats.mix(this.stats).serializeNBT());
                                break;
                            }
                        }

                        ((TileNestingBox) tile).inventory.setStackInSlot(i, item);
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        return;
                    }
                }
            }
            else
            {
                this.nestingBoxPos = null;
            }
        }
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.animalCapability.setGender(this.world.rand.nextBoolean() ? EnumGender.MALE : EnumGender.FEMALE);
        this.animalCapability.setAge(24000 * 30 + this.world.rand.nextInt(24000 * 90));
        return livingdata;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("eggTimer", this.eggTimer);
        if (this.nestingBoxPos != null)
        {
            compound.setInteger("nbX", this.nestingBoxPos.getX());
            compound.setInteger("nbY", this.nestingBoxPos.getY());
            compound.setInteger("nbZ", this.nestingBoxPos.getZ());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.eggTimer = compound.getInteger("eggTimer");
        if (compound.hasKey("nbX"))
        {
            this.nestingBoxPos = new BlockPos(compound.getInteger("nbX"), compound.getInteger("nbY"), compound.getInteger("nbZ"));
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        long age = this.animalCapability.getAge();
        this.dropItem(new ItemStack(Items.FEATHER, 4 + this.world.rand.nextInt(5), 0));
        this.dropItem(new ItemStack(Items.BONE, 2 + this.world.rand.nextInt(8), 0));
        ItemStack chicken = new ItemStack(ExPItems.food, 1, FoodEntry.CHICKEN_RAW.getId());
        ItemFood food = (ItemFood)chicken.getItem();
        food.setTotalWeight(chicken, age < this.getAdulthoodAge() ? 100 + this.world.rand.nextInt(100) : 500 + this.world.rand.nextInt(1500));
        food.setTotalRot(chicken, 0);
        food.setLastTickTime(chicken, new Calendar(IExPWorld.of(this.world).today().getTime()));
        this.dropItem(chicken);
    }

    public static class ChickenStats implements IAnimalStats
    {
        public float eggWeight = 50;
        public int eggTimer = 24000;

        @Override
        public IAnimalStats mix(@Nonnull IAnimalStats other)
        {
            if (other instanceof ChickenStats)
            {
                ChickenStats ret = new ChickenStats();
                ret.eggWeight = Math.max(20, (((ChickenStats) other).eggWeight + this.eggWeight) / 2 + (float)(Math.random() - Math.random()));
                ret.eggTimer = (((ChickenStats) other).eggTimer + this.eggTimer) / 2;
                return ret;
            }
            else
            {
                throw new InvalidParameterException();
            }
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound ret = new NBTTagCompound();
            ret.setFloat("eggWeight", this.eggWeight);
            ret.setInteger("eggTimer", this.eggTimer);
            return ret;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            this.eggWeight = nbt.getFloat("eggWeight");
            this.eggTimer = nbt.getInteger("eggTimer");
        }
    }
}

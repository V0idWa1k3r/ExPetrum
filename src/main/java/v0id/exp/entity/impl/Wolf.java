package v0id.exp.entity.impl;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import v0id.api.exp.util.ColorHEX;
import v0id.api.exp.util.ColorHSV;

import javax.annotation.Nullable;

public class Wolf extends EntityMob
{
    public static final DataParameter<Integer> PARAM_COLOR = EntityDataManager.createKey(Wolf.class, DataSerializers.VARINT);

    public Wolf(World worldIn)
    {
        super(worldIn);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.5F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true)
        {
            protected double getAttackReachSqr(EntityLivingBase attackTarget)
            {
                return (double)(4.0F + attackTarget.width);
            }
        });

        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, Pig.class, true));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, Sheep.class, true));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, Chicken.class, true));
        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, Cow.class, true));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(PARAM_COLOR, -1);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
    }

    protected SoundEvent getAmbientSound()
    {
        return this.rand.nextFloat() <= 0.01F ? SoundEvents.ENTITY_WOLF_HOWL : SoundEvents.ENTITY_WOLF_GROWL;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (this.world.getWorldTime() % 24000 == 12000)
        {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WOLF_HOWL, SoundCategory.HOSTILE, 1.0F, 1F);
        }

        if (this.world.getWorldTime() % 24000 < 12000 && !this.world.isRemote)
        {
            if (this.rand.nextFloat() < 0.001F)
            {
                this.setDead();
            }
        }
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);
        this.dropItem(new ItemStack(Items.BONE, 1 + this.world.rand.nextInt(6), 0));
    }

    public void dropItem(ItemStack is)
    {
        InventoryHelper.spawnItemStack(this.world, this.posX, this.posY, this.posZ, is);
    }

    public int getColorHEX()
    {
        return this.getDataManager().get(PARAM_COLOR);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        ColorHSV color = new ColorHSV(1F / 360F, 1F / 100F, 1F);
        if (this.world.rand.nextFloat() < 0.1F)
        {
            color = new ColorHSV(1F / 360F, 1F / 100F, 20F / 100F);
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("exp:modifier_wolf_alpha_health", 24, 0));
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("exp:modifier_wolf_alpha_damage", 4, 0));
        }
        else
        {
            if (this.world.rand.nextFloat() < 0.75F)
            {
                color = new ColorHSV((28 + this.world.rand.nextInt(5) - this.world.rand.nextInt(5)) / 360F, (80 - this.world.rand.nextInt(40)) / 100F, (35 + this.world.rand.nextInt(20) - this.world.rand.nextInt(20)) / 100F);
            }
        }

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("exp:modifier_wolf_health", this.world.rand.nextInt(8) - this.world.rand.nextInt(8), 0));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("exp:modifier_wolf_damage", this.world.rand.nextInt(2) - this.world.rand.nextInt(2), 0));
        this.setHealth(this.getMaxHealth());
        this.dataManager.set(PARAM_COLOR, ColorHEX.FromHSV(color).getHexcode());
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() && this.world.rand.nextFloat() < 0.33F && this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 32, false) == null;
    }
}

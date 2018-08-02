package v0id.exp.entity;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityThrownSpear extends EntityProjectile
{
	public static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityThrownSpear.class, DataSerializers.ITEM_STACK);
    public Entity hitEntity;

	public EntityThrownSpear(World w)
	{
		super(w);
	}

	public EntityThrownSpear(World worldIn, EntityLivingBase throwerIn, ItemStack is)
	{
		super(worldIn, throwerIn);
		this.setRenderStack(is);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(STACK, ItemStack.EMPTY);
	}
	
	public ItemStack getRenderStack()
	{
		return this.getDataManager().get(STACK);
	}
	
	public void setRenderStack(ItemStack is)
	{
		this.getDataManager().set(STACK, is);
	}

	@Override
	public boolean causeImpactDamage(Entity entityHit)
	{
		return entityHit.attackEntityFrom(this.createDamageSource(), (float) (this.getDamage() * 1.5F));
	}
	
	public double getDamage()
	{
		Multimap<String, AttributeModifier> modifiersMap = this.getRenderStack().getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
		double ret = 1;
		if (modifiersMap.containsKey(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
		{
			for (AttributeModifier mod : modifiersMap.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			{
				ret = mod.getOperation() == 0 ? ret + mod.getAmount() : ret * mod.getAmount();
			}
		}
		
		return (float) ret;
	}
	
	public DamageSource createDamageSource()
	{
		Entity thrower = this.shootingEntity;
		return DamageSource.causeThrownDamage(this, thrower == null ? this : thrower);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setTag("renderStack", this.getRenderStack().serializeNBT());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		ItemStack is = ItemStack.EMPTY.copy();
        this.setRenderStack(is);
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		super.onCollideWithPlayer(entityIn);
		if (this.inGround && !this.isDead && !this.world.isRemote)
		{
			this.setDead();
			ItemStack is = this.getRenderStack();
			this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, is));
		}
	}
}

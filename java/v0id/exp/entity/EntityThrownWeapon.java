package v0id.exp.entity;

import java.lang.reflect.Field;

import com.google.common.collect.Multimap;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class EntityThrownWeapon extends EntityThrowable
{
	public static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityThrownWeapon.class, DataSerializers.OPTIONAL_ITEM_STACK);
	
	public EntityThrownWeapon(World w)
	{
		super(w);
	}
	
	public EntityThrownWeapon(World worldIn, EntityLivingBase throwerIn, ItemStack is)
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
	
	public boolean isInGround()
	{
		return this.inGround;
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
	protected void onImpact(RayTraceResult result)
	{
		if (result.typeOfHit == Type.ENTITY && result.entityHit != null)
		{
			result.entityHit.attackEntityFrom(this.createDamageSource(), this.getDamage());
		}
		else
		{
			if (result.typeOfHit == Type.BLOCK)
			{
				BlockPos blockpos = result.getBlockPos();
	            IBlockState iblockstate = this.world.getBlockState(blockpos);
				this.inGround = true;
				this.reflectImpactTileData(result.getBlockPos());
				if (iblockstate.getMaterial() != Material.AIR)
	            {
	                iblockstate.getBlock().onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
	            }
			}
		}
	}
	
	public static boolean warningDone = false;
	
	public void reflectImpactTileData(BlockPos pos)
	{
		// Accessibility flag removes security manager checks that offers a significant performance boost
		if (!reflectFlds[0].isAccessible())
		{
			for (Field f : reflectFlds)
			{
				f.setAccessible(true);
			}
		}
		
		try
		{
			reflectFlds[0].setInt(this, pos.getX());
			reflectFlds[1].setInt(this, pos.getY());
			reflectFlds[2].setInt(this, pos.getZ());
			reflectFlds[3].set(this, this.world.getBlockState(pos).getBlock());
		}
		catch (Exception ex)
		{
			if (warningDone)
			{
				return;
			}
			
			FMLLog.bigWarning(
					  "ExPetrum was unable to reflect some(first 4) fields(expected I,I,I,Lnet.minecraft.block.Block) at EntityThrowable class! \n"
					+ "Currently this will not crash your game. /n"
					+ "However this is a severe issue most likely caused by one of your coremods! /n"
					+ "DO NOT REPORT THIS TO AUTHOR OF EX PERTUM(v0id)! /n"
					+ "Find which coremod does this and complain to their author! /n"
					+ "Exception was: %s (%s)",
				ex.getClass().getName(), ex.getMessage());
			warningDone = true;
		}
	}
	
	
	
	public float getDamage()
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
		EntityLivingBase thrower = this.getThrower();
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
		is.deserializeNBT(compound.getCompoundTag("renderStack"));
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

	
	private static final Field[] reflectFlds;
	
	static
	{
		reflectFlds = new Field[4];
		reflectFlds[0] = EntityThrowable.class.getDeclaredFields()[0];
		reflectFlds[1] = EntityThrowable.class.getDeclaredFields()[1];
		reflectFlds[2] = EntityThrowable.class.getDeclaredFields()[2];
		reflectFlds[3] = EntityThrowable.class.getDeclaredFields()[3];
		for (Field f : reflectFlds)
		{
			f.setAccessible(true);
		}
	}
}

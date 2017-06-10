package v0id.exp.combat.impl;

import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPRegistryNames;

public class Spin extends SpecialAttack
{
	public static final WeakHashMap<EntityPlayer, Float> initialAngles = new WeakHashMap();
	
	public Spin()
	{
		super(ExPRegistryNames.specialAttackSpin.toString());
		this.setSortName("exp:dSpin");
		this.setExecutionTime(10);
	}

	@Override
	public void onExecutionTick(EntityPlayer player, int progress)
	{
		player.rotationYaw += 40;
		Vec3d playerLook = player.getLookVec();
		Vec3d playerPos = player.getPositionVector();
		player.world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, playerPos.x + playerLook.x, playerPos.y + player.getEyeHeight() * 0.5F, playerPos.z + playerLook.z, 0, 0, 0, new int[0]);
		AxisAlignedBB aabb = new AxisAlignedBB(playerPos.addVector(-3, -1, -3), playerPos.addVector(3, 3, 3));
		List<EntityLivingBase> lst = player.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, e -> e != player);
		for (EntityLivingBase ent : lst)
		{
			if (ent instanceof EntityTameable && ((EntityTameable)ent).getOwner() == player)
			{
				continue;
			}
			
			Vec3d ePos = ent.getPositionVector();
			Vec3d player2ent = ePos.subtract(playerPos).normalize();
			float angle = (float) Math.toDegrees(Math.acos(player2ent.dotProduct(playerLook)));
			if (angle > 40)
			{
				continue;
			}
			
			player.world.playSound(player, ent.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.2F, 1.5F);
			ent.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) (player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 0.75F));
		}
	}

	@Override
	public void onExecutionStart(EntityPlayer player)
	{
		initialAngles.put(player, player.rotationYaw);
		player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1F, 0.1F);
	}

	@Override
	public void onExecutionEnd(EntityPlayer player)
	{
		if (initialAngles.containsKey(player))
		{
			player.rotationYaw = initialAngles.get(player);
			initialAngles.remove(player);
		}
	}

	@Override
	public boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick)
	{
		return player.getActiveItemStack().isEmpty() && !invokedWithRightClick && currentWeaponWeight != EnumWeaponWeight.LIGHT && (currentWeapon.isAssociated(WeaponType.SWORD) || currentWeapon.isAssociated(WeaponType.HAMMER) || currentWeapon.isAssociated(WeaponType.AXE));
	}

}

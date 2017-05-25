package v0id.exp.combat.impl;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.util.Helpers;

public class PiercingDash extends SpecialAttack
{
	public PiercingDash()
	{
		super(ExPRegistryNames.specialAttackPiercingDash.toString());
		this.setExecutionTime(10);
	}

	@Override
	public void onExecutionTick(EntityPlayer player, int progress)
	{
		
	}

	@Override
	public void onExecutionStart(EntityPlayer player)
	{
		Vec3d look = player.getLookVec().scale(6);
		Vec3d pos = player.getPositionVector();
		List<EntityLivingBase> targets = Helpers.rayTraceEntities(player.world, pos.addVector(0, player.getEyeHeight(), 0), look, Optional.of(e -> e != player), EntityLivingBase.class);
		for (EntityLivingBase target : targets)
		{
			Vec3d targetPos = target.getPositionVector();
			target.knockBack(player, 3, pos.xCoord - targetPos.xCoord, pos.zCoord - targetPos.zCoord);
			double distance = Math.max(0.3, targetPos.distanceTo(pos));
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) (player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * (1 - (distance / 6))));
		}
		
		player.motionX += look.xCoord / 5;
		player.motionZ += look.zCoord / 5;
		
		for (int i = 0; i < 200; ++i)
		{
			double randomMagnitude = player.world.rand.nextDouble();
			Vec3d at = new Vec3d(pos.xCoord + look.xCoord * randomMagnitude, pos.yCoord + player.getEyeHeight() - 0.25 + look.yCoord * randomMagnitude, pos.zCoord + look.zCoord * randomMagnitude);
			player.world.spawnParticle(EnumParticleTypes.CRIT, at.xCoord, at.yCoord, at.zCoord, 0, 0, 0, new int[0]);
		}
	}

	@Override
	public void onExecutionEnd(EntityPlayer player)
	{
		
	}

	@Override
	public boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick)
	{
		return player.isSprinting() && !invokedWithRightClick && (currentWeapon.isAssociated(WeaponType.SWORD) || currentWeapon.isAssociated(WeaponType.SPEAR) || currentWeapon.isAssociated(WeaponType.DAGGER));
	}

}

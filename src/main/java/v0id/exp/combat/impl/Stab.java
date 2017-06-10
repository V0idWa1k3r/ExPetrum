package v0id.exp.combat.impl;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.util.Helpers;

public class Stab extends SpecialAttack
{
	public Stab()
	{
		super(ExPRegistryNames.specialAttackStab);
		this.setExecutionTime(10);
	}

	@Override
	public void onExecutionTick(EntityPlayer player, int progress)
	{
		player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1, 2F);
		Vec3d look = player.getLookVec().scale(3);
		Vec3d pos = player.getPositionVector();
		List<EntityLivingBase> targets = Helpers.rayTraceEntities(player.world, pos.addVector(0, player.getEyeHeight(), 0), look, Optional.of(e -> e != player), EntityLivingBase.class);
		EntityLivingBase assumedToBeLookedAt = Helpers.getClosest(targets, player);
		if (assumedToBeLookedAt != null)
		{
			assumedToBeLookedAt.hurtResistantTime = 0;
			assumedToBeLookedAt.attackEntityFrom(DamageSource.causePlayerDamage(player), Math.max(1, (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() / 10));
			player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1, 0.1F);
			Vec3d targetPos = assumedToBeLookedAt.getPositionVector();
			player.world.spawnParticle(EnumParticleTypes.CRIT, targetPos.x + player.world.rand.nextDouble() / 2 - player.world.rand.nextDouble() / 2, targetPos.y + assumedToBeLookedAt.getEyeHeight() + player.world.rand.nextDouble() / 2 - player.world.rand.nextDouble() / 2, targetPos.z + player.world.rand.nextDouble() / 2 - player.world.rand.nextDouble() / 2, 0, 0, 0, new int[0]);
			if (!player.world.isRemote)
			{
				ItemStack is = player.getHeldItemMainhand().isEmpty() ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
				is.damageItem(1, player);
			}
		}
	}

	@Override
	public void onExecutionStart(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onExecutionEnd(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick)
	{
		return !invokedWithRightClick && player.getActiveItemStack().isEmpty() && currentWeapon.isAssociated(WeaponType.DAGGER) && currentWeaponWeight == EnumWeaponWeight.LIGHT;
	}

}

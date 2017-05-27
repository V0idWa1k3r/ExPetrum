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

public class Behead extends SpecialAttack
{
	public Behead()
	{
		super(ExPRegistryNames.specialAttackBehead.toString());
		this.setExecutionTime(10);
		this.setSortName("exp:eBehead");
	}

	@Override
	public void onExecutionTick(EntityPlayer player, int progress)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onExecutionStart(EntityPlayer player)
	{
		ItemStack is = player.getHeldItemMainhand().isEmpty() ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
		player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1, 1F);
		Vec3d look = player.getLookVec().scale(5);
		Vec3d pos = player.getPositionVector();
		List<EntityLivingBase> targets = Helpers.rayTraceEntities(player.world, pos.addVector(0, player.getEyeHeight(), 0), look, Optional.of(e -> e != player), EntityLivingBase.class);
		EntityLivingBase assumedToBeLookedAt = Helpers.getClosest(targets, player);
		if (assumedToBeLookedAt != null)
		{
			if (!player.world.isRemote)
			{
				is.damageItem(1, player);
			}
			
			assumedToBeLookedAt.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
			player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1, 0.1F);
			Vec3d targetPos = assumedToBeLookedAt.getPositionVector();
			player.world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, targetPos.xCoord, targetPos.yCoord + assumedToBeLookedAt.getEyeHeight(), targetPos.zCoord, 0, 0, 0, new int[0]);
			float chance = (1 - assumedToBeLookedAt.getHealth() / assumedToBeLookedAt.getMaxHealth());
			if (!player.world.isRemote && player.world.rand.nextFloat() < chance / 10)
			{
				assumedToBeLookedAt.hurtResistantTime = assumedToBeLookedAt.hurtTime = 0;
				assumedToBeLookedAt.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
			}
		}
	}

	@Override
	public void onExecutionEnd(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick)
	{
		return player.getActiveItemStack().isEmpty() && currentWeapon.isAssociated(WeaponType.AXE) && currentWeaponWeight != EnumWeaponWeight.LIGHT;
	}

}

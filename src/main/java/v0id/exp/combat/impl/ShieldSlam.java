package v0id.exp.combat.impl;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPPotions;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.util.Helpers;

public class ShieldSlam extends SpecialAttack
{

	public ShieldSlam()
	{
		super(ExPRegistryNames.specialAttackShieldSlam.toString());
		this.setExecutionTime(5);
	}

	@Override
	public void onExecutionTick(EntityPlayer player, int progress)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onExecutionStart(EntityPlayer player)
	{
		if (player.getActiveItemStack().isEmpty())
		{
			return;
		}
		
		Vec3d look = player.getLookVec().scale(5);
		Vec3d pos = player.getPositionVector();
		List<EntityLivingBase> targets = Helpers.rayTraceEntities(player.world, pos.addVector(0, player.getEyeHeight(), 0), look, Optional.of(e -> e != player), EntityLivingBase.class);
		EntityLivingBase assumedToBeLookedAt = Helpers.getClosest(targets, player);
		if (assumedToBeLookedAt != null)
		{
			if (!player.world.isRemote)
			{
				assumedToBeLookedAt.addPotionEffect(new PotionEffect(ExPPotions.stunned, 40, 0, false, false));
			}
			
			player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1, 0.1F);
			Vec3d targetPos = assumedToBeLookedAt.getPositionVector();
			assumedToBeLookedAt.knockBack(player, 2, pos.x - targetPos.x, pos.z - targetPos.z);
		}
		
		player.getActiveItemStack().damageItem(3, player);
	}

	@Override
	public void onExecutionEnd(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick)
	{
		return !invokedWithRightClick && player.isActiveItemStackBlocking();
	}

}

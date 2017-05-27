package v0id.exp.combat.impl;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPPotions;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.util.Helpers;

public class DownStrike extends SpecialAttack
{

	public DownStrike()
	{
		super(ExPRegistryNames.specialAttackDownStrike.toString());
		this.setExecutionTime(10);
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
		EnumWeaponWeight weight = EnumWeaponWeight.getWeaponWeight(is);
		player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1, 1F);
		Vec3d look = player.getLookVec().scale(5);
		Vec3d pos = player.getPositionVector();
		List<EntityLivingBase> targets = Helpers.rayTraceEntities(player.world, pos.addVector(0, player.getEyeHeight(), 0), look, Optional.of(e -> e != player), EntityLivingBase.class);
		EntityLivingBase assumedToBeLookedAt = Helpers.getClosest(targets, player);
		if (assumedToBeLookedAt != null)
		{
			if (!player.world.isRemote)
			{
				assumedToBeLookedAt.addPotionEffect(new PotionEffect(ExPPotions.stunned, weight == EnumWeaponWeight.NORMAL ? 20 : 30, 0, false, false));
			}
			
			player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1, 1F);
			Vec3d targetPos = assumedToBeLookedAt.getPositionVector();
			assumedToBeLookedAt.knockBack(player, 1, pos.xCoord - targetPos.xCoord, pos.zCoord - targetPos.zCoord);
			for (int i = 0; i < 50; ++i)
			{
				player.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, targetPos.xCoord + player.world.rand.nextDouble() - player.world.rand.nextDouble(), targetPos.yCoord + assumedToBeLookedAt.getEyeHeight() + player.world.rand.nextDouble() - player.world.rand.nextDouble(), targetPos.zCoord + player.world.rand.nextDouble() - player.world.rand.nextDouble(), 0, -0.1, 0, new int[0]);
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
		return player.getActiveItemStack().isEmpty() && !invokedWithRightClick && (currentWeapon.isAssociated(WeaponType.HAMMER) || currentWeapon.isAssociated(WeaponType.AXE) || currentWeapon.isAssociated(WeaponType.SWORD) && currentWeaponWeight != EnumWeaponWeight.LIGHT);
	}

}

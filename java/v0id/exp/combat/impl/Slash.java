package v0id.exp.combat.impl;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPPotions;
import v0id.api.exp.data.ExPRegistryNames;

public class Slash extends SpecialAttack
{

	public Slash()
	{
		super(ExPRegistryNames.specialAttackSlash.toString());
		this.setExecutionTime(8);
	}

	@Override
	public void onExecutionTick(EntityPlayer player, int progress)
	{

	}

	@Override
	public void onExecutionStart(EntityPlayer player)
	{
		ItemStack is = player.getHeldItemMainhand().isEmpty() ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
		EnumWeaponWeight weight = EnumWeaponWeight.getWeaponWeight(is);
		player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1, 1F);
		Vec3d playerLook = player.getLookVec();
		Vec3d playerPos = player.getPositionVector();
		player.world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, playerPos.xCoord + playerLook.xCoord, playerPos.yCoord + player.getEyeHeight() * 0.5F, playerPos.zCoord + playerLook.zCoord, 0, 0, 0, new int[0]);
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
			if (angle > 60)
			{
				continue;
			}
			
			ent.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) (player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 0.85F));
			player.world.playSound(player, ent.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1, 2F);
			if (!player.world.isRemote && weight == EnumWeaponWeight.HEAVY && player.world.rand.nextBoolean())
			{
				ent.addPotionEffect(new PotionEffect(ExPPotions.stunned, 100, 0, false, false));
			}
		}
	}

	@Override
	public void onExecutionEnd(EntityPlayer player)
	{
		
	}

	@Override
	public boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick)
	{
		return player.getActiveItemStack().isEmpty() && !invokedWithRightClick && !currentWeapon.isAssociated(WeaponType.DAGGER) && currentWeapon != WeaponType.SPEAR && currentWeapon != WeaponType.NONE;
	}
}

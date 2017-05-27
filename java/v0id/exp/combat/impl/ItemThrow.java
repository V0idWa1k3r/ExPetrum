package v0id.exp.combat.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.EntityThrownWeapon;

public class ItemThrow extends SpecialAttack
{
	public ItemThrow()
	{
		super(ExPRegistryNames.specialAttackThrow);
		this.setExecutionTime(1);
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
		EntityThrownWeapon etw = new EntityThrownWeapon(player.getEntityWorld(), player, is.copy());
		etw.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
		if (!player.world.isRemote)
		{
			player.world.spawnEntity(etw);
			is.setCount(0);
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
		return !invokedWithRightClick && player.getActiveItemStack().isEmpty() && currentWeapon.isAssociated(WeaponType.DAGGER) && currentWeaponWeight == EnumWeaponWeight.LIGHT;
	}

}

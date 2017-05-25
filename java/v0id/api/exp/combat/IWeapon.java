package v0id.api.exp.combat;

import net.minecraft.item.ItemStack;

public interface IWeapon
{
	WeaponType getType(ItemStack is);
	
	EnumWeaponWeight getWeaponWeight(ItemStack is);
}

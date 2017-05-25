package v0id.api.exp.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public enum EnumWeaponWeight
{
	LIGHT,
	NORMAL,
	HEAVY;
	
	public static EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		if (is.isEmpty())
		{
			return LIGHT;
		}
		
		Item item = is.getItem();
		if (item instanceof IWeapon)
		{
			return ((IWeapon)item).getWeaponWeight(is);
		}
		
		if (item instanceof ItemPickaxe || item instanceof ItemSpade)
		{
			return HEAVY;
		}
		
		if (item instanceof ItemAxe || item instanceof ItemSword)
		{
			return NORMAL;
		}
		
		return LIGHT;
	}
}

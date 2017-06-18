package v0id.api.exp.combat;

import com.google.common.collect.Lists;
import net.minecraft.item.*;

import java.util.Arrays;
import java.util.List;

public class WeaponType
{
	public final List<WeaponType> associatedTypes = Lists.newArrayList();
	
	public WeaponType()
	{
		
	}
	
	public WeaponType(WeaponType...types)
	{
		this.associatedTypes.addAll(Arrays.asList(types));
	}
	
	public boolean isAssociated(WeaponType type)
	{
		return (type == this || this.associatedTypes.contains(type)) || this.associatedTypes.stream().anyMatch(t -> t.isAssociated(type));
	}
	
	// Basic weapon types
	// A sword. The basic weapon
	public static final WeaponType SWORD = new WeaponType();
	
	// A dagger. Quick weak weapon sutable for stealth.
	public static final WeaponType DAGGER = new WeaponType();
	
	// A spear. Very precise but can pierce armor.
	public static final WeaponType SPEAR = new WeaponType();
	
	// Axe. A bit heavier than the sword and harder to use but can be deadlier.
	public static final WeaponType AXE = new WeaponType();
	
	// A hammer. Very heavy yet powerful crushing weapon
	public static final WeaponType HAMMER = new WeaponType();
	
	// Sub-weapons.
	// A dual-edged axe. 
	public static final WeaponType BATTLEAXE = new WeaponType(AXE, SWORD);
	
	// Halberd. A dual axe with a pointy end.
	public static final WeaponType HALBERD = new WeaponType(BATTLEAXE, SPEAR);
	
	// A hardened hammer. Extremely heavy
	public static final WeaponType BATTLEHAMMER = new WeaponType(HAMMER);
	
	// A Scythe
	public static final WeaponType SCYTHE = new WeaponType(SWORD);
	
	// Unknown weapon type.
	public static final WeaponType NONE = new WeaponType();
	
	/**
	 * Gets the weapon type of the given itemstack
	 * @param is : the stack to get the type of
	 * @return - a weapon type corresponding to the stack's item or NONE
	 */
	public static WeaponType getType(ItemStack is)
	{
		Item item = is.getItem();
		if (item instanceof IWeapon)
		{
			return ((IWeapon)item).getType(is);
		}
		
		if (item instanceof ItemSword)
		{
			return SWORD;
		}
		
		if (item instanceof ItemAxe)
		{
			return AXE;
		}
		
		if (item instanceof ItemSpade)
		{
			return HAMMER;
		}
		
		if (item instanceof ItemShears)
		{
			return DAGGER;
		}
		
		return NONE;
	}
}

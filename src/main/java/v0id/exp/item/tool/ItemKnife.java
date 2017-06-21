package v0id.exp.item.tool;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.IWeapon;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.exp.item.IInitializableItem;
import v0id.exp.player.inventory.PlayerInventoryHelper;

public class ItemKnife extends ItemExPWeapon implements IWeapon, IWeightProvider, IInitializableItem, IOreDictEntry
{
	public ItemKnife()
	{
		super();
		this.initItem();
	}

	@Override
	public void registerOreDictNames()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemKnife));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return PlayerInventoryHelper.defaultVolume;
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.DAGGER;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.LIGHT;
	}

	@Override
	public EnumToolClass getToolClass()
	{
		return EnumToolClass.KNIFE;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 0.8F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -1.5F;
	}

}

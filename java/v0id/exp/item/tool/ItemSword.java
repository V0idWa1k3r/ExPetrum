package v0id.exp.item.tool;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.IWeapon;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.item.IInitializableItem;

public class ItemSword extends ItemExPWeapon implements IWeapon, IWeightProvider, IInitializableItem, IItemRegistryEntry, IOreDictEntry
{
	public ItemSword()
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
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(this);
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.itemSword);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
		ExPHandlerRegistry.put(this);
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
		return Pair.of((byte)1, (byte)3);
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.SWORD;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.NORMAL;
	}

	@Override
	public EnumToolClass getToolClass()
	{
		return EnumToolClass.SWORD;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 1.1F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -2.4F;
	}

}

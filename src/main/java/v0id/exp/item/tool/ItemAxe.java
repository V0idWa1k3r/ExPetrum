package v0id.exp.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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

public class ItemAxe extends ItemExPTool implements IWeapon, IWeightProvider, IInitializableItem, IOreDictEntry
{
	public ItemAxe()
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
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemAxe));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getStats(item).getWeight() * this.getToolClass().getWeight();
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)2, (byte)3);
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.AXE;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.HEAVY;
	}

	@Override
	public EnumToolClass getToolClass()
	{
		return EnumToolClass.AXE;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 1.0F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -3.2F;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.getStats(stack).getEfficiency();
    }

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
        Material material = state.getMaterial();
        return material == Material.WOOD || material == Material.PLANTS || material == Material.VINE || super.canHarvestBlock(state, stack);
    }
}

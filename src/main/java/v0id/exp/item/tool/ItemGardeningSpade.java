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

public class ItemGardeningSpade extends ItemExPTool implements IWeapon, IWeightProvider, IInitializableItem, IOreDictEntry
{
	public ItemGardeningSpade()
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
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemGardeningSpade));
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
		return Pair.of((byte)1, (byte)2);
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.NONE;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.LIGHT;
	}

	@Override
	public EnumToolClass getToolClass()
	{
		return EnumToolClass.SHOVEL;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 0.4F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -1F;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        float ret = material != Material.GRASS && material != Material.GROUND && material != Material.CRAFTED_SNOW && material != Material.SNOW && material != Material.SAND && material != Material.CLAY && material != Material.GOURD ? super.getDestroySpeed(stack, state) : this.getStats(stack).getEfficiency();
    	return ret * 0.75F;
    }

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		Material material = state.getMaterial();
		return material == Material.GRASS || material == Material.GROUND || material == Material.CRAFTED_SNOW || material == Material.SNOW || material == Material.SAND || material == Material.CLAY || material == Material.GOURD || super.canHarvestBlock(state, stack);
	}
}

package v0id.exp.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.IWeapon;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;

import java.util.Arrays;

public class ItemBattleaxe extends ItemExPWeapon implements IWeapon, IWeightProvider, IOreDictEntry
{
	public ItemBattleaxe(EnumToolStats stats)
	{
		super(stats, EnumToolClass.BATTLEAXE);
		this.setSelfRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemBattleaxe));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(EnumToolStats.values()).forEach(mat -> Arrays.stream(ExPOreDict.itemBattleaxe).forEach(name ->
        {
            OreDictionary.registerOre(name, new ItemStack(this, 1, mat.ordinal()));
            OreDictionary.registerOre(name + Character.toUpperCase(mat.name().charAt(0)) + mat.name().toLowerCase().substring(1), new ItemStack(this, 1, mat.ordinal()));
        }));
    }

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getStats(item).getWeight() * this.getToolClass().getWeight();
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)3, (byte)3);
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.BATTLEAXE;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.HEAVY;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 1.3F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -2.8F;
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

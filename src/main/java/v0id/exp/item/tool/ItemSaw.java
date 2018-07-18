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

public class ItemSaw extends ItemExPTool implements IWeapon, IWeightProvider, IOreDictEntry
{
	public ItemSaw(EnumToolStats stats)
	{
		super(stats, EnumToolClass.SAW);
		this.setSelfRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemSaw));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(EnumToolStats.values()).forEach(mat -> Arrays.stream(ExPOreDict.itemSaw).forEach(name -> OreDictionary.registerOre(name, new ItemStack(this, 1, mat.ordinal()))));
    }

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getStats(item).getWeight() * this.getToolClass().getWeight();
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)3, (byte)2);
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.NONE;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.NORMAL;
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
        return material != Material.WOOD ? super.getDestroySpeed(stack, state) : this.getStats(stack).getEfficiency();
    }

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		Material material = state.getMaterial();
		return material == Material.WOOD || super.canHarvestBlock(state, stack);
	}

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack ret = itemStack.copy();
        ret.setItemDamage(ret.getItemDamage() + 1);
        if (ret.getItemDamage() >= ret.getMaxDamage())
        {
            return ItemStack.EMPTY;
        }

        return ret;
    }
}

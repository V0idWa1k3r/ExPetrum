package v0id.exp.item.tool;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.NonNullList;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.ItemIngot;

public abstract class ItemExPTool extends ItemTool implements IExPTool
{
	public static final Set<Block> effectiveOn = Sets.newHashSet(new Block[]{}); 
	
	public ItemExPTool()
	{
		super(1, 1, ExPMisc.materialExPetrum, effectiveOn);
		this.setFull3D();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		EnumMetal metal = this.getStats(toRepair).getMaterial();
		EnumMetal ingotMetal = repair.getItem() instanceof ItemIngot ? EnumMetal.values()[Math.min(repair.getMetadata(), EnumMetal.values().length - 1)] : null;
		return metal != null && metal.equals(ingotMetal);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState)
	{
		return this.getToolClass().getName().equals(toolClass) ? this.getStats(stack).getHarvestLevel() : super.getHarvestLevel(stack, toolClass, player, blockState);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return Sets.newHashSet(new String[]{ this.getToolClass().getName() });
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + '.' + (this.getStats(stack).getMaterial() != null ? this.getStats(stack).getMaterial().name().toLowerCase() : "stone");
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < EnumToolStats.values().length; ++i)
		{
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		return this.getToolCompound(stack).getInteger("damageCurrent");
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return this.getToolCompound(stack).getInteger("damageMax");
	}
	
	@Override
	public boolean isDamaged(ItemStack stack)
    {
		return this.getDamage(stack) > 0;
    }
	
	@Override
	public void setDamage(ItemStack stack, int damage)
    {
		this.getToolCompound(stack).setInteger("damageCurrent", Math.max(0, damage));
    }
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> ret = HashMultimap.create();
		if (slot == EntityEquipmentSlot.MAINHAND)
        {
			ret.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage(stack), 0));
			ret.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.getAttackSpeed(stack), 0));
        }
		
		return ret;
    }
}

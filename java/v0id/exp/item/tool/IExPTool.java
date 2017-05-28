package v0id.exp.item.tool;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;

public interface IExPTool
{
	float getAttackDamage(ItemStack is);
	
	float getAttackSpeed(ItemStack is);
	
	EnumToolClass getToolClass();
	
	default EnumToolStats getStats(ItemStack is)
	{
		return EnumToolStats.values()[Math.min(is.getMetadata(), EnumToolStats.values().length - 1)];
	}
	
	default NBTTagCompound getToolCompound(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("exp.toolStats") ? stack.getTagCompound().getCompoundTag("exp.toolStats") : this.initToolCompound(stack);
	}
	
	default NBTTagCompound initToolCompound(ItemStack stack)
	{
		NBTTagCompound ret = new NBTTagCompound();
		ret.setInteger("damageMax", this.getStats(stack).getDurability());
		ret.setInteger("damageCurrent", 0);
		ret.setFloat("skill", 1.0F);
		ret.setFloat("purity", 1.0F);
		ret.setFloat("efficiency", this.getStats(stack).getEfficiency());
		ret.setFloat("attackDamageBase", this.getStats(stack).getDamage());
		ret.setFloat("attackDamageMultiplier", this.getStats(stack).getWeaponDamageMultiplier());
		stack.setTagInfo("exp.toolStats", ret);
		return ret;
	}
}

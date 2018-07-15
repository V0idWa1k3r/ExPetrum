package v0id.exp.item.tool;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;

import java.util.Map;

public interface IExPTool
{
	Map<Pair<EnumToolClass, EnumToolStats>, Item> allTools = Maps.newHashMap();

	float getAttackDamage(ItemStack is);
	
	float getAttackSpeed(ItemStack is);
	
	EnumToolClass getToolClass();
	
	EnumToolStats getStats(ItemStack is);
	
	default NBTTagCompound getToolCompound(ItemStack stack)
	{
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("exp.toolStats") ? stack.getTagCompound().getCompoundTag("exp.toolStats") : this.initToolCompound(stack);
	}
	
	default NBTTagCompound initToolCompound(ItemStack stack)
	{
		NBTTagCompound ret = new NBTTagCompound();
		ret.setInteger("damageMax", this.getStats(stack).getDurability());
		ret.setFloat("skill", 1.0F);
		ret.setFloat("purity", 1.0F);
		ret.setFloat("efficiency", this.getStats(stack).getEfficiency());
		ret.setFloat("attackDamageBase", this.getStats(stack).getDamage());
		ret.setFloat("attackDamageMultiplier", this.getStats(stack).getWeaponDamageMultiplier());
		stack.setTagInfo("exp.toolStats", ret);
		return ret;
	}
}

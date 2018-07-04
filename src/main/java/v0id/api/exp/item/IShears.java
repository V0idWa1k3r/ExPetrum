package v0id.api.exp.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IShears
{
    int getWoolAmount(EntityLivingBase sheep, ItemStack shears);
}

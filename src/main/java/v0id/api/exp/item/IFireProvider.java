package v0id.api.exp.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IFireProvider
{
    default void damageItem(ItemStack is, EntityPlayer player, int value)
    {
        is.damageItem(value, player);
    }
}

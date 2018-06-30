package v0id.api.exp.item;

import net.minecraft.item.ItemStack;
import v0id.api.exp.metal.EnumMetal;

public interface IMeltableMetal
{
    EnumMetal getMetal(ItemStack is);

    float getMeltingTemperature(ItemStack is);

    int getMetalAmound(ItemStack is);
}

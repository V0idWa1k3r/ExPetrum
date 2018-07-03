package v0id.api.exp.item;

import net.minecraft.item.ItemStack;
import v0id.api.exp.metal.EnumMetal;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IMold
{
    boolean tryFill(ItemStack self, BiConsumer<EnumMetal, Float> reductor, Consumer<ItemStack> resultSetter, EnumMetal metal, float value);

    boolean isMold(ItemStack self);

    boolean hasMetal(ItemStack self);

    boolean isLiquid(ItemStack self);

    ItemStack getResult(ItemStack self);
}

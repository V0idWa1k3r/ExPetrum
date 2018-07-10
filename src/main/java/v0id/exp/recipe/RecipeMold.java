package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import v0id.api.exp.item.IMold;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nonnull;

public class RecipeMold extends ShapelessOreRecipe implements IRecipe
{
    public RecipeMold(@Nonnull ItemStack result, Object... input)
    {
        super(new ResourceLocation("minecraft:misc"), result, input);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack mold = ItemStack.EMPTY;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty() && is.getItem() instanceof IMold)
            {
                if (!mold.isEmpty())
                {
                    return false;
                }
                else
                {
                    if (is.getItem() instanceof IMold)
                    {
                        mold = is;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }

        return super.matches(inv, worldIn) && !((IMold)mold.getItem()).isLiquid(mold);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1)
    {
        ItemStack result = super.getCraftingResult(var1);
        ItemStack mold = ItemStack.EMPTY;
        for (int i = 0; i < var1.getWidth() * var1.getHeight(); ++i)
        {
            ItemStack is = var1.getStackInSlot(i);
            if (!is.isEmpty() && is.getItem() instanceof IMold)
            {
                mold = is;
            }
        }

        TemperatureUtils.setTemperature(result, TemperatureUtils.getTemperature(mold));
        return result;
    }
}

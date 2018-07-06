package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import v0id.api.exp.item.IMold;

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
}

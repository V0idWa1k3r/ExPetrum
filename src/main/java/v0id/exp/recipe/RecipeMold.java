package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import v0id.api.exp.item.IMold;

public class RecipeMold extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        boolean hasMold = false;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (hasMold)
                {
                    return false;
                }
                else
                {
                    if (is.getItem() instanceof IMold)
                    {
                        hasMold = true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }

        return hasMold;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack mold = ItemStack.EMPTY;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (!mold.isEmpty())
                {
                    return ItemStack.EMPTY;
                }
                else
                {
                    if (is.getItem() instanceof IMold)
                    {
                        mold = is;
                    }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        return ((IMold)mold.getItem()).isLiquid(mold) ? ItemStack.EMPTY : ((IMold)mold.getItem()).getResult(mold);
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 1;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }
}

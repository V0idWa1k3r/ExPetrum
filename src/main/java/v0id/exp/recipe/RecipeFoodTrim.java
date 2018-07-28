package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import v0id.api.exp.item.IKnife;
import v0id.exp.item.ItemFood;

public class RecipeFoodTrim extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack food = ItemStack.EMPTY;
        ItemStack knife = ItemStack.EMPTY;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof ItemFood)
                {
                    if (food.isEmpty())
                    {
                        food = is;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    if (is.getItem() instanceof IKnife)
                    {
                        if (knife.isEmpty())
                        {
                            knife = is;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }

        return !food.isEmpty() && !knife.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack food = ItemStack.EMPTY;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof ItemFood)
                {
                    if (food.isEmpty())
                    {
                        food = is.copy();
                    }
                }
            }
        }

        if (food.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        ItemFood ifood = (ItemFood) food.getItem();
        float decay = ifood.getTotalRot(food);
        ifood.setTotalWeight(food, Math.min(10000, ifood.getTotalWeight(food) * ((100 - decay) / 100F)));
        ifood.setTotalRot(food, 0.1F);
        return food;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
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

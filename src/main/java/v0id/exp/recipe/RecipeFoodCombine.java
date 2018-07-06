package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import v0id.exp.item.ItemFood;

public class RecipeFoodCombine extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
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
                        food = is;
                    }
                    else
                    {
                        if (food.getItem() != is.getItem() || food.getMetadata() != is.getMetadata())
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return !food.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack food = ItemStack.EMPTY;
        float amount = 0;
        float rot = 0;
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

                    amount += ((ItemFood)is.getItem()).getTotalWeight(is);
                    rot = Math.max(((ItemFood)is.getItem()).getTotalRot(is), rot);
                }
            }
        }

        if (food.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        ItemFood ifood = (ItemFood) food.getItem();
        ifood.setTotalWeight(food, Math.min(10000, amount));
        ifood.setTotalRot(food, Math.min(((ItemFood)food.getItem()).getEntry(food).getBaseHealth(), rot));
        return food;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> ret = NonNullList.create();
        float amount = 0;
        for (int i = 0; i < inv.getWidth() * inv.getHeight(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof ItemFood)
                {
                    float added = ((ItemFood)is.getItem()).getTotalWeight(is);
                    if (added + amount > 10000)
                    {
                        float val = (amount + added) - 10000;
                        amount = 10000;
                        ((ItemFood)is.getItem()).setTotalWeight(is, val);
                        ret.add(i, is.copy());
                    }
                    else
                    {
                        amount += added;
                        ret.add(i, ItemStack.EMPTY);
                    }
                }
            }
            else
            {
                ret.add(i, ItemStack.EMPTY);
            }
        }

        return ret;
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

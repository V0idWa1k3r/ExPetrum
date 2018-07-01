package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.block.item.ItemBlockLog;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.block.tree.BlockLog;
import v0id.exp.item.tool.ItemSaw;

public class RecipePlanks extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        boolean hasSaw = false;
        boolean hasLog = false;
        for (int i = 0; i < inv.getHeight() * inv.getWidth(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof ItemSaw)
                {
                    if (hasSaw)
                    {
                        return false;
                    }

                    hasSaw = true;
                }
                else
                {
                    if (is.getItem() instanceof ItemBlockLog)
                    {
                        if (hasLog)
                        {
                            return false;
                        }

                        hasLog = true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }

        return hasSaw && hasLog;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack log = ItemStack.EMPTY;
        for (int i = 0; i < inv.getHeight() * inv.getWidth(); ++i)
        {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
            {
                if (is.getItem() instanceof ItemBlockLog)
                {
                    log = is;
                }
            }
        }

        return log.isEmpty() ? log : this.getPlankFromWood(log);
    }

    public ItemStack getPlankFromWood(ItemStack wood)
    {
        EnumTreeType tt = EnumTreeType.values()[(wood.getMetadata() - 1) / 3 + ((BlockLog)((ItemBlockWithMetadata)wood.getItem()).getBlock()).logIndex * 5];
        return new ItemStack(ExPBlocks.planks[tt.ordinal() / 15], 2, tt.ordinal() % 15);
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

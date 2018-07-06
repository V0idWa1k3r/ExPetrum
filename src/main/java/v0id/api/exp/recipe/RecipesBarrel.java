package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class RecipesBarrel
{
    public static final List<IRecipeBarrel> allRecipes = Lists.newArrayList();

    public static void addRecipe(IRecipeBarrel recipeBarrel)
    {
        allRecipes.add(recipeBarrel);
    }

    public static void addRecipe(ItemStack in, FluidStack fs, ItemStack out, int time)
    {
        allRecipes.add(new RecipeBarrel(in, fs, out, time));
    }

    public static IRecipeBarrel findRecipe(ItemStack is, FluidStack fs)
    {
        for (IRecipeBarrel rec : allRecipes)
        {
            if (rec.matches(fs, is))
            {
                return rec;
            }
        }

        return null;
    }

    public static class RecipeBarrelFluid implements IRecipeBarrel
    {
        public final ItemStack itemIn;
        public final Fluid fluidIn;
        public final FluidStack fluidOut;
        public final int timeReq;
        public final boolean constructFluid;

        public RecipeBarrelFluid(ItemStack itemIn, Fluid fluidIn, FluidStack fluidOut, int timeReq, boolean b)
        {
            this.itemIn = itemIn;
            this.fluidIn = fluidIn;
            this.fluidOut = fluidOut;
            this.timeReq = timeReq;
            this.constructFluid = b;
        }

        @Override
        public boolean matches(FluidStack fs, ItemStack is)
        {
            return fs != null && fs.getFluid() == this.fluidIn && is.isItemEqual(this.itemIn) && is.getCount() >= this.itemIn.getCount();
        }

        @Override
        public int getProgressRequired(ItemStack is)
        {
            return this.timeReq;
        }

        @Override
        public ItemStack getResult(ItemStack is)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack getInput()
        {
            return this.itemIn;
        }

        @Override
        public FluidStack getInputFluid()
        {
            return new FluidStack(this.fluidIn, 10000);
        }

        @Override
        public FluidStack getOutputFluid(ItemStack is)
        {
            FluidStack copy = this.fluidOut.copy();
            copy.amount = 10000;
            return this.constructFluid ? copy : this.fluidOut.copy();
        }

        @Override
        public void consumeFluid(IFluidHandler handler, ItemStack is)
        {
            FluidStack fs = handler.drain(Integer.MAX_VALUE, true);
            handler.fill(this.constructFluid ? new FluidStack(this.fluidOut.getFluid(), fs.amount) : this.fluidOut.copy(), true);
        }

        @Override
        public String getRecipeName(ItemStack is)
        {
            return this.fluidOut.getUnlocalizedName();
        }

        @Override
        public void consumeItem(ItemStack is)
        {
            is.shrink(this.itemIn.getCount());
        }
    }

    public static class RecipeBarrel implements IRecipeBarrel
    {
        public final ItemStack itemIn;
        public final FluidStack fluidIn;
        public final ItemStack itemOut;
        public final int timeReq;

        public RecipeBarrel(ItemStack itemIn, FluidStack fluidIn, ItemStack itemOut, int timeReq)
        {
            this.itemIn = itemIn;
            this.fluidIn = fluidIn;
            this.itemOut = itemOut;
            this.timeReq = timeReq;
        }

        @Override
        public boolean matches(FluidStack fs, ItemStack is)
        {
            return is.isItemEqual(itemIn) && is.getCount() >= this.itemIn.getCount() && fs.getFluid() == this.fluidIn.getFluid() && fs.amount >= this.fluidIn.amount;
        }

        @Override
        public int getProgressRequired(ItemStack is)
        {
            return this.timeReq;
        }

        @Override
        public ItemStack getResult(ItemStack is)
        {
            return this.itemOut.copy();
        }

        @Override
        public ItemStack getInput()
        {
            return this.itemIn;
        }

        @Override
        public FluidStack getInputFluid()
        {
            FluidStack copy = this.fluidIn.copy();
            copy.amount = 10000;
            return copy;
        }

        @Override
        public FluidStack getOutputFluid(ItemStack is)
        {
            FluidStack copy = this.fluidIn.copy();
            copy.amount = 10000 - this.fluidIn.amount;
            return copy;
        }

        @Override
        public void consumeFluid(IFluidHandler handler, ItemStack is)
        {
            handler.drain(this.fluidIn.amount, true);
        }

        @Override
        public String getRecipeName(ItemStack is)
        {
            return this.itemOut.getUnlocalizedName() + ".name";
        }

        @Override
        public void consumeItem(ItemStack is)
        {
            is.shrink(this.itemIn.getCount());
        }
    }

    public interface IRecipeBarrel
    {
        boolean matches(FluidStack fs, ItemStack is);

        int getProgressRequired(ItemStack is);

        ItemStack getResult(ItemStack is);

        ItemStack getInput();

        FluidStack getInputFluid();

        FluidStack getOutputFluid(ItemStack is);

        void consumeFluid(IFluidHandler handler, ItemStack is);

        String getRecipeName(ItemStack is);

        void consumeItem(ItemStack is);
    }
}

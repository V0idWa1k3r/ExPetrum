package v0id.exp.item;

import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemFluidBottle extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
    public static final List<Fluid> allowedFluids = Lists.newArrayList();

    public ItemFluidBottle()
    {
        super();
        this.initItem();
        allowedFluids.clear();
        allowedFluids.add(FluidRegistry.WATER);
        allowedFluids.add(ExPFluids.saltWater);
        allowedFluids.add(ExPFluids.milk);
        allowedFluids.add(ExPFluids.juice);
        allowedFluids.add(ExPFluids.oliveOil);
        allowedFluids.add(ExPFluids.walnutOil);
        allowedFluids.add(ExPFluids.brine);
        allowedFluids.add(ExPFluids.tannin);
        allowedFluids.add(ExPFluids.oil);
    }

    public static ItemStack createFluidBottle(Fluid f)
    {
        if (allowedFluids.contains(f))
        {
            return new ItemStack(ExPItems.fluidBottle, 1, allowedFluids.indexOf(f));
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void registerOreDictNames()
    {
        for (int i = 0; i < allowedFluids.size(); ++i)
        {
            Fluid f = allowedFluids.get(i);
            OreDictionary.registerOre("bottle" + Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1), new ItemStack(this, 1, i));
        }
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.4F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    @Override
    public void initItem()
    {
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemFluidBottle));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setHasSubtypes(true);
        this.setCreativeTab(ExPCreativeTabs.tabMiscItems);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (Fluid f : allowedFluids)
        {
            items.add(createFluidBottle(f));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + '.' + allowedFluids.get(stack.getMetadata()).getName();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ICapabilityProvider()
        {
            private FixedFluidTank tank = new FixedFluidTank(new FluidStack(allowedFluids.get(stack.getMetadata()), 250));

            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
            {
                return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
            }

            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
            {
                return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this.tank);
            }
        };
    }

    private static class FixedFluidTank implements IFluidHandlerItem
    {
        private final FluidStack fluidStack;

        private FixedFluidTank(FluidStack fluidStack)
        {
            this.fluidStack = fluidStack;
        }

        @Nonnull
        @Override
        public ItemStack getContainer()
        {
            return new ItemStack(Items.GLASS_BOTTLE, 1, 0);
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return new IFluidTankProperties[]{
                    new IFluidTankProperties()
                    {
                        @Nullable
                        @Override
                        public FluidStack getContents()
                        {
                            return FixedFluidTank.this.fluidStack;
                        }

                        @Override
                        public int getCapacity()
                        {
                            return FixedFluidTank.this.fluidStack.amount;
                        }

                        @Override
                        public boolean canFill()
                        {
                            return false;
                        }

                        @Override
                        public boolean canDrain()
                        {
                            return true;
                        }

                        @Override
                        public boolean canFillFluidType(FluidStack fluidStack)
                        {
                            return false;
                        }

                        @Override
                        public boolean canDrainFluidType(FluidStack fluidStack)
                        {
                            return fluidStack.isFluidEqual(FixedFluidTank.this.fluidStack);
                        }
                    }
            };
        }

        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            return this.fluidStack;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            return this.fluidStack;
        }
    }
}

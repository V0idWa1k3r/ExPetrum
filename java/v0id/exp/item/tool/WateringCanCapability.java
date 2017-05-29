package v0id.exp.item.tool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class WateringCanCapability implements IFluidHandlerItem, ICapabilityProvider, IFluidTankProperties
{
	public ItemStack container;
	public static final Fluid WATER = FluidRegistry.WATER;
	
	public WateringCanCapability(ItemStack is)
	{
		this.container = is;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return new IFluidTankProperties[]{ this };
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (resource == null || !resource.getFluid().equals(WATER) || resource.amount <= 0)
		{
			return 0;
		}
		
		FluidStack contents = this.getContents();
		int max = this.getCapacity();
		int current = contents != null ? contents.amount : 0;
		int lacking = max - current;
		int filled = Math.min(lacking, resource.amount);
		if (doFill)
		{
			if (contents != null)
			{
				contents.amount += filled;
			}
			else
			{
				contents = resource.copy();
				contents.amount = filled;
			}
			
			this.setFluidStack(contents);
		}
		
		return filled;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		FluidStack contents = this.getContents();
		if (contents == null || !resource.isFluidEqual(contents))
		{
			return null;
		}
		
		int requested = resource.amount;
		int current = this.getContents().amount;
		int drained = Math.min(requested, current);
		FluidStack ret = this.getContents().copy();
		ret.amount = drained;
		if (doDrain)
		{
			if (drained == current)
			{
				this.setFluidStack(null);
			}
			else
			{
				contents.amount -= drained;
				this.setFluidStack(contents);
			}
		}
		
		return ret;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		FluidStack contents = this.getContents();
		if (contents == null)
		{
			return null;
		}
		
		int requested = maxDrain;
		int current = this.getContents().amount;
		int drained = Math.min(requested, current);
		FluidStack ret = this.getContents().copy();
		ret.amount = drained;
		if (doDrain)
		{
			if (drained == current)
			{
				this.setFluidStack(null);
			}
			else
			{
				contents.amount -= drained;
				this.setFluidStack(contents);
			}
		}
		
		return ret;
	}

	@Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        
        return null;
    }

	@Override
	public ItemStack getContainer()
	{
		return this.container;
	}

	@Override
	public FluidStack getContents()
	{
		return this.getContainer().hasTagCompound() && this.getContainer().getTagCompound().hasKey("exp.wateringCanFluidStackTag") ? FluidStack.loadFluidStackFromNBT(this.getContainer().getTagCompound().getCompoundTag("exp.wateringCanFluidStackTag")) : null;
	}
	
	public void setFluidStack(FluidStack fs)
	{
		if (fs != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag = fs.writeToNBT(tag);
			this.getContainer().getTagCompound().setTag("exp.wateringCanFluidStackTag", tag);
		}
		else
		{
			this.getContainer().getTagCompound().removeTag("exp.wateringCanFluidStackTag");
		}
	}

	@Override
	public int getCapacity()
	{
		switch (((ItemExPTool)this.getContainer().getItem()).getStats(this.getContainer()).getTier())
		{
			case 0:
			{
				return 100;
			}
			
			case 1:
			{
				return 200;
			}
			
			case 2:
			{
				return 500;
			}
			
			case 4:
			{
				return 2000;
			}
			
			case 5:
			{
				return 5000;
			}
			
			case 3:
			default:
			{
				return 1000;
			}
		}
	}

	@Override
	public boolean canFill()
	{
		return true;
	}

	@Override
	public boolean canDrain()
	{
		return true;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack)
	{
		return fluidStack != null && fluidStack.getFluid().equals(WATER);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack)
	{
		return true;
	}

}

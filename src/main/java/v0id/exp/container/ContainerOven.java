package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import v0id.exp.tile.TileOven;

import javax.annotation.Nonnull;

public class ContainerOven extends Container
{
    public TileOven tile;
    public float temperature;
    public int burnTime;
    public int burnTimeMax;

    public ContainerOven(InventoryPlayer playerInventory, TileOven oven)
    {
        this.tile = oven;
        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 0, 62, 8));
        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 1, 62, 66)
        {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack)
            {
                return false;
            }
        });

        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 2, 89, 17));
        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 3, 107, 17));
        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 4, 125, 17));
        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 5, 143, 17));
        this.addSlotToContainer(new SlotItemHandler(oven.inventory, 6, 116, 52));
        this.addPlayerInventory(playerInventory);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            if (this.temperature != this.tile.temperature_handler.getCurrentTemperature())
            {
                icontainerlistener.sendWindowProperty(this, 0, Float.floatToIntBits(this.tile.temperature_handler.getCurrentTemperature()));
            }

            if (this.burnTime != this.tile.burnTimeLeft)
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tile.burnTimeLeft);
            }

            if (this.burnTimeMax != this.tile.maxBurnTime)
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tile.maxBurnTime);
            }
        }

        this.temperature = this.tile.temperature_handler.getCurrentTemperature();
        this.burnTime = this.tile.burnTimeLeft;
        this.burnTimeMax = this.tile.maxBurnTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id == 0)
        {
            this.tile.temperature_handler.setTemperatureBypassMax(Float.intBitsToFloat(data));
        }

        if (id == 1)
        {
            this.tile.burnTimeLeft = data;
        }

        if (id == 2)
        {
            this.tile.maxBurnTime = data;
        }
    }

    public void addPlayerInventory(InventoryPlayer playerInventory)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index > 6)
            {
                if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 6, 7, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (itemstack1.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) || itemstack1.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
                    {
                        if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }

                    if (!this.mergeItemStack(itemstack1, 2, 6, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }

                if (index < 34)
                {
                    if (!this.mergeItemStack(itemstack1, 34, 43, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 40 && !this.mergeItemStack(itemstack1, 7, 34, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 7, 43, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}

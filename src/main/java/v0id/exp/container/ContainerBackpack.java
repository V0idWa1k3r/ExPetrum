package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerBackpack extends Container
{
    public IItemHandler cap;

    public ContainerBackpack(InventoryPlayer inventoryPlayer, ItemStack backpack)
    {
        this.cap = backpack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        this.addPotInventory(cap);
        this.addPlayerInventory(inventoryPlayer);
    }

    public void addPotInventory(IItemHandler cap)
    {
        for (int i = 0; i < cap.getSlots(); ++i)
        {
            this.addSlotToContainer(new SlotItemHandler(cap, i, 8 + (i % 9) * 18, (cap.getSlots() == 18 ? 28 : 34) + (i / 9) * 18)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return super.isItemValid(stack) && !stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                }
            });
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

            if (index < this.cap.getSlots())
            {
                if (!this.mergeItemStack(itemstack1, this.cap.getSlots(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.cap.getSlots(), false))
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
        }

        return itemstack;
    }
}

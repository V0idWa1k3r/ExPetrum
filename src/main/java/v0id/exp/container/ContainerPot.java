package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import v0id.api.exp.item.IMold;

import javax.annotation.Nonnull;

public class ContainerPot extends Container
{
    public ItemStack pot;
    public int slots;

    public ContainerPot(InventoryPlayer inventoryPlayer, IItemHandler cap)
    {
        slots = 4;
        this.addPotInventory(cap);
        this.addPlayerInventory(inventoryPlayer);
    }

    public ContainerPot(InventoryPlayer playerInventory, ItemStack pot)
    {
        this.pot = pot;
        if (this.pot.hasTagCompound() && this.pot.getTagCompound().hasKey("metals"))
        {
            slots = 1;
        }
        else
        {
            slots = 4;
        }

        IItemHandler cap = pot.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing) null);
        this.addPotInventory(cap);
        this.addPlayerInventory(playerInventory);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
    }

    public void addPotInventory(IItemHandler cap)
    {
        if (slots == 4)
        {
            this.addSlotToContainer(new SlotItemHandler(cap, 0, 70, 27)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return super.isItemValid(stack) && !stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                }
            });

            this.addSlotToContainer(new SlotItemHandler(cap, 1, 88, 27)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return super.isItemValid(stack) && !stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                }
            });

            this.addSlotToContainer(new SlotItemHandler(cap, 2, 70, 45)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return super.isItemValid(stack) && !stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                }
            });

            this.addSlotToContainer(new SlotItemHandler(cap, 3, 88, 45)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return super.isItemValid(stack) && !stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                }
            });
        }
        else
        {
            this.addSlotToContainer(new SlotItemHandler(cap, 0, 80, 34)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return stack.getItem() instanceof IMold && ((IMold) stack.getItem()).isMold(stack);
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

            if (index < slots)
            {
                if (!this.mergeItemStack(itemstack1, slots, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, slots, false))
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

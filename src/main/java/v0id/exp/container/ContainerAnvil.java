package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import v0id.api.exp.item.IHammer;
import v0id.exp.item.ItemGeneric;
import v0id.exp.tile.TileAnvil;

public class ContainerAnvil extends Container
{
    public TileAnvil tile;

    public ContainerAnvil(InventoryPlayer playerInventory, TileAnvil anvil)
    {
        this.tile = anvil;
        this.addSlotToContainer(new SlotItemHandler(anvil.inventory, 0, 8, 8));
        this.addSlotToContainer(new SlotItemHandler(anvil.inventory, 1, 26, 8));
        this.addSlotToContainer(new SlotItemHandler(anvil.inventory, 2, 17, 46));
        this.addSlotToContainer(new SlotItemHandler(anvil.inventory, 3, 8, 66));
        this.addSlotToContainer(new SlotItemHandler(anvil.inventory, 4, 26, 66));
        this.addSlotToContainer(new SlotItemHandler(anvil.inventory, 5, 152, 66));
        this.addPlayerInventory(playerInventory);
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

            if (index > 5)
            {
                if (itemstack1.getItem() instanceof IHammer)
                {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (itemstack1.getItem() instanceof ItemGeneric && itemstack1.getMetadata() == ItemGeneric.EnumGenericType.FLUX.ordinal())
                    {
                        if (!this.mergeItemStack(itemstack1, 5, 6, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else
                    {
                        if (!this.mergeItemStack(itemstack1, 0, 4, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }

                if (index < 33)
                {
                    if (!this.mergeItemStack(itemstack1, 33, 42, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 40 && !this.mergeItemStack(itemstack1, 6, 33, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 6, 42, false))
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

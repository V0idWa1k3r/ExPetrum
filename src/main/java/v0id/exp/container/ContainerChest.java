package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import v0id.exp.tile.TileChest;

public class ContainerChest extends Container
{
    public TileChest chest;

    public ContainerChest(InventoryPlayer inventoryPlayer, TileChest chest)
    {
        this.addPotInventory(chest.inventory);
        this.addPlayerInventory(inventoryPlayer);
        this.chest = chest;
        ++this.chest.currentPlayers;
        chest.getWorld().playSound(null, chest.getPos().getX(), chest.getPos().getY(), chest.getPos().getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1, 1);
    }

    public void addPotInventory(IItemHandler cap)
    {
        for (int i = 0; i < cap.getSlots(); ++i)
        {
            this.addSlotToContainer(new SlotItemHandler(cap, i, 8 + (i % 9) * 18, 28 + i / 9 * 18));
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

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        --this.chest.currentPlayers;
        chest.getWorld().playSound(null, chest.getPos().getX(), chest.getPos().getY(), chest.getPos().getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 1, 1);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.chest.inventory.getSlots())
            {
                if (!this.mergeItemStack(itemstack1, this.chest.inventory.getSlots(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.chest.inventory.getSlots(), false))
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

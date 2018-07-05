package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import v0id.api.exp.item.ITuyere;
import v0id.exp.tile.TileBloomery;

import javax.annotation.Nonnull;

public class ContainerBloomery extends Container
{
    public TileBloomery tile;
    public float temperature;
    public int burnTime;

    public ContainerBloomery(InventoryPlayer playerInventory, TileBloomery bloomery)
    {
        this.tile = bloomery;
        this.addSlotToContainer(new SlotItemHandler(bloomery.inventory, 0, 26, 8));
        this.addSlotToContainer(new SlotItemHandler(bloomery.inventory, 1, 62, 8));
        this.addSlotToContainer(new SlotItemHandler(bloomery.inventory, 2, 44, 44)
        {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack)
            {
                return false;
            }
        });

        this.addSlotToContainer(new SlotItemHandler(bloomery.inventory, 3, 134, 26));
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
            if (this.temperature != this.tile.temperatureHandler.getCurrentTemperature())
            {
                icontainerlistener.sendWindowProperty(this, 0, Float.floatToIntBits(this.tile.temperatureHandler.getCurrentTemperature()));
            }

            if (this.burnTime != this.tile.work)
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tile.work);
            }
        }

        this.temperature = this.tile.temperatureHandler.getCurrentTemperature();
        this.burnTime = this.tile.work;
}

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id == 0)
        {
            this.tile.temperatureHandler.setTemperatureBypassMax(Float.intBitsToFloat(data));
        }

        if (id == 1)
        {
            this.tile.work = data;
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

            if (index >= 4)
            {
                if (TileBloomery.isItemCharcoal(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (itemstack1.getItem() instanceof ITuyere)
                    {
                        if (!this.mergeItemStack(itemstack1, 3, 4, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else
                    {
                        if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }

                if (index < 31)
                {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4, 40, false))
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

package v0id.exp.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import v0id.exp.tile.TileForge;

public class ContainerForge extends Container
{
    public TileForge tile;
    public float temperature;
    public int burnTime;
    public int burnTimeMax;

    public ContainerForge(InventoryPlayer playerInventory, TileForge forge)
    {
        this.tile = forge;
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 0, 44, 8));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 1, 62, 26));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 2, 80, 26));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 3, 98, 26));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 4, 116, 8));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 5, 44, 44));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 6, 62, 62));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 7, 80, 62));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 8, 98, 62));
        this.addSlotToContainer(new SlotItemHandler(forge.inventory, 9, 116, 44));
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

            if (index >= 10)
            {
                if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 5, 10, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (!this.mergeItemStack(itemstack1, 0, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }

                if (index < 37)
                {
                    if (!this.mergeItemStack(itemstack1, 37, 46, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 40 && !this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
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

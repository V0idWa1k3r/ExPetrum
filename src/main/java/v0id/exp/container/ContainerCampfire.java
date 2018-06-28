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
import v0id.exp.tile.TileCampfire;

public class ContainerCampfire extends Container
{
    public TileCampfire tile;
    public float temperature;
    public int burnTime;
    public int burnTimeMax;

    public ContainerCampfire(InventoryPlayer playerInventory, TileCampfire campfire)
    {
        this.tile = campfire;
        this.addSlotToContainer(new SlotItemHandler(campfire.inventory_thing, 0, 80, 19));
        this.addSlotToContainer(new SlotItemHandler(campfire.inventory_wood, 0, 62, 54));
        this.addSlotToContainer(new SlotItemHandler(campfire.inventory_wood, 1, 80, 54));
        this.addSlotToContainer(new SlotItemHandler(campfire.inventory_wood, 2, 98, 54));
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
            this.tile.temperature_handler.setTemperature(Float.intBitsToFloat(data), false);
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

            if (index != 0 && index != 1 && index != 2 && index != 3)
            {
                if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 4, false))
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

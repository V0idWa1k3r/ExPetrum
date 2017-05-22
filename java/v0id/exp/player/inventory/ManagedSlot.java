package v0id.exp.player.inventory;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ManagedSlot extends Slot
{
	public final Slot ref;
	
	public ManagedSlot(Slot s)
	{
		super(s.inventory, s.slotNumber, s.xPos, s.yPos);
		this.ref = s;
		this.slotNumber = s.slotNumber;
	}

	@Override
	public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_)
    {
		this.ref.onSlotChange(p_75220_1_, p_75220_2_);
    }

    @Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        return this.ref.onTake(thePlayer, stack);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return this.ref.isItemValid(stack) && PlayerInventoryHelper.canSlotAccept(stack, Optional.empty(), ((InventoryPlayer)this.inventory).player, this.getSlotIndex());
    }

    @Override
	public ItemStack getStack()
    {
        return this.ref.getStack();
    }

    @Override
	public boolean getHasStack()
    {
        return this.ref.getHasStack();
    }
    
    @Override
	public void putStack(ItemStack stack)
    {
    	this.ref.putStack(stack);
    }

    @Override
	public void onSlotChanged()
    {
        this.ref.onSlotChanged();
    }
    
    @Override
	public int getSlotStackLimit()
    {
        return this.ref.getSlotStackLimit();
    }

    @Override
	public int getItemStackLimit(ItemStack stack)
    {
        return this.ref.getItemStackLimit(stack);
    }

    @Override
	@Nullable
    @SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {
        return this.ref.getSlotTexture();
    }

    @Override
	public ItemStack decrStackSize(int amount)
    {
        return this.ref.decrStackSize(amount);
    }

    
    @Override
	public boolean isHere(IInventory inv, int slotIn)
    {
        return this.ref.isHere(inv, slotIn);
    }

    @Override
	public boolean canTakeStack(EntityPlayer playerIn)
    {
        return this.ref.canTakeStack(playerIn);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean canBeHovered()
    {
        return this.ref.canBeHovered();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public net.minecraft.util.ResourceLocation getBackgroundLocation()
    {
        return this.ref.getBackgroundLocation();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void setBackgroundLocation(net.minecraft.util.ResourceLocation texture)
    {
        this.ref.setBackgroundLocation(texture);
    }

    
    @Override
	public void setBackgroundName(String name)
    {
       	this.ref.setBackgroundName(name);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public net.minecraft.client.renderer.texture.TextureAtlasSprite getBackgroundSprite()
    {
        return this.ref.getBackgroundSprite();
    }

    @Override
	public int getSlotIndex()
    {
        return this.ref.getSlotIndex();
    }
    
    @Override
	public boolean isSameInventory(Slot other)
    {
        return this.ref.isSameInventory(other);
    }
}

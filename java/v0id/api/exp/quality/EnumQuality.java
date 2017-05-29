package v0id.api.exp.quality;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;

public enum EnumQuality implements IStringSerializable
{
	PLAIN(EnumRarity.COMMON),
	DECENT(EnumRarity.UNCOMMON),
	GOOD(EnumRarity.RARE),
	PERFECT(EnumRarity.EPIC);
	
	private final EnumRarity associatedRarity;

	private EnumQuality(EnumRarity associatedRarity)
	{
		this.associatedRarity = associatedRarity;
	}

	public EnumRarity getAssociatedRarity()
	{
		return this.associatedRarity;
	}

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}
	
	public void writeToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setByte("exp.quality", (byte) this.ordinal());
	}
	
	public static EnumQuality readFromNBTTagCompound(NBTTagCompound tag)
	{
		return values()[Math.min(tag.getByte("exp.quality"), values().length - 1)];
	}
	
	public static EnumQuality ofItem(ItemStack item)
	{
		return item.hasTagCompound() ? readFromNBTTagCompound(item.getTagCompound()) : EnumQuality.PLAIN;
	}
	
	public void setForItem(ItemStack is)
	{
		if (!is.hasTagCompound())
		{
			is.setTagCompound(new NBTTagCompound());
		}
		
		this.writeToNBTTagCompound(is.getTagCompound());
	}
}

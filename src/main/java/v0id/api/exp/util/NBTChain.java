package v0id.api.exp.util;

import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("SameParameterValue")
public class NBTChain
{
	private final NBTTagCompound internalTag;
	
	private NBTChain()
	{
		this.internalTag = new NBTTagCompound();
	}
	
	public NBTChain withByte(String key, byte b)
	{
		this.internalTag.setByte(key, b);
		return this;
	}

	public NBTChain withShort(String key, short s)
	{
		this.internalTag.setShort(key, s);
		return this;
	}
	
	public NBTChain withInt(String key, int i)
	{
		this.internalTag.setInteger(key, i);
		return this;
	}
	
	public NBTChain withLong(String key, long l)
	{
		this.internalTag.setLong(key, l);
		return this;
	}
	
	public NBTChain withString(String key, String s)
	{
		this.internalTag.setString(key, s);
		return this;
	}
	
	public NBTChain withDouble(String key, double d)
	{
		this.internalTag.setDouble(key, d);
		return this;
	}
	
	public NBTChain withFloat(String key, float f)
	{
		this.internalTag.setFloat(key, f);
		return this;
	}
	
	public NBTChain withByteArray(String key, byte[] b)
	{
		this.internalTag.setByteArray(key, b);
		return this;
	}
	
	public NBTChain withBool(String key, boolean b)
	{
		this.internalTag.setBoolean(key, b);
		return this;
	}
	
	public NBTChain withTag(String key, NBTTagCompound tag)
	{
		this.internalTag.setTag(key, tag);
		return this;
	}
	
	public NBTTagCompound endChain()
	{
		return this.internalTag;
	}
	
	public static NBTChain startChain()
	{
		return new NBTChain();
	}
}

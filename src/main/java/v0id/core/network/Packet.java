package v0id.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import v0id.core.markers.Default;

@SuppressWarnings("WeakerAccess")
public class Packet implements IMessage
{
	public NBTTagCompound getData()
	{
		return data;
	}

	public short getId()
	{
		return id;
	}

	NBTTagCompound data;
	short id;
	
	@Default
	public Packet()
	{
		
	}
	
	public Packet(PacketType type, NBTTagCompound data)
	{
		this.id = type.internalID;
		this.data = data;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.data = ByteBufUtils.readTag(buf);
		assert this.data != null : String.format("VoidApi detected severe networking issue - packet %s carried invalid NBT data!", this.id);
		this.id = this.data.getShort("packetID");	
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		this.data.setShort("packetID", this.id);
		ByteBufUtils.writeTag(buf, this.data);
	}

}

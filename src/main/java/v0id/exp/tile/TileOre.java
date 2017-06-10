package v0id.exp.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import v0id.api.exp.block.EnumOre;

public class TileOre extends TileEntity
{
	public EnumOre type;
	public byte subtype;
	public byte amount;
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.type = EnumOre.values()[compound.getByte("oreType")];
		this.subtype = compound.getByte("subtype");
		this.amount = compound.getByte("amount");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound ret = super.writeToNBT(compound);
		ret.setByte("oreType", (byte) this.type.ordinal());
		ret.setByte("subtype", this.subtype);
		ret.setByte("amount", this.amount);
		return ret;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.getPos(), 0, this.serializeNBT());
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.serializeNBT();
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.deserializeNBT(pkt.getNbtCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.deserializeNBT(tag);
	}
}

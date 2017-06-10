package v0id.exp.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.exp.crop.ExPFarmland;

public class TileFarmland extends TileEntity
{
	public ExPFarmland farmlandState;
	
	public TileFarmland()
	{
		super();
		this.farmlandState = ExPFarmland.createWithTile(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.farmlandState.deserializeNBT(compound.getCompoundTag("farmlandState"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound ret = super.writeToNBT(compound);
		ret.setTag("farmlandState", this.farmlandState.serializeNBT());
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

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ExPFarmlandCapability.farmlandCap && (facing == EnumFacing.UP || facing == null) ? true : super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ExPFarmlandCapability.farmlandCap && (facing == EnumFacing.UP || facing == null) ? (T) this.farmlandState : super.getCapability(capability, facing);
	}
}

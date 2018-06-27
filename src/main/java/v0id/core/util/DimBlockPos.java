package v0id.core.util;

import com.google.common.hash.Hashing;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class DimBlockPos implements Serializable, INBTSerializable<NBTTagCompound>
{
	public BlockPos pos;
	public int dim;
	
	public DimBlockPos()
	{
		
	}
	
	public DimBlockPos(BlockPos pos, int i)
	{
		this.pos = pos;
		this.dim = i;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof DimBlockPos && ((DimBlockPos)o).pos.equals(this.pos) && ((DimBlockPos)o).dim == this.dim;
	}
	
	@Override
	public int hashCode()
	{
		return Hashing.murmur3_32().newHasher().putInt(this.dim).putInt(this.pos.hashCode()).hash().asInt();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(this.dim);
		out.writeInt(this.pos.getX());
		out.writeInt(this.pos.getY());
		out.writeInt(this.pos.getZ());
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.dim = in.readInt();
		this.pos = new BlockPos(in.readInt(), in.readInt(), in.readInt());
	}
	
	private void readObjectNoData() throws ObjectStreamException
	{
		this.dim = 0;
		this.pos = BlockPos.ORIGIN;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound ret = new NBTTagCompound();
		ret.setInteger("x", this.pos.getX());
		ret.setInteger("y", this.pos.getY());
		ret.setInteger("z", this.pos.getZ());
		ret.setInteger("dim", this.dim);
		return ret;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
		this.dim = nbt.getInteger("dim");
	}
}

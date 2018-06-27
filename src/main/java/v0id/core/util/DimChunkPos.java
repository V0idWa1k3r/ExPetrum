package v0id.core.util;

import com.google.common.hash.Hashing;
import net.minecraft.util.math.ChunkPos;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class DimChunkPos implements Serializable
{
	ChunkPos pos;

	public ChunkPos getPos()
	{
		return pos;
	}

	public void setPos(ChunkPos pos)
	{
		this.pos = pos;
	}

	public int getDim()
	{
		return dim;
	}

	public void setDim(int dim)
	{
		this.dim = dim;
	}

	int dim;
	
	public DimChunkPos(ChunkPos pos, int i)
	{
		this.pos = pos;
		this.dim = i;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof DimChunkPos && ((DimChunkPos)o).pos.equals(this.pos) && ((DimChunkPos)o).dim == this.dim; 
	}
	
	@Override
	public int hashCode()
	{
		return Hashing.murmur3_32().newHasher().putInt(this.dim).putInt(this.pos.hashCode()).hash().asInt();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(this.dim);
		out.writeInt(this.pos.x);
		out.writeInt(this.pos.z);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.dim = in.readInt();
		this.pos = new ChunkPos(in.readInt(), in.readInt());
	}
	
	private void readObjectNoData() throws ObjectStreamException
	{
		this.dim = 0;
		this.pos = new ChunkPos(0, 0);
	}
}

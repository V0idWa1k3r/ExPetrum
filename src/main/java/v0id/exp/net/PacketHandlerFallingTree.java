package v0id.exp.net;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import v0id.api.core.VoidApi;
import v0id.api.core.network.IPacketHandler;
import v0id.exp.entity.EntityFallingTree;

public class PacketHandlerFallingTree implements IPacketHandler
{
	@Override
	public void handleData(NBTTagCompound data)
	{
		UUID id = UUID.fromString(data.getString("uuid"));
		NBTTagCompound dataTag = data.getCompoundTag("dataTag");
		Holder<EntityFallingTree> fallingTreeRef = new Holder();
		for (int i = 0; i < VoidApi.proxy.getClientWorld().getLoadedEntityList().size(); ++i)
		{
			this.tryMatch(VoidApi.proxy.getClientWorld().getLoadedEntityList().get(i), fallingTreeRef, id);
			if (fallingTreeRef.has())
			{
				break;
			}
		}
		
		if (fallingTreeRef.has())
		{
			EntityFallingTree tree = fallingTreeRef.get();
			VoidApi.proxy.getClientListener().addScheduledTask(() -> tree.deserializeDataFromNBT(dataTag));
		}
	}
	
	public void tryMatch(Entity e, Holder<EntityFallingTree> ref, UUID id)
	{
		if (!ref.has())
		{
			if (id.equals(e.getPersistentID()))
			{
				ref.set((EntityFallingTree) e);
			}
		}
	}

	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}

	private class Holder<T>
	{
		T obj;
		
		public Holder()
		{
			
		}
		
		public void set(T t)
		{
			this.obj = t;
		}
		
		public T get()
		{
			return this.obj;
		}
		
		public boolean has()
		{
			return this.obj != null;
		}
	}
}

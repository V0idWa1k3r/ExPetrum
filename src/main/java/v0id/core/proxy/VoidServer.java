package v0id.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import v0id.core.util.IItemColorProvider;

public class VoidServer implements IVoidProxy
{
	public String getClientPlayerName()
	{
		return "";
	}

	public World getClientWorld()
	{
		return null;
	}

	public IThreadListener getClientListener()
	{
		return null;
	}

	public Object getFontRenderer(String s)
	{
		return null;
	}

	public void registerAllItemModels(Class<?> c)
	{

	}

	public void registerItemColor(IItemColorProvider provider, Item... items)
	{

	}

	public String format(String s, Object... objects)
	{
		return s;
	}
	
	public EntityPlayer getClientPlayer()
	{
		return null;
	}
	
	public Object provideClientOnlyInstance(String classname)
	{
		return null;
	}
	
	public void subscribeClient(String classname, EventBus bus)
	{
		
	}
	
	public void executeOnClient(String classname, String methodname, Object instance, Object... params)
	{
		
	}
	
	public int getClientGrassColor(IBlockAccess w, BlockPos pos)
	{
		return -1;
	}
	
	public float getRenderPartialTicks()
	{
		return 1.0F;
	}
}

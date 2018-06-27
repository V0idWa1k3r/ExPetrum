package v0id.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import v0id.core.util.IItemColorProvider;

public interface IVoidProxy
{
	String getClientPlayerName();
	
	World getClientWorld();
	
	IThreadListener getClientListener();
	
	Object getFontRenderer(String s);
	
	void registerAllItemModels(Class<?> c);
	
	void registerItemColor(IItemColorProvider provider, Item...items);
	
	String format(String s, Object...objects);
	
	EntityPlayer getClientPlayer();
	
	Object provideClientOnlyInstance(String classname);
	
	void subscribeClient(String classname, EventBus bus);
	
	void executeOnClient(String classname, String methodname, Object instance, Object... params);
	
	int getClientGrassColor(IBlockAccess w, BlockPos pos);

	float getRenderPartialTicks();
}

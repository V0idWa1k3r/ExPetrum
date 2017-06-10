package v0id.exp.registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.core.VoidApi;
import v0id.exp.handler.ExPHandlerServer;
import v0id.exp.handler.ExPHandlerWorldGen;

public class ExPEventRegistry extends AbstractRegistry
{
	public static final ExPHandlerServer handlerServer = new ExPHandlerServer();
	public static final ExPHandlerWorldGen handlerWorld = new ExPHandlerWorldGen();

	public ExPEventRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		MinecraftForge.EVENT_BUS.register(handlerServer);
		MinecraftForge.TERRAIN_GEN_BUS.register(handlerWorld);
		VoidApi.proxy.subscribeClient("v0id.exp.handler.ExPHandlerClient", MinecraftForge.EVENT_BUS);
	}
}

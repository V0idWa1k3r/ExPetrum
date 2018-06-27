package v0id.core;

import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.cfg.CVCfg;
import v0id.core.logging.LogLevel;
import v0id.core.markers.Default;
import v0id.core.network.Packet;
import v0id.core.network.VoidNetHandler;
import v0id.core.network.VoidNetwork;
import v0id.core.proxy.IVoidProxy;
import v0id.core.util.java.Reflector;

@SuppressWarnings("WeakerAccess")
public class VoidApi
{
	public static VoidApi instance;
	public static CVCfg config;
	
	@SidedProxy(clientSide = "v0id.core.proxy.VoidClient", serverSide = "v0id.core.proxy.VoidServer")
	public static IVoidProxy proxy;
	
	@Default
	public VoidApi()
	{
		instance = this;
	}
	
	@InstanceFactory
	public static VoidApi getInstance()
	{
		return instance == null ? new VoidApi() : instance;
	}

	public void preInit(FMLPreInitializationEvent event)
	{
		instance = this;
		VCLoggers.loggerMod.log(LogLevel.Fine, "%s created.", VCConsts.modname);
		Reflector.preloadClass("v0id.api.core.event.handler.VCEventHandler");
		VoidNetwork.networkManager = NetworkRegistry.INSTANCE.newSimpleChannel(VCConsts.modid + ".net");
		VoidNetwork.networkManager.registerMessage(VoidNetHandler.class, Packet.class, 0, Side.CLIENT);
		VoidNetwork.networkManager.registerMessage(VoidNetHandler.class, Packet.class, 0, Side.SERVER);
	}
}

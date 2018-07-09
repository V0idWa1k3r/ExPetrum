package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.exp.net.ExPNetwork;

public class ExPNetworkRegistry extends AbstractRegistry
{
	public ExPNetworkRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		ExPNetwork.init();
	}
}

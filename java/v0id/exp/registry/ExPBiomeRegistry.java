package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPMisc;
import v0id.exp.world.gen.WorldTypeExP;

public class ExPBiomeRegistry extends AbstractRegistry
{

	public ExPBiomeRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		ExPMisc.worldTypeExP = new WorldTypeExP("expworld");
	}

	@Override
	public void postInit(FMLPostInitializationEvent evt)
	{
		super.postInit(evt);
	}
}

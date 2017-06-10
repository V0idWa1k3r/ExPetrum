package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.player.ExPPlayerCapability;
import v0id.api.exp.tile.crop.ExPCropCapability;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.ExPSeedsCapability;
import v0id.api.exp.world.ExPWorldCapability;

public class ExPCapabilityRegistry extends AbstractRegistry
{
	public ExPCapabilityRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		ExPPlayerCapability.register();
		ExPWorldCapability.register();
		ExPCropCapability.register();
		ExPFarmlandCapability.register();
		ExPSeedsCapability.register();
	}
}

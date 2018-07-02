package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.metal.AnvilMinigame;

public class ExPMiscRegistry extends AbstractRegistry
{
	public ExPMiscRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
        AnvilMinigame.ensureAllCardsAreRegistered();
	}
}

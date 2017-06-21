package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPItems;
import v0id.exp.util.OreDictManager;

public class ExPOreDictRegistry extends AbstractRegistry
{
	public ExPOreDictRegistry()
	{
		super();
	}

	@Override
	public void init(FMLInitializationEvent evt)
	{
		OreDictManager.mapStorage(ExPBlocks.class);
		OreDictManager.mapStorage(ExPItems.class);
		OreDictManager.register();
	}
}

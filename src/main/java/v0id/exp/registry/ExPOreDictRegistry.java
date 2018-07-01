package v0id.exp.registry;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
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
		OreDictionary.registerOre("flint", Items.FLINT);
	}
}

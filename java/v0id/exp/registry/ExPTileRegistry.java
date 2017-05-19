package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.exp.tile.TileCrop;
import v0id.exp.tile.TileOre;
import v0id.exp.tile.TileWorkedBoulder;

public class ExPTileRegistry extends AbstractRegistry
{
	public ExPTileRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		GameRegistry.registerTileEntity(TileOre.class, "exp:ore");
		GameRegistry.registerTileEntity(TileWorkedBoulder.class, "exp:workedBoulder");
		GameRegistry.registerTileEntity(TileCrop.class, "exp:crop");
	}
}

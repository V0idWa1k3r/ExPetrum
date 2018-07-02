package v0id.exp.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.exp.tile.*;

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
		GameRegistry.registerTileEntity(TileOre.class, new ResourceLocation("exp:ore"));
		GameRegistry.registerTileEntity(TileWorkedBoulder.class, new ResourceLocation("exp:workedBoulder"));
		GameRegistry.registerTileEntity(TileCrop.class, new ResourceLocation("exp:crop"));
		GameRegistry.registerTileEntity(TileFarmland.class, new ResourceLocation("exp:farmland"));
		GameRegistry.registerTileEntity(TileCampfire.class, new ResourceLocation("exp:campfire"));
		GameRegistry.registerTileEntity(TilePot.class, new ResourceLocation("exp:pot"));
		GameRegistry.registerTileEntity(TilePotteryStation.class, new ResourceLocation("exp:pottery_station"));
		GameRegistry.registerTileEntity(TileLogPile.class, new ResourceLocation("exp:log_pile"));
		GameRegistry.registerTileEntity(TileCrate.class, new ResourceLocation("exp:crate"));
		GameRegistry.registerTileEntity(TileForge.class, new ResourceLocation("exp:forge"));
		GameRegistry.registerTileEntity(TileQuern.class, new ResourceLocation("exp:quern"));
	}
}

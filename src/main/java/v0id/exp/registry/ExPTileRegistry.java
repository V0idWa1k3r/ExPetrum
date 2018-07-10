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
		GameRegistry.registerTileEntity(TileAnvil.class, new ResourceLocation("exp:anvil"));
		GameRegistry.registerTileEntity(TileCrucible.class, new ResourceLocation("exp:crucible"));
		GameRegistry.registerTileEntity(TileNestingBox.class, new ResourceLocation("exp:nesting_box"));
		GameRegistry.registerTileEntity(TileBarrel.class, new ResourceLocation("exp:barrel"));
		GameRegistry.registerTileEntity(TileScrapingRack.class, new ResourceLocation("exp:scraping_rack"));
		GameRegistry.registerTileEntity(TileBellows.class, new ResourceLocation("exp:bellows"));
		GameRegistry.registerTileEntity(TileSpinningWheel.class, new ResourceLocation("exp:spinning_wheel"));
		GameRegistry.registerTileEntity(TileBloomery.class, new ResourceLocation("exp:bloomery"));
		GameRegistry.registerTileEntity(TileFruitPress.class, new ResourceLocation("exp:fruit_press"));
		GameRegistry.registerTileEntity(TileChest.class, new ResourceLocation("exp:chest"));
		GameRegistry.registerTileEntity(TileShaft.class, new ResourceLocation("exp:shaft"));
		GameRegistry.registerTileEntity(TileGearbox.class, new ResourceLocation("exp:gearbox"));
		GameRegistry.registerTileEntity(TileWaterWheel.class, new ResourceLocation("exp:water_wheel"));
		GameRegistry.registerTileEntity(TileStructurePiece.class, new ResourceLocation("exp:structure"));
		GameRegistry.registerTileEntity(TileSplitterGearbox.class, new ResourceLocation("exp:splitter_gearbox"));
		GameRegistry.registerTileEntity(TileMechanicalQuern.class, new ResourceLocation("exp:mechanical_quern"));
		GameRegistry.registerTileEntity(TileWindmill.class, new ResourceLocation("exp:windmill"));
		GameRegistry.registerTileEntity(TileMechanicalBellows.class, new ResourceLocation("exp:mechanical_bellows"));
		GameRegistry.registerTileEntity(TileSaw.class, new ResourceLocation("exp:saw"));
		GameRegistry.registerTileEntity(TileMechanicalPotteryStation.class, new ResourceLocation("exp:mechanical_pottery_station"));
		GameRegistry.registerTileEntity(TileBlastFurnace.class, new ResourceLocation("exp:blast_furnace"));
	}
}

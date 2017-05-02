package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPBiomes;
import v0id.exp.world.biome.impl.ExPBeach;
import v0id.exp.world.biome.impl.ExPColdForest;
import v0id.exp.world.biome.impl.ExPColdPlains;
import v0id.exp.world.biome.impl.ExPDenseColdForest;
import v0id.exp.world.biome.impl.ExPDenseForest;
import v0id.exp.world.biome.impl.ExPDenseWarmForest;
import v0id.exp.world.biome.impl.ExPDesert;
import v0id.exp.world.biome.impl.ExPForest;
import v0id.exp.world.biome.impl.ExPHills;
import v0id.exp.world.biome.impl.ExPJungle;
import v0id.exp.world.biome.impl.ExPMountains;
import v0id.exp.world.biome.impl.ExPOcean;
import v0id.exp.world.biome.impl.ExPPlains;
import v0id.exp.world.biome.impl.ExPRareForest;
import v0id.exp.world.biome.impl.ExPRiver;
import v0id.exp.world.biome.impl.ExPSavanna;
import v0id.exp.world.biome.impl.ExPSwampland;
import v0id.exp.world.biome.impl.ExPWarmForest;
import v0id.exp.world.biome.impl.ExPWarmPlains;

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
		ExPBiomes.river = ExPRiver.create();
		ExPBiomes.ocean = ExPOcean.create();
		ExPBiomes.beach = ExPBeach.create();
		ExPBiomes.plains = ExPPlains.create();
		ExPBiomes.forest = ExPForest.create();
		ExPBiomes.mountains = ExPMountains.create();
		ExPBiomes.cold_plains = ExPColdPlains.create();
		ExPBiomes.cold_forest = ExPColdForest.create();
		ExPBiomes.warm_plains = ExPWarmPlains.create();
		ExPBiomes.warm_forest = ExPWarmForest.create();
		ExPBiomes.dense_forest = ExPDenseForest.create();
		ExPBiomes.dense_cold_forest = ExPDenseColdForest.create();
		ExPBiomes.dense_warm_forest = ExPDenseWarmForest.create();
		ExPBiomes.swampland = ExPSwampland.create();
		ExPBiomes.jungle = ExPJungle.create();
		ExPBiomes.desert = ExPDesert.create();
		ExPBiomes.rare_forest = ExPRareForest.create();
		ExPBiomes.hills = ExPHills.create();
		ExPBiomes.savanna = ExPSavanna.create();
	}

	@Override
	public void postInit(FMLPostInitializationEvent evt)
	{
		super.postInit(evt);
	}
}

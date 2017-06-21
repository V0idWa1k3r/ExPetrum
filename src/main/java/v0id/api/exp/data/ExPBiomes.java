package v0id.api.exp.data;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.core.markers.StaticStorage;

@StaticStorage
@GameRegistry.ObjectHolder(ExPRegistryNames.modid)
public class ExPBiomes
{
    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeRiver)
	public static final Biome river = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeOcean)
	public static final Biome ocean = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeBeach)
	public static final Biome beach = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomePlains)
	public static final Biome plains = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeForest)
	public static final Biome forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeMountains)
	public static final Biome mountains = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeColdPlains)
	public static final Biome cold_plains = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeColdForest)
	public static final Biome cold_forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeWarmPlains)
	public static final Biome warm_plains = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeWarmForest)
	public static final Biome warm_forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeDenseForest)
	public static final Biome dense_forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeDenseColdForest)
	public static final Biome dense_cold_forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeDenseWarmForest)
	public static final Biome dense_warm_forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeLake)
	public static final Biome swampland = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeJungle)
	public static final Biome jungle = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeDesert)
	public static final Biome desert = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeRareForest)
	public static final Biome rare_forest = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeHills)
	public static final Biome hills = null;

    @GameRegistry.ObjectHolder(ExPRegistryNames.biomeSavanna)
	public static final Biome savanna = null;
}

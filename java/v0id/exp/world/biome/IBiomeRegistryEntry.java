package v0id.exp.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public interface IBiomeRegistryEntry
{
	void registerBiome(IForgeRegistry<Biome> registry);
}

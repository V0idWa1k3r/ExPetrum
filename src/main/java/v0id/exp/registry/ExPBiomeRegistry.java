package v0id.exp.registry;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.exp.world.biome.IDictionariedBiome;
import v0id.exp.world.biome.impl.*;

import java.util.Arrays;
import java.util.List;

public class ExPBiomeRegistry extends AbstractRegistry
{
	public static ExPBiomeRegistry instance;
	public static List<Biome> registryEntries;
	
	public ExPBiomeRegistry()
	{
		super();
		instance = this;
	}

	public void registerBiomes(RegistryEvent.Register<Biome> event)
    {
        IForgeRegistry<Biome> registry = event.getRegistry();
        registryEntries = Arrays.asList(
                ExPRiver.create(),
                ExPOcean.create(),
                ExPBeach.create(),
                ExPPlains.create(),
                ExPForest.create(),
                ExPMountains.create(),
                ExPColdPlains.create(),
                ExPColdForest.create(),
                ExPWarmPlains.create(),
                ExPWarmForest.create(),
                ExPDenseForest.create(),
                ExPDenseColdForest.create(),
                ExPDenseWarmForest.create(),
                ExPLake.create(),
                ExPJungle.create(),
                ExPDesert.create(),
                ExPRareForest.create(),
                ExPHills.create(),
                ExPSavanna.create()
        );

        registryEntries.forEach(registry::register);
        registryEntries.stream().filter(e -> e instanceof IDictionariedBiome).map(e -> (IDictionariedBiome)e).forEach(IDictionariedBiome::registerTypes);
    }
}

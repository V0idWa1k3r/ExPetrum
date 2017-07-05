package v0id.exp.registry;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.exp.potion.PotionStunned;

import java.util.Collections;
import java.util.List;

public class ExPPotionRegistry extends AbstractRegistry
{
	public static ExPPotionRegistry instance;
	public static List<Potion> registryEntries;
	
	public ExPPotionRegistry()
	{
		super();
		instance = this;
	}

	public void registerPotions(RegistryEvent.Register<Potion> event)
    {
        IForgeRegistry<Potion> registry = event.getRegistry();
        registryEntries = Collections.singletonList(new PotionStunned());
        registryEntries.forEach(registry::register);
    }
}

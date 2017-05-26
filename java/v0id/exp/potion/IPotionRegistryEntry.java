package v0id.exp.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public interface IPotionRegistryEntry
{
	void registerPotion(IForgeRegistry<Potion> registry);
}

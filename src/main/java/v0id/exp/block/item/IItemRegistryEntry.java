package v0id.exp.block.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public interface IItemRegistryEntry
{
	void registerItem(IForgeRegistry<Item> registry);
}

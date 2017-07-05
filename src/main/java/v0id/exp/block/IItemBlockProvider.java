package v0id.exp.block;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by V0idWa1k3r on 21-Jun-17.
 */
public interface IItemBlockProvider
{
    void registerItem(IForgeRegistry<Item> registry);
}

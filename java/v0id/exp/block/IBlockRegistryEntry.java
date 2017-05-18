package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public interface IBlockRegistryEntry
{
	void registerBlock(IForgeRegistry<Block> registry);
}

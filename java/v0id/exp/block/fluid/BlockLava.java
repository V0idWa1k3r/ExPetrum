package v0id.exp.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;

public class BlockLava extends BlockFluidFinite implements IInitializableBlock, IBlockRegistryEntry, IItemRegistryEntry
{
	public BlockLava()
	{
		super(ExPFluids.lava, Material.LAVA);
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setBlockUnbreakable();
		this.setRegistryName(ExPRegistryNames.blockLava);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
		this.setLightOpacity(Blocks.LAVA.getLightOpacity(Blocks.LAVA.getDefaultState()));
		this.setLightLevel(1);
		this.setQuantaPerBlock(10);
		ExPHandlerRegistry.blockEntries.add(this);
		ExPHandlerRegistry.itemEntries.add(this);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.LAVA || other == Blocks.FLOWING_LAVA;
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerBlock(IForgeRegistry<Block> registry)
	{
		registry.register(this);
	}
}

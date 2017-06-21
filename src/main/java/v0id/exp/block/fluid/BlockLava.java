package v0id.exp.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWeighted;

public class BlockLava extends BlockFluidFinite implements IInitializableBlock, IItemBlockProvider
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
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockLava));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
		this.setLightOpacity(3);
		this.setLightLevel(1);
		this.setQuantaPerBlock(10);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.LAVA || other == Blocks.FLOWING_LAVA;
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWeighted(this));
	}
}

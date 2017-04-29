package v0id.exp.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;

public class BlockLava extends BlockFluidFinite
{
	public BlockLava()
	{
		super(ExPFluids.lava, Material.LAVA);
		this.initBlock();
	}
	
	public void initBlock()
	{
		this.setBlockUnbreakable();
		this.setRegistryName(ExPRegistryNames.blockLava);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setLightOpacity(Blocks.LAVA.getLightOpacity(Blocks.LAVA.getDefaultState()));
		this.setLightLevel(1);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		this.setQuantaPerBlock(10);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.LAVA || other == Blocks.FLOWING_LAVA;
	}
	
	
}


package v0id.exp.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWeighted;

public class BlockOil extends BlockFluidFinite implements IInitializableBlock, IItemBlockProvider
{
	public BlockOil()
	{
		super(ExPFluids.oil, Material.WATER);
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setBlockUnbreakable();
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockOil));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
		this.setLightOpacity(0);
		this.setQuantaPerBlock(10);
        Blocks.FIRE.setFireInfo(this, 1000, 2);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return false;
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWeighted(this));
	}
}

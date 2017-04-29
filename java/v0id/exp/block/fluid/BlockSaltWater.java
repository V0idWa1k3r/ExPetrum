package v0id.exp.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.exp.block.IWater;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;

public class BlockSaltWater extends BlockFluidFinite implements IWater
{
	public BlockSaltWater()
	{
		super(ExPFluids.saltWater, Material.WATER);
		this.initBlock();
	}
	
	public void initBlock()
	{
		this.setBlockUnbreakable();
		this.setRegistryName(ExPRegistryNames.blockSaltWater);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setLightOpacity(Blocks.WATER.getLightOpacity(Blocks.WATER.getDefaultState()));
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		this.setQuantaPerBlock(10);
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return plantable.getPlantType(world, pos) == EnumPlantType.Water && this.getQuantaPercentage(world, pos) >= 1;
	}

	@Override
	public boolean isSalt(World w, BlockPos pos)
	{
		return true;
	}
}

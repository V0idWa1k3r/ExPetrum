package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.api.exp.util.ColorRGB;
import v0id.api.exp.block.*;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.tile.TileFarmland;

import java.util.Random;

import static v0id.api.exp.block.property.EnumDirtClass.ACRISOL;
import static v0id.api.exp.data.ExPBlockProperties.DIRT_CLASS;

public class BlockFarmland extends BlockContainer implements IInitializableBlock, IItemBlockProvider, IGravitySusceptible, ICanGrowCrop, IGrass, IAcceptsWaterCan
{
	public BlockFarmland()
	{
		super(Material.GROUND);
		this.initBlock();
	}

	@Override
	public float getNutrientLevel(World w, BlockPos pos)
	{
		EnumDirtClass type = EnumDirtClass.values()[this.getMetaFromState(w.getBlockState(pos))];
		return type.getNutrientMultiplier();
	}

    @SuppressWarnings("deprecation")
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

	@Override
	public float getHumidityLevel(World w, BlockPos pos)
	{
		if (w.getTileEntity(pos) != null && w.getTileEntity(pos).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
		{
			return IFarmland.of(w.getTileEntity(pos)).getMoisture();
		}
		
		return 0;
	}

	@Override
	public float getGrowthMultiplier(World w, BlockPos pos)
	{
		if (w.getTileEntity(pos) != null && w.getTileEntity(pos).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
		{
			return IFarmland.of(w.getTileEntity(pos)).getGrowthMultiplier();
		}
		
		return 0;
	}

	@Override
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 0;
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}

	@Override
	public void initBlock()
	{
		this.setHardness(3.5f);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockFarmland));
		this.setResistance(3);
		this.setSoundType(SoundType.GROUND);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIRT_CLASS, ACRISOL));
		this.setCreativeTab(ExPCreativeTabs.tabCommon);
		this.setTickRandomly(true);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileFarmland();
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DIRT_CLASS, EnumDirtClass.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DIRT_CLASS).ordinal();
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
		{
			IFarmland.of(worldIn.getTileEntity(pos)).onWorldTick();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.onNeighborChange(worldIn, pos, fromPos);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		if (world instanceof World)
		{
			World w = (World) world;
			if (this.canFall(w, world.getBlockState(pos), pos, neighbor))
			{
				w.setBlockState(pos, ExPBlocks.soil.getDefaultState().withProperty(DIRT_CLASS, world.getBlockState(pos).getValue(DIRT_CLASS)));
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.neighborChanged(state, worldIn, pos, this, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ExPBlocks.soil);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.FARMLAND;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRT_CLASS);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		EnumPlantType type = plantable.getPlantType(world, pos.offset(direction));
		return direction == EnumFacing.UP && (type == EnumPlantType.Crop || type == EnumPlantType.Plains);
	}

	@Override
	public boolean isFertile(World world, BlockPos pos)
	{
		if (world.getTileEntity(pos) instanceof TileFarmland)
		{
			return IFarmland.of(world.getTileEntity(pos)).getMoisture() >= 0.5;
		}
		
		return super.isFertile(world, pos);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumDirtClass.values().length; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int getGrassColor(IBlockState state, BlockPos pos, IBlockAccess w)
	{
		int baseColor = 0x02661c;
		int mulColor = w.getBiome(pos).getGrassColorAtPos(pos);
		ColorRGB rgbBase = ColorRGB.FromHEX(baseColor);
		ColorRGB mulBase = ColorRGB.FromHEX(mulColor);
		return ((int)((rgbBase.getR() + mulBase.getR()) * 127.5) << 16) + ((int)((rgbBase.getG() + mulBase.getG()) * 127.5) << 8) + (int)((rgbBase.getB() + mulBase.getB()) * 127.5);
	}

	@Override
	public EnumGrassState getState()
	{
		return EnumGrassState.NORMAL;
	}

	@Override
	public EnumGrassAmount getAmount(IBlockState state, BlockPos pos, IBlockAccess w)
	{
		return EnumGrassAmount.values()[Math.max(state.getValue(DIRT_CLASS).getAmount().ordinal() - 1, 0)];
	}

	@Override
	public void acceptWatering(EntityPlayer player, World w, BlockPos pos, IBlockState state, IFluidHandlerItem wateringCanCapability, ItemStack wateringCanStack, int wateringCanTier)
	{
		if (player.ticksExisted % 20 == 0 && !w.isRemote)
		{
			TileEntity tile = w.getTileEntity(pos);
			if (tile != null && tile.hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
			{
				FluidStack cost = new FluidStack(FluidRegistry.WATER, (int)(10 * (1F - (float)wateringCanTier / 10)));
				FluidStack tryDrain = wateringCanCapability.drain(cost, false);
				if (tryDrain != null && tryDrain.amount == cost.amount)
				{
					IFarmland farmlandCap = tile.getCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP);
					if (farmlandCap.getMoisture() <= 0.91F)
					{
						farmlandCap.setMoisture(farmlandCap.getMoisture() + 0.1F);
						wateringCanCapability.drain(cost, true);
						farmlandCap.setDirty();
					}
				}
			}
		}
	}
}

package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.IHasSpecialName;
import v0id.api.exp.block.property.EnumKaolinType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemGeneric;

import java.util.Random;

import static v0id.api.exp.data.ExPBlockProperties.KAOLIN_TYPE;

public class BlockKaolin extends Block implements IInitializableBlock, IGravitySusceptible, IItemBlockProvider, IHasSpecialName, IWeightProvider
{
    public BlockKaolin()
    {
        super(Material.ROCK);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(2);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockKaolin));
        this.setResistance(6);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setDefaultState(this.blockState.getBaseState().withProperty(KAOLIN_TYPE, EnumKaolinType.ROCK));
        this.setCreativeTab(ExPCreativeTabs.tabUnderground);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, KAOLIN_TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(KAOLIN_TYPE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta).withProperty(KAOLIN_TYPE, EnumKaolinType.values()[meta]);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @Override
    public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
    {
        return 15;
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
        if (world instanceof World && world.getBlockState(pos).getValue(KAOLIN_TYPE) == EnumKaolinType.ROCK)
        {
            World w = (World) world;
            if (this.canFall(w, world.getBlockState(pos), pos, neighbor) && w.rand.nextBoolean())
            {
                GravityHelper.doFall(world.getBlockState(pos), w, pos, neighbor);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.onNeighborChange(worldIn, pos, pos);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, state.getValue(KAOLIN_TYPE).ordinal());
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return this.getUnlocalizedName() + "." + EnumKaolinType.values()[is.getMetadata()].getName();
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return item.getMetadata() == 2 ? 0.6F : 0.4F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        EnumKaolinType type = state.getValue(KAOLIN_TYPE);
        return type == EnumKaolinType.ROCK ? ExPItems.generic : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        EnumKaolinType type = state.getValue(KAOLIN_TYPE);
        return type == EnumKaolinType.ROCK ? ItemGeneric.EnumGenericType.KAOLIN.ordinal() : type.ordinal();
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        EnumKaolinType type = state.getValue(KAOLIN_TYPE);
        return type == EnumKaolinType.ROCK ? 1 + random.nextInt(4) : super.quantityDropped(state, fortune, random);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (EnumKaolinType type : EnumKaolinType.values())
        {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }


}

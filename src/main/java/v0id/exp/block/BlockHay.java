package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

public class BlockHay extends Block implements IInitializableBlock, IGravitySusceptible, IItemBlockProvider, IWeightProvider
{
    public BlockHay()
    {
        super(Material.ROCK);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(0.2F);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockHay));
        this.setResistance(1);
        this.setSoundType(SoundType.PLANT);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @Override
    public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
    {
        return 5;
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
    public float provideWeight(ItemStack item)
    {
        return 1F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

}

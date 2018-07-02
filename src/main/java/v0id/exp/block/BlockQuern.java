package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemGrindstone;
import v0id.exp.tile.TileQuern;

import javax.annotation.Nullable;

public class BlockQuern extends Block implements IWeightProvider, IInitializableBlock, IItemBlockProvider
{
    public BlockQuern()
    {
        super(Material.ROCK);
        this.initBlock();
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 6F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

    @Override
    public void initBlock()
    {
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setRegistryName(ExPRegistryNames.blockQuern);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        TileQuern tile = (TileQuern) worldIn.getTileEntity(pos);
        if (hitY >= 0.5F && tile.inventory.getStackInSlot(0).getItem() instanceof ItemGrindstone && !tile.inventory.getStackInSlot(0).isEmpty())
        {
            if (tile.rotationIndex == 0)
            {
                tile.rotationIndex = 90;
                tile.inventory.getStackInSlot(0).setItemDamage(tile.inventory.getStackInSlot(0).getItemDamage() + 1);
                if (tile.inventory.getStackInSlot(0).getItemDamage() >= tile.inventory.getStackInSlot(0).getMaxDamage())
                {
                    tile.inventory.setStackInSlot(0, ItemStack.EMPTY);
                }

                tile.sendUpdatePacket();
            }

            return true;
        }

        playerIn.openGui(ExPetrum.instance, 6, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileQuern();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, 0.5F, 1);
    }
}

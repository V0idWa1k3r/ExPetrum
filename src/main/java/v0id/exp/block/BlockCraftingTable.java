package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.item.ItemBlockWithMetadata;

import java.util.Arrays;

import static v0id.api.exp.data.ExPBlockProperties.TREE_TYPE;

public class BlockCraftingTable extends BlockWorkbench implements IWeightProvider, IOreDictEntry, IInitializableBlock, IItemBlockProvider
{
    public int logIndex;

    public BlockCraftingTable(int i)
    {
        super();
        this.logIndex = i;
        this.initBlock();
    }

    @Override
    public boolean isAssociatedBlock(Block other)
    {
        return super.isAssociatedBlock(other) || other instanceof BlockWorkbench || other instanceof BlockCraftingTable;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(ExPOreDict.blockCraftingTable).forEach(name ->
        {
            for (int i = 0; i < 15; ++i)
            {
                EnumTreeType treeType = EnumTreeType.values()[this.logIndex * 15 + i];
                OreDictionary.registerOre(name, new ItemStack(this, 1, i));
                OreDictionary.registerOre(name + Character.toUpperCase(treeType.name().charAt(0)) + treeType.name().toLowerCase().substring(1), new ItemStack(this, 1, i));
            }
        });
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.8F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

    @Override
    public void initBlock()
    {
        this.setHardness(3);
        this.setRegistryName(createRegistryLocation());
        this.setResistance(6);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TREE_TYPE, EnumTreeType.values()[this.logIndex * 15]));
        Blocks.FIRE.setFireInfo(this, 5, 20);
    }

    public ResourceLocation createRegistryLocation()
    {
        return ExPRegistryNames.asLocation(ExPRegistryNames.blockCraftingTable + this.logIndex);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TREE_TYPE);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < 15; ++i)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TREE_TYPE, EnumTreeType.values()[meta + this.logIndex * 15]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int ordinal = state.getValue(TREE_TYPE).ordinal();
        if (ordinal * 15 < this.logIndex * 15 || ordinal * 15 >= this.logIndex * 15)
        {
            return 0;
        }

        return ordinal % 15;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        playerIn.openGui(ExPetrum.instance, -1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}

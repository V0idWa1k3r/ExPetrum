package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.IHasSpecialName;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

import java.util.Arrays;

import static v0id.api.exp.data.ExPBlockProperties.TREE_TYPE;

public class BlockPlanks extends Block implements IWeightProvider, IOreDictEntry, IItemBlockProvider, IHasSpecialName
{
    public int logIndex;

    public BlockPlanks(int i)
    {
        super(Material.WOOD);
        this.logIndex = i;
        this.setHardness(3);
        this.setRegistryName(createRegistryLocation());
        this.setResistance(6);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TREE_TYPE, EnumTreeType.values()[this.logIndex * 15]));
        Blocks.FIRE.setFireInfo(this, 5, 20);
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(ExPOreDict.blockPlanks).forEach(name ->
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
        return 0.2F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)1);
    }

    public ResourceLocation createRegistryLocation()
    {
        return ExPRegistryNames.asLocation(ExPRegistryNames.blockPlanks + this.logIndex);
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
        if (ordinal < this.logIndex * 15 || ordinal >= (this.logIndex + 1) * 15)
        {
            return 0;
        }

        return ordinal % 15;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TREE_TYPE).ordinal() % 15;
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return super.getUnlocalizedName() + "." + EnumTreeType.values()[this.logIndex * 15 + is.getMetadata()].name().toLowerCase();
    }
}

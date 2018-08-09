package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

public class BlockHay extends Block implements IItemBlockProvider, IWeightProvider
{
    public BlockHay()
    {
        super(Material.PLANTS);
        this.setHardness(0.2F);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockHay));
        this.setResistance(1);
        this.setSoundType(SoundType.PLANT);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        Blocks.FIRE.setFireInfo(this, 60, 100);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
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

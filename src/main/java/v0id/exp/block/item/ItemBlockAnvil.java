package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import v0id.api.exp.block.EnumAnvilMaterial;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.metal.EnumMetal;

public class ItemBlockAnvil extends ItemBlockWithMetadata implements IMeltableMetal
{
    public ItemBlockAnvil(Block block)
    {
        super(block);
    }

    @Override
    public EnumMetal getMetal(ItemStack is)
    {
        return EnumAnvilMaterial.values()[is.getMetadata()].getMetal();
    }

    @Override
    public float getMeltingTemperature(ItemStack is)
    {
        return EnumAnvilMaterial.values()[is.getMetadata()].getMetal().getMeltingTemperature();
    }

    @Override
    public int getMetalAmound(ItemStack is)
    {
        return 1400;
    }
}

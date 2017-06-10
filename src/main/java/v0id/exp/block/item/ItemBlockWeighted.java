package v0id.exp.block.item;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.player.inventory.PlayerInventoryHelper;

public class ItemBlockWeighted extends ItemBlock implements IWeightProvider
{
	public ItemBlockWeighted(Block block)
	{
		super(block);
		this.setRegistryName(block.getRegistryName());
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getBlock() instanceof IWeightProvider ? ((IWeightProvider)this.getBlock()).provideWeight(item) : 1;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return this.getBlock() instanceof IWeightProvider ? ((IWeightProvider)this.getBlock()).provideVolume(item) : PlayerInventoryHelper.defaultVolume;
	}
}

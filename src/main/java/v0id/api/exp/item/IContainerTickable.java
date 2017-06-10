package v0id.api.exp.item;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IContainerTickable
{
	void onContainerTick(ItemStack is, World w, BlockPos pos, TileEntity container);
}

package v0id.api.exp.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public interface IAcceptsWaterCan
{
	void acceptWatering(EntityPlayer player, World w, BlockPos pos, IBlockState state, IFluidHandlerItem wateringCanCapability, ItemStack wateringCanStack, int wateringCanTier); 
}

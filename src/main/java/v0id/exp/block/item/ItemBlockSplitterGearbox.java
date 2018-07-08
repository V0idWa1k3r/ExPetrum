package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.exp.tile.TileSplitterGearbox;

public class ItemBlockSplitterGearbox extends ItemBlockWithMetadata
{
    public ItemBlockSplitterGearbox(Block block)
    {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        TileSplitterGearbox tile = (TileSplitterGearbox) world.getTileEntity(pos);
        EnumFacing facing = world.getBlockState(pos).getValue(BlockDirectional.FACING);
        tile.output0 = facing.getAxis() == EnumFacing.Axis.Y ? EnumFacing.SOUTH : facing.rotateY();
        tile.output1 = facing.getAxis() == EnumFacing.Axis.Y ? EnumFacing.NORTH : facing.rotateYCCW();
        return ret;
    }
}

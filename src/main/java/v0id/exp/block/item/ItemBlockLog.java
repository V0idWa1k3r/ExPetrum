package v0id.exp.block.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.block.BlockLogPile;
import v0id.exp.tile.TileLogPile;

public class ItemBlockLog extends ItemBlockWithMetadata
{
    public ItemBlockLog(Block block)
    {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        IBlockState newCopy = newState;
        if (player.isSneaking())
        {
            newState = ExPBlocks.logPile.getDefaultState();
        }

        boolean ret = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        if (world.getBlockState(pos).getBlock() instanceof BlockLogPile)
        {
            setTileEntityNBT(world, player, pos, stack);
            TileLogPile tile = (TileLogPile) world.getTileEntity(pos);
            tile.type = newCopy.getValue(ExPBlockProperties.TREE_TYPE);
            EnumFacing facing = EnumFacing.fromAngle(player.rotationYaw);
            tile.rotated = facing.getAxis() == EnumFacing.Axis.X;
            tile.sendUpdatePacket();
            world.getBlockState(pos).getBlock().onBlockPlacedBy(world, pos, newState, player, stack);
            if (player instanceof EntityPlayerMP)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
            }
        }

        return ret;
    }
}

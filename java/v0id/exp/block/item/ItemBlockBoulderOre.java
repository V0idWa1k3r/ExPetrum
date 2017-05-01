package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.exp.tile.TileOre;

public class ItemBlockBoulderOre extends ItemBlockWithMetadata
{

	public ItemBlockBoulderOre(Block block)
	{
		super(block);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.block.getUnlocalizedName();
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if (!world.setBlockState(pos, newState, 11)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
            setTileEntityNBT(world, player, pos, stack);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileOre)
            {
            	int oreIndex = (stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length;
        		((TileOre)tile).type = EnumOre.values()[oreIndex];
        		((TileOre)tile).amount = 10;
            }
            
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
	}

	
}

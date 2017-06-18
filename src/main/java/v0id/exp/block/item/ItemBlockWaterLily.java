package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class ItemBlockWaterLily extends ItemBlockWithMetadata
{
	public ItemBlockWaterLily(Block block)
	{
		super(block);
	}

	@SuppressWarnings("deprecation")
    @Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
            {
                return new ActionResult<>(EnumActionResult.FAIL, itemstack);
            }

            BlockPos blockpos1 = blockpos.up();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            net.minecraftforge.common.util.BlockSnapshot blocksnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(worldIn, blockpos1);
            worldIn.setBlockState(blockpos1, this.block.getStateFromMeta(itemstack.getMetadata()), 3);
            if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos, EnumFacing.UP, (IPlantable) worldIn.getBlockState(blockpos1).getBlock()))
            {
                // special case for handling block placement with water lilies

                worldIn.setBlockState(blockpos1, this.block.getStateFromMeta(itemstack.getMetadata()));
                if (net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace(playerIn, blocksnapshot, EnumFacing.UP, handIn).isCanceled())
                {
                    blocksnapshot.restore(true, false);
                    return new ActionResult<>(EnumActionResult.FAIL, itemstack);
                }

                worldIn.setBlockState(blockpos1, this.block.getStateFromMeta(itemstack.getMetadata()));

                if (!playerIn.capabilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }

                //noinspection ConstantConditions
                playerIn.addStat(StatList.getObjectUseStats(this));
                worldIn.playSound(playerIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
            }
            else
            {
                blocksnapshot.restore(true, false);
            }
        }

        return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		return true;
	}
}

package v0id.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface IFunctionalBlockColor extends IBlockColor
{
	@Override
	int colorMultiplier(@Nonnull IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex);
	
	static void registerBlockColorHandler(IFunctionalBlockColor color, Block... blocks)
	{
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, blocks);
	}
}

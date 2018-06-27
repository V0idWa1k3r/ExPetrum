package v0id.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface IFunctionalStateMapper extends IStateMapper
{
	@Nonnull
	@Override
	Map<IBlockState, ModelResourceLocation> putStateModelLocations(@Nonnull Block blockIn);
	
	static void setCustomStateMapper(Block block, IFunctionalStateMapper mapper)
	{
		ModelLoader.setCustomStateMapper(block, mapper);
	}
}

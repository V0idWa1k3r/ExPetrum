package v0id.exp.client.render.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fluids.BlockFluidFinite;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.core.client.RenderUtils;
import v0id.core.client.model.WavefrontObject;
import v0id.exp.tile.TileWaterWheel;

public class TESRWaterWheel extends FastTESR<TileWaterWheel>
{
    public static WavefrontObject model;

    @Override
    public void renderTileEntityFast(TileWaterWheel te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockHorizontal.FACING);
        Vector3f pos = new Vector3f((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
        transform = transform.scale(new Vector3f(0.5F, 0.5F, 0.5F));
        int waterLevels = 0;
        BlockPos at = te.getPos().down(3);
        for (int i = -1; i <= 1; ++i)
        {
            BlockPos offset = at.offset(facing.getAxis() == EnumFacing.Axis.X ? EnumFacing.NORTH : EnumFacing.EAST, i);
            IBlockState state = te.getWorld().getBlockState(offset);
            if (state.getBlock() instanceof BlockFluidFinite)
            {
                waterLevels += state.getValue(BlockFluidFinite.LEVEL);
            }
        }

        float f = (float)Math.toRadians(te.getWorld().getWorldTime() % 360 * waterLevels / 10F + partialTicks * waterLevels / 10F);
        if (facing.getAxis() == EnumFacing.Axis.Z)
        {
            transform = transform.rotate((float)Math.toRadians(90), new Vector3f(0, 1, 0));
        }

        transform = transform.rotate(f, new Vector3f(1, 0, 0));
        RenderUtils.renderObj(buffer, model, pos, transform, new float[]{ 1, 1, 1, 1 }, new int[]{ 240, 0 }, () -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exp:blocks/wood/plank/generic"));
    }
}

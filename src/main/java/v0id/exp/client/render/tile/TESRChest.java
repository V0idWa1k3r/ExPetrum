package v0id.exp.client.render.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.tile.TileChest;

public class TESRChest extends TileEntitySpecialRenderer<TileChest>
{
    private final ModelChest simpleChest = new ModelChest();

    @Override
    public void render(TileChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.getWorld().getBlockState(te.getPos()).getBlock() == ExPBlocks.chest)
        {
            super.render(te, x, y, z, partialTicks, destroyStage, alpha);
            this.bindTexture(new ResourceLocation("exp", "textures/tiles/chest/" + te.type.getName() + ".png"));
            EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockHorizontal.FACING);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + (facing == EnumFacing.EAST || facing == EnumFacing.NORTH ? 1 : 0), y + 1, z + (facing == EnumFacing.EAST || facing == EnumFacing.SOUTH ? 1 : 0));
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1, 0);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            simpleChest.chestLid.rotateAngleX = -(f * ((float) Math.PI / 2F));
            simpleChest.renderAll();
            GlStateManager.popMatrix();
        }
    }
}

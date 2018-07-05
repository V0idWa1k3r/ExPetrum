package v0id.exp.client.render.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.api.exp.world.IExPWorld;
import v0id.core.client.RenderUtils;
import v0id.exp.tile.TileBellows;

public class TESRBellows extends FastTESR<TileBellows>
{
    @Override
    public void renderTileEntityFast(TileBellows te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().registerSprite(new ResourceLocation("exp", "blocks/bellows"));
        TextureAtlasSprite planks = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().registerSprite(new ResourceLocation("exp", "blocks/wood/plank/generic"));
        EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockHorizontal.FACING);
        int i = te.getWorld().getCombinedLight(te.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        long current = IExPWorld.of(te.getWorld()).today().getTime();
        long last = te.bellowsLast;
        long diff = current - last;
        float a = diff <= 100 ? Math.abs((float)Math.sin(Math.toRadians(diff * 1.8F))) : 0;
        Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
        Vector3f pos = new Vector3f((float)x, (float)y, (float)z);
        if (facing == EnumFacing.NORTH)
        {
            pos = new Vector3f((float)x + 0.5F, (float)y + 0.2F, (float)z + 0.1F);
            transform.rotate((float)Math.toRadians(-45 + a * 45), new Vector3f(1, 0, 0));
            transform.translate(new Vector3f(0, 0, 0.5F));
            transform = transform.scale(new Vector3f(0.625F, 0.0625F, 0.8125F));
            RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, e -> planks);
            buffer.pos(x + 0.2F, y + 0.8F - a * 0.65F, z + 0.7F + a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.0625F, z + 0.9F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.0625F, z + 0.9F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.8F - a * 0.65F, z + 0.7F + a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.2F, z + 0.15F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.0625F, z + 0.15F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.0625F, z + 0.9F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.8F - a * 0.65F,  z + 0.7F + a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.2F, z + 0.15F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.0625F, z + 0.15F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.0625F, z + 0.9F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.8F - a * 0.65F,  z + 0.7F + a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
        }

        if (facing == EnumFacing.SOUTH)
        {
            pos = new Vector3f((float)x + 0.5F, (float)y + 0.2F, (float)z + 0.9F);
            transform.rotate((float)Math.toRadians(180), new Vector3f(0, 1, 0));
            transform.rotate((float)Math.toRadians(-45 + a * 45), new Vector3f(1, 0, 0));
            transform.translate(new Vector3f(0, 0, 0.5F));
            transform = transform.scale(new Vector3f(0.625F, 0.0625F, 0.8125F));
            RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, e -> planks);
            buffer.pos(x + 0.2F, y + 0.8F - a * 0.65F, z + 0.3F - a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.0625F, z + 0.1F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.0625F, z + 0.1F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.8F - a * 0.65F, z + 0.3F - a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.2F, z + 0.85F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.0625F, z + 0.85F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.0625F, z + 0.1F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.2F, y + 0.8F - a * 0.65F,  z + 0.3 - a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.2F, z + 0.85F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.0625F, z + 0.85F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.0625F, z + 0.1F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.8F, y + 0.8F - a * 0.65F,  z + 0.3 - a * 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
        }

        if (facing == EnumFacing.WEST)
        {
            pos = new Vector3f((float)x + 0.1F, (float)y + 0.2F, (float)z + 0.5F);
            transform.rotate((float)Math.toRadians(90), new Vector3f(0, 1, 0));
            transform.rotate((float)Math.toRadians(-45 + a * 45), new Vector3f(1, 0, 0));
            transform.translate(new Vector3f(0, 0, 0.5F));
            transform = transform.scale(new Vector3f(0.625F, 0.0625F, 0.8125F));
            RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, e -> planks);
            buffer.pos(x + 0.7F + a * 0.2F, y + 0.8F - a * 0.65F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.9F, y + 0.0625F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.9F, y + 0.0625F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.7F + a * 0.2F, y + 0.8F - a * 0.65F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.15F, y + 0.2F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.15F, y + 0.0625F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.9F, y + 0.0625F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.7F + a * 0.2F, y + 0.8F - a * 0.65F,  z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.15F, y + 0.2F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.15F, y + 0.0625F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.9F, y + 0.0625F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.7F + a * 0.2F, y + 0.8F - a * 0.65F,  z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
        }

        if (facing == EnumFacing.EAST)
        {
            pos = new Vector3f((float)x + 0.9F, (float)y + 0.2F, (float)z + 0.5F);
            transform.rotate((float)Math.toRadians(270), new Vector3f(0, 1, 0));
            transform.rotate((float)Math.toRadians(-45 + a * 45), new Vector3f(1, 0, 0));
            transform.translate(new Vector3f(0, 0, 0.5F));
            transform = transform.scale(new Vector3f(0.625F, 0.0625F, 0.8125F));
            RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, e -> planks);
            buffer.pos(x + 0.3F - a * 0.2F, y + 0.8F - a * 0.65F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.1F, y + 0.0625F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.1F, y + 0.0625F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.3F - a * 0.2F, y + 0.8F - a * 0.65F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.85F, y + 0.2F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.85F, y + 0.0625F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.1F, y + 0.0625F, z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.3 - a * 0.2F, y + 0.8F - a * 0.65F,  z + 0.2F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.85F, y + 0.2F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.85F, y + 0.0625F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.1F, y + 0.0625F, z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
            buffer.pos(x + 0.3 - a * 0.2F, y + 0.8F - a * 0.65F,  z + 0.8F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
        }
    }
}

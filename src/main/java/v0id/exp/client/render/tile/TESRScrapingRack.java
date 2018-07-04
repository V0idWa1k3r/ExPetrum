package v0id.exp.client.render.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import v0id.exp.item.ItemGeneric;
import v0id.exp.tile.TileScrapingRack;

public class TESRScrapingRack extends FastTESR<TileScrapingRack>
{
    @Override
    public void renderTileEntityFast(TileScrapingRack te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (!te.inventory.getStackInSlot(0).isEmpty())
        {
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().registerSprite(new ResourceLocation("exp", "items/misc/prepared_hide"));
            TextureAtlasSprite sprite2 = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().registerSprite(new ResourceLocation("exp", "items/misc/soaked_hide"));
            EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockHorizontal.FACING);
            float a = te.inventory.getStackInSlot(0).getMetadata() == ItemGeneric.EnumGenericType.PREPARED_HIDE.ordinal() ? 0.0F : Math.max(0, 1.0F - te.progress);
            int i = te.getWorld().getCombinedLight(te.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;

            if (facing == EnumFacing.SOUTH)
            {
                buffer.pos(x, y + 1, z + 0.1F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y, z + 0.5F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y, z + 0.5F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y + 1, z + 0.1F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y + 1, z + 0.101F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y, z + 0.501F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y, z + 0.501F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y + 1, z + 0.101F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y + 1, z + 0.099F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y, z + 0.499F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y, z + 0.499F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y + 1, z + 0.099F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
            }

            if (facing == EnumFacing.NORTH)
            {
                buffer.pos(x, y + 1, z + 0.9F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y, z + 0.5F).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y, z + 0.5F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y + 1, z + 0.9F).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y + 1, z + 0.901F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y, z + 0.501F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y, z + 0.501F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y + 1, z + 0.901F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y + 1, z + 0.899F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x, y, z + 0.499F).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y, z + 0.499F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 1, y + 1, z + 0.899F).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
            }

            if (facing == EnumFacing.EAST)
            {
                buffer.pos(x + 0.1F, y + 1, z).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.5F, y, z).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.5F, y, z + 1).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.1F, y + 1, z + 1).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.101F, y + 1, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.501F, y, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.501F, y, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.101F, y + 1, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.099F, y + 1, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.499F, y, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.499F, y, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.099F, y + 1, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
            }

            if (facing == EnumFacing.WEST)
            {
                buffer.pos(x + 0.9F, y + 1, z).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.5F, y, z).color(1, 1, 1, 1F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.5F, y, z + 1).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.9F, y + 1, z + 1).color(1, 1, 1, 1F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.901F, y + 1, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.501F, y, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.501F, y, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.901F, y + 1, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.899F, y + 1, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMinV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.499F, y, z).color(1, 1, 1, a).tex(sprite2.getMinU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.499F, y, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMaxV()).lightmap(k, j).endVertex();
                buffer.pos(x + 0.899F, y + 1, z + 1).color(1, 1, 1, a).tex(sprite2.getMaxU(), sprite2.getMinV()).lightmap(k, j).endVertex();
            }
        }
    }
}

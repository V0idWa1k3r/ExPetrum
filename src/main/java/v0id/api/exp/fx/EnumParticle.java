package v0id.api.exp.fx;

import net.minecraft.util.BlockRenderLayer;
import v0id.core.util.java.Rectangle;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public enum EnumParticle
{
    LEAF(new Rectangle(0, 0, 16, 16), BlockRenderLayer.CUTOUT);

    EnumParticle(Rectangle renderRectangle, BlockRenderLayer layer)
    {
        this.renderRectangle = renderRectangle;
        this.layer = layer;
    }

    public Rectangle getRenderRectangle()
    {
        return renderRectangle;
    }

    public BlockRenderLayer getLayer()
    {
        return layer;
    }

    private final Rectangle renderRectangle;
    private final BlockRenderLayer layer;
}

package v0id.exp.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

// I know this is hacky and wastes a tiny fraction of cpu cycles.
// However this is the only non-ugly and non-copy-1000-lines-of-code-from-forge solution.
// Forge's obj models can't be colored with IItemColors.
// And I do not want to make a billion textures for my models, duh
// So I have to create workarounds.
// 'But if this is a bug in forge why would you not submit it to their github?'
// Oh, they are aware - https://github.com/MinecraftForge/MinecraftForge/issues/2851
// Just not fixing it. 
// I mean, more than a year has passed...
// #BlameForge :D I'm kidding, obviously
public class ModelIngotBaked implements IBakedModel
{
	public final OBJBakedModel model;
	private ImmutableList<BakedQuad> hackedQuads;

    public ModelIngotBaked(OBJBakedModel mdl)
	{
		this.model = mdl;
	}

	@Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
    {
        if (this.hackedQuads == null)
        {
            List<BakedQuad> wrappedQuads = this.model.getQuads(blockState, side, rand);
            if (!wrappedQuads.isEmpty())
            {
                List<BakedQuad> ret = new ArrayList<>(wrappedQuads.size());
                wrappedQuads.forEach(q -> ret.add(q instanceof UnpackedBakedQuad ? new WrappedQuad((UnpackedBakedQuad) q) : q));
                hackedQuads = ImmutableList.copyOf(ret);
            }
            else
            {
                return wrappedQuads;
            }
        }
		
		return this.hackedQuads;
    }

	@Override
	public boolean isAmbientOcclusion()
	{
		return this.model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return this.model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return this.model.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return this.model.getParticleTexture();
	}

	@Deprecated
	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return this.model.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return this.model.getOverrides();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return PerspectiveMapWrapper.handlePerspective(this, this.model.getState(), cameraTransformType);
	}
	
	public static class WrappedQuad extends BakedQuad
	{
		public WrappedQuad(UnpackedBakedQuad parent)
		{
			super(parent.getVertexData(), 0, parent.getFace(), parent.getSprite(), parent.shouldApplyDiffuseLighting(), parent.getFormat());
		}
	}
}

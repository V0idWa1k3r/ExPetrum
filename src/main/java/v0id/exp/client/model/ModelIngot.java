package v0id.exp.client.model;

import java.util.Collection;

import com.google.common.base.Function;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.common.model.IModelState;

public class ModelIngot implements IModel
{
	public final OBJModel model;
	
	public ModelIngot(OBJModel model)
	{
		this.model = model;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return this.model.getDependencies();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return this.model.getTextures();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		return new ModelIngotBaked((OBJBakedModel) this.model.bake(state, format, bakedTextureGetter));
	}

	@Override
	public IModelState getDefaultState()
	{
		return this.model.getDefaultState();
	}

}

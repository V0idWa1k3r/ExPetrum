package v0id.exp.client.model;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.common.model.IModelState;
import v0id.api.exp.block.property.EnumDirtClass;

public class ModelCattail implements IModelCustomData
{
	public static final List<ResourceLocation> dependencies = Lists.newArrayList();
	public static final List<ResourceLocation> textures = Lists.newArrayList();
	
	static
	{
		dependencies.add(new ResourceLocation("minecraft", "block/cube_all"));
		dependencies.add(new ResourceLocation("minecraft", "block/cross"));
		for (int i = 0; i < 16; ++i)
		{
			textures.add(new ResourceLocation("exp", "blocks/dirt/" + EnumDirtClass.values()[i].getName()));
		}
		
		textures.add(new ResourceLocation("exp", "blocks/plants/wildmisc/cattail"));
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableSet.copyOf(this.dependencies);
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableSet.copyOf(this.textures);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,	Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		return ModelCattailBaked.bake(state, format, bakedTextureGetter);
	}

	@Override
	public IModelState getDefaultState()
	{
		return null;
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData)
	{
		return null;
	}

}

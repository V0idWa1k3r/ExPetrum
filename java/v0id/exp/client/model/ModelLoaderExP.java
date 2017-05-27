package v0id.exp.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelLoaderExP implements ICustomModelLoader
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		ModelCattailBaked.bakedQuads.clear();
		ModelCattailBaked.soilTextures = null;
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return "exp".equals(modelLocation.getResourceDomain()) && (modelLocation.getResourcePath().contains("cattail") || modelLocation.getResourcePath().contains("ingot.objexp"));
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return modelLocation.getResourcePath().contains("cattail") ? new ModelCattail() : loadIngotModel(modelLocation);
	}
	
	public IModel loadIngotModel(ResourceLocation modelLocation) throws Exception
	{
		return new ModelIngot((OBJModel) OBJLoader.INSTANCE.loadModel(modelLocation));
	}

}

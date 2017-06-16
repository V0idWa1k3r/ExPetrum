package v0id.exp.client.model;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.model.IModelState;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.data.ExPBlockProperties;

public class ModelCattailBaked implements IBakedModel
{
	public static TextureAtlasSprite[] soilTextures;
	public static TextureAtlasSprite cattailTexture;
	final static int norm_top = 32512;
	final static int norm_down = 33204;
	final static int norm_north = 8454144;
	final static int norm_south = 8323072;
	final static int norm_west = 129;
	final static int norm_east = 127;
	public static List<List<List<BakedQuad>>> bakedQuads = Lists.newArrayList();
	public static final ModelCattailBaked INSTANCE = new ModelCattailBaked();
	
	public static IBakedModel bake(IModelState state, VertexFormat format,	Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		if (soilTextures == null)
		{
			bakeTextures(bakedTextureGetter);
			bakeModels();
		}
		
		return INSTANCE;
	}

	private static void bakeModels()
	{
		for (int i = 0; i < EnumDirtClass.values().length; ++i)
		{
			List<List<BakedQuad>> dataMap = Lists.newArrayList();
			for (int j = 0; j < 7; ++j)
			{
				dataMap.add(Lists.newArrayList());
			}
			
			populateCube(i, dataMap);
			populateCross(dataMap);
			bakedQuads.add(dataMap);
		}
	}

	private static void populateCross(List<List<BakedQuad>> dataMap)
	{		
		int[] dataCross0 = new int[]{
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(3), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(3), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0,
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0
		};
		
		int[] dataCross1 = new int[]{
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(3), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(3), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0,
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0
		};
		
		int[] dataCross2 = new int[]{
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(3), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(3), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0,
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0
		};
		
		int[] dataCross3 = new int[]{
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(3), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(3), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMinV()), 0,
			Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(cattailTexture.getMaxU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0,
			Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(cattailTexture.getMinU()), Float.floatToRawIntBits(cattailTexture.getMaxV()), 0
		};
		
		ForgeHooksClient.fillNormal(dataCross0, null);
		ForgeHooksClient.fillNormal(dataCross1, null);
		ForgeHooksClient.fillNormal(dataCross2, null);
		ForgeHooksClient.fillNormal(dataCross3, null);
		BakedQuad faceCross0 = new BakedQuad(dataCross0, -1, EnumFacing.UP, cattailTexture, true, DefaultVertexFormats.ITEM);
		BakedQuad faceCross1 = new BakedQuad(dataCross1, -1, EnumFacing.UP, cattailTexture, true, DefaultVertexFormats.ITEM);
		BakedQuad faceCross2 = new BakedQuad(dataCross2, -1, EnumFacing.UP, cattailTexture, true, DefaultVertexFormats.ITEM);
		BakedQuad faceCross3 = new BakedQuad(dataCross3, -1, EnumFacing.UP, cattailTexture, true, DefaultVertexFormats.ITEM);
		List<BakedQuad> quads = Lists.newArrayList();
		quads.add(faceCross0);
		quads.add(faceCross1);
		quads.add(faceCross2);
		quads.add(faceCross3);
		dataMap.get(0).addAll(quads);
	}

	private static void populateCube(int i, List<List<BakedQuad>> dataMap)
	{
		TextureAtlasSprite textureGround = soilTextures[i];
		int[] dataUp = new int[]{
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_top,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_top,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_top,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_top
		};
		
		int[] dataDown = new int[]{
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(0), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_down,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(0), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_down,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(0), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_down,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(0), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_down
		};
		
		int[] dataNorth = new int[]{
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_north,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_north,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(0), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_north,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(0), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_north
		};
		
		int[] dataSouth = new int[]{
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_south,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_south,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(0), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_south,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(0), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_south
		};
		
		int[] dataWest = new int[]{
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_west,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_west,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(0), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_west,
				Float.floatToRawIntBits(0), Float.floatToRawIntBits(0), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_west
		};
		
		int[] dataEast = new int[]{
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_east,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(1), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMinV()),	norm_east,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(0), Float.floatToRawIntBits(1),	-1,	Float.floatToRawIntBits(textureGround.getMaxU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_east,
				Float.floatToRawIntBits(1), Float.floatToRawIntBits(0), Float.floatToRawIntBits(0),	-1,	Float.floatToRawIntBits(textureGround.getMinU()), Float.floatToRawIntBits(textureGround.getMaxV()),	norm_east
		};
		
		BakedQuad faceUp = new BakedQuad(dataUp, -1, EnumFacing.UP, textureGround, true, DefaultVertexFormats.ITEM);
		BakedQuad faceDown = new BakedQuad(dataDown, -1, EnumFacing.DOWN, textureGround, true, DefaultVertexFormats.ITEM);
		BakedQuad faceNorth = new BakedQuad(dataNorth, -1, EnumFacing.NORTH, textureGround, true, DefaultVertexFormats.ITEM);
		BakedQuad faceSouth = new BakedQuad(dataSouth, -1, EnumFacing.SOUTH, textureGround, true, DefaultVertexFormats.ITEM);
		BakedQuad faceWest = new BakedQuad(dataWest, -1, EnumFacing.WEST, textureGround, true, DefaultVertexFormats.ITEM);
		BakedQuad faceEast = new BakedQuad(dataEast, -1, EnumFacing.EAST, textureGround, true, DefaultVertexFormats.ITEM);
		List<BakedQuad> lstUp = Lists.newArrayList();
		List<BakedQuad> lstDown = Lists.newArrayList();
		List<BakedQuad> lstNorth = Lists.newArrayList();
		List<BakedQuad> lstSouth = Lists.newArrayList();
		List<BakedQuad> lstWest = Lists.newArrayList();
		List<BakedQuad> lstEast = Lists.newArrayList();
		lstUp.add(faceUp);
		lstDown.add(faceDown);
		lstNorth.add(faceNorth);
		lstSouth.add(faceSouth);
		lstWest.add(faceWest);
		lstEast.add(faceEast);
		dataMap.get(EnumFacing.UP.ordinal() + 1).addAll(lstUp);
		dataMap.get(EnumFacing.DOWN.ordinal() + 1).addAll(lstDown);
		dataMap.get(EnumFacing.NORTH.ordinal() + 1).addAll(lstNorth);
		dataMap.get(EnumFacing.SOUTH.ordinal() + 1).addAll(lstSouth);
		dataMap.get(EnumFacing.WEST.ordinal() + 1).addAll(lstWest);
		dataMap.get(EnumFacing.EAST.ordinal() + 1).addAll(lstEast);
	}

	private static void bakeTextures(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		soilTextures = new TextureAtlasSprite[16];
		for (int i = 0; i < EnumDirtClass.values().length; ++i)
		{
			soilTextures[i] = bakedTextureGetter.apply(ModelCattail.textures.get(i));
		}
		
		cattailTexture = bakedTextureGetter.apply(ModelCattail.textures.get(16));
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		int meta = state == null ? 0 : state.getValue(ExPBlockProperties.DIRT_CLASS).ordinal();
		return bakedQuads.get(meta).get(side == null ? 0 : side.ordinal() + 1);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.BEDROCK.getDefaultState()).getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}

}

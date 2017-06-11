package v0id.exp.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.ArrayUtils;
import v0id.api.core.util.IFunctionalRenderFactory;
import v0id.api.core.util.java.IInstanceProvider;
import v0id.api.core.util.java.Instance;
import v0id.api.exp.block.*;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.block.property.EnumWaterLilyType;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.combat.condition.ExecuteConditionKeyBindings;
import v0id.api.exp.data.*;
import v0id.api.exp.item.EnumToolhead;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.exp.block.tree.BlockLeaf;
import v0id.exp.block.tree.BlockLog;
import v0id.exp.client.model.ModelLoaderExP;
import v0id.exp.client.render.entity.RenderFallingTree;
import v0id.exp.client.render.entity.RenderThrownWeapon;
import v0id.exp.crop.ExPFarmland;
import v0id.exp.entity.EntityFallingTree;
import v0id.exp.entity.EntityGravFallingBlock;
import v0id.exp.entity.EntityThrownWeapon;
import v0id.exp.registry.ILifecycleListener;
import v0id.exp.tile.TileFarmland;
import v0id.exp.tile.TileOre;
import v0id.exp.util.Helpers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClientRegistry implements IInstanceProvider, ILifecycleListener
{
	@Instance
	public static ClientRegistry instance;
	
	public ClientRegistry()
	{

	}
	
	public void initAttacksConditions()
	{
		ExPWeaponAttacks.piercingDash.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindForward));
		ExPWeaponAttacks.slash.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack));
		ExPWeaponAttacks.downStrike.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindForward));
		ExPWeaponAttacks.spin.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight));
		ExPWeaponAttacks.shieldSlam.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindForward));
		ExPWeaponAttacks.behead.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindRight));
		ExPWeaponAttacks.stab.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack));
		ExPWeaponAttacks.itemThrow.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindForward));
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		OBJLoader.INSTANCE.addDomain("exp");
		for (int i = 0; i < 16; ++i)
		{
			// Lambda capture
			Integer iWrapper = i;
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.rock), i, new ModelResourceLocation(ExPBlocks.rock.getRegistryName(), "inventory-" + i));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.soil), i, new ModelResourceLocation(ExPBlocks.soil.getRegistryName(), "inventory-" + i));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.grass), i, new ModelResourceLocation(ExPBlocks.grass.getRegistryName(), "inventory-" + i));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.grass_dry), i, new ModelResourceLocation(ExPBlocks.grass_dry.getRegistryName(), "inventory-" + i));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.grass_dead), i, new ModelResourceLocation(ExPBlocks.grass_dead.getRegistryName(), "inventory-" + i));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.cattail), i, new ModelResourceLocation(ExPBlocks.cattail.getRegistryName(), "class=" + EnumDirtClass.values()[i].getName()));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.sand), i, new ModelResourceLocation(ExPBlocks.sand.getRegistryName(), "class=" + EnumRockClass.values()[i].getName()));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.seaweed), i, new ModelResourceLocation(ExPBlocks.seaweed.getRegistryName(), "class=" + EnumRockClass.values()[i].getName()));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.farmland), i, new ModelResourceLocation(ExPBlocks.farmland.getRegistryName(), "class=" + EnumDirtClass.values()[i].getName()));
			Stream.of(ExPBlocks.shrubs).forEach(s -> ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(s), iWrapper, new ModelResourceLocation(s.getRegistryName(), "istall=false,type=" + EnumShrubType.values()[iWrapper])));
			if (i < 10)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.waterLily), i, new ModelResourceLocation(ExPBlocks.waterLily.getRegistryName(), "blooming=" + (i >= 5) + ",type=" + EnumWaterLilyType.values()[i % 5].getName()));
			}
			
			if (i < 4)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.vegetation), i, new ModelResourceLocation(ExPBlocks.vegetation.getRegistryName(), "inventory-" + i));
			}
			
			if (i < 2)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.ice), i, new ModelResourceLocation(ExPBlocks.ice.getRegistryName(), "salt=" + (i == 1)));
			}

			if (i < EnumBerry.values().length)
            {
                for (int j = 0; j < 4; j++)
                {
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.berryBushes[j]), i, new ModelResourceLocation(ExPBlocks.berryBushes[j].getRegistryName(), "istall=false,type=" + (EnumBerry.values()[i].getName())));
                }
            }
			
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.pebble), i, new ModelResourceLocation(ExPBlocks.pebble.getRegistryName(), "amdl=0,class=" + EnumRockClass.values()[i].getName()));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.boulder), i, new ModelResourceLocation(ExPBlocks.boulder.getRegistryName(), "amdl=0,class=" + EnumRockClass.values()[i].getName()));
			ModelLoader.setCustomModelResourceLocation(ExPItems.rock, i, new ModelResourceLocation(ExPItems.rock.getRegistryName(), "class=" + EnumRockClass.values()[i].getName()));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.workedBoulder), i, new ModelResourceLocation(ExPBlocks.workedBoulder.getRegistryName(), "class=" + EnumRockClass.values()[i].getName() + ",workindex=0"));
		}
		
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.coralRock), 0, new ModelResourceLocation(ExPBlocks.coralRock.getRegistryName(), "rtindex=0"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.coralPlant), 0, new ModelResourceLocation(ExPBlocks.coralPlant.getRegistryName(), "ptindex=0,rtindex=0"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.snow), 0, new ModelResourceLocation(ExPBlocks.snow.getRegistryName(), "layers=1"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.crop), 0, new ModelResourceLocation("exp:crops/dead", "normal"));
		Arrays.asList(ExPBlocks.logs).forEach(this::registerLogItemModel);
		Arrays.asList(ExPBlocks.logsDeco).forEach(this::registerLogItemModel);
		Arrays.asList(ExPBlocks.leaves).forEach(this::registerLeafItemModel);
		for (int i = 0; i < EnumRockClass.values().length * EnumOre.values().length * 3; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.ore), i, new ModelResourceLocation(ExPBlocks.ore.getRegistryName(), "class=" + EnumRockClass.values()[i % EnumRockClass.values().length].getName() + ",oretexture=" + EnumOre.values()[(i / EnumRockClass.values().length) % EnumOre.values().length].getTextureIndex()));
			if (i < EnumRockClass.values().length * EnumOre.values().length)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.boulderOre), i, new ModelResourceLocation(ExPBlocks.boulderOre.getRegistryName(), "class=" + EnumRockClass.values()[i % EnumRockClass.values().length].getName() + ",oretexture=" + EnumOre.values()[(i / EnumRockClass.values().length) % EnumOre.values().length].getTextureIndex()));
			}
		}
		
		for (int i = 0; i < EnumTreeType.values().length + EnumShrubType.values().length + EnumBerry.values().length; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(ExPItems.stick, i, new ModelResourceLocation(ExPItems.stick.getRegistryName(), "type=" + ExPOreDict.stickNames[i]));
		}
		
		for (int i = 0; i < EnumToolhead.values().length; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(ExPItems.toolHead, i, new ModelResourceLocation(ExPItems.toolHead.getRegistryName(), "type=" + EnumToolhead.values()[i].getName()));
		}
		
		for (int i = 0; i < EnumCrop.values().length - 1; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(ExPItems.seeds, i, new ModelResourceLocation(ExPItems.seeds.getRegistryName(), "crop=" + EnumCrop.values()[i + 1].getName()));
		}
		
		for (int i = 0; i < FoodEntry.allEntries.size(); ++i)
		{
			ModelLoader.setCustomModelResourceLocation(ExPItems.food, i, new ModelResourceLocation(ExPItems.food.getRegistryName(), "type=" + FoodEntry.allEntries.get(i).getUnlocalizedName()));
		}
		
		for (int i = 0; i < EnumMetal.values().length; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(ExPItems.ingot, i, new ModelResourceLocation(ExPItems.ingot.getRegistryName(), "inventory"));
		}
		
		List<Item> toolsList = Arrays.asList(ExPItems.knife, ExPItems.pickaxe, ExPItems.axe, ExPItems.shovel, ExPItems.hoe, ExPItems.sword, ExPItems.scythe, ExPItems.battleaxe, ExPItems.hammer, ExPItems.spear, ExPItems.watering_can, ExPItems.gardening_spade);
		toolsList.forEach(tool -> ModelLoader.setCustomMeshDefinition(tool, stack -> new ModelResourceLocation(new ResourceLocation(tool.getRegistryName().getResourceDomain(), "tools/" + tool.getRegistryName().getResourcePath()), "material=" + EnumToolStats.values()[stack.getMetadata()].getName())));
		for (int i = 0; i < EnumToolStats.values().length; ++i)
		{
			Integer lambdaCaptureInt = i;
			toolsList.forEach(tool -> ModelLoader.registerItemVariants(tool, new ModelResourceLocation(new ResourceLocation(tool.getRegistryName().getResourceDomain(), "tools/" + tool.getRegistryName().getResourcePath()), "material=" + EnumToolStats.values()[lambdaCaptureInt].getName())));
		}
		
		this.registerCustomStateMappers();
		IFunctionalRenderFactory.registerEntityRenderingHandler(EntityGravFallingBlock.class, RenderFallingBlock::new);
		IFunctionalRenderFactory.registerEntityRenderingHandler(EntityFallingTree.class, RenderFallingTree::new);
		IFunctionalRenderFactory.registerEntityRenderingHandler(EntityThrownWeapon.class, RenderThrownWeapon::new);
		//ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(ExPBlocks.saltWater), item -> new ModelResourceLocation(ExPBlocks.saltWater.getRegistryName(), "fluid"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.saltWater), 0, new ModelResourceLocation(ExPBlocks.saltWater.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.freshWater), 0, new ModelResourceLocation(ExPBlocks.freshWater.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.lava), 0, new ModelResourceLocation(ExPBlocks.lava.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.oil), 0, new ModelResourceLocation(ExPBlocks.oil.getRegistryName(), "inventory"));
		ModelLoader.setCustomStateMapper(ExPBlocks.saltWater, new StateMapperFluid(ExPBlocks.saltWater));
		ModelLoader.setCustomStateMapper(ExPBlocks.freshWater, new StateMapperFluid(ExPBlocks.freshWater));
		ModelLoader.setCustomStateMapper(ExPBlocks.lava, new StateMapperFluid(ExPBlocks.lava));
		ModelLoader.setCustomStateMapper(ExPBlocks.oil, new StateMapperFluid(ExPBlocks.oil));
		ModelLoader.setCustomStateMapper(ExPBlocks.cattail, new StateMapperCattail());
		ModelLoader.setCustomStateMapper(ExPBlocks.crop, new StateMapperCrop(ExPBlocks.crop));
		ModelLoaderRegistry.registerLoader(new ModelLoaderExP());
		this.initAttacksConditions();
	}
	
	public void registerLogItemModel(Block b)
	{
		for (int i = 0; i < 5; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i * 3 + 1, new ModelResourceLocation(ExPRegistryNames.blockLog, "axis=y,ttype=" + EnumTreeType.values()[i + ((BlockLog)b).logIndex * 5].getName()));
		}
	}
	
	public void registerLeafItemModel(Block b)
	{
		for (int i = 0; i < 15; ++i)
		{
			EnumLeafState els = EnumLeafState.values()[i % 3];
			ResourceLocation rl = new ResourceLocation(ExPRegistryNames.blockLeaves.getResourceDomain(), ExPRegistryNames.blockLeaves.getResourcePath() + "_" + els.getName());
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i, new ModelResourceLocation(rl, "ttype=" + EnumTreeType.values()[i / 3 + ((BlockLeaf)b).logIndex * 5].getName()));
		}
	}
	
	public void registerCustomStateMappers()
	{
		List<Block> lst = Lists.newArrayList();
		lst.addAll(Arrays.asList(ExPBlocks.logs));
		lst.addAll(Arrays.asList(ExPBlocks.logsDeco));
		for (Block b : lst)
		{
			ModelLoader.setCustomStateMapper(b, new IStateMapper(){

				@Override
				public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
				{
					Map<IBlockState, ModelResourceLocation> ret = Maps.newLinkedHashMap();
					for (int i = 0; i < 15; ++i)
					{
						IBlockState state = b.getStateFromMeta(i);
						String enumName = state.getValue(ExPBlockProperties.TREE_TYPES[((BlockLog)b).logIndex]).getName();
						String modelLocation = "axis=" + Axis.values()[i % 3].getName() + ",ttype=" + enumName;
						ModelResourceLocation mrl = new ModelResourceLocation(ExPRegistryNames.blockLog, modelLocation);
						ret.put(state, mrl);
					}
					
					return ret;
				}
			});
		}
		
		lst.clear();
		lst.addAll(Arrays.asList(ExPBlocks.leaves));
		for (Block b : lst)
		{
			ModelLoader.setCustomStateMapper(b, new IStateMapper(){

				@Override
				public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
				{
					Map<IBlockState, ModelResourceLocation> ret = Maps.newLinkedHashMap();
					for (int i = 0; i < 15; ++i)
					{
						IBlockState state = b.getStateFromMeta(i);
						EnumLeafState els = EnumLeafState.values()[i % 3];
						String enumName = state.getValue(ExPBlockProperties.TREE_TYPES[((BlockLeaf)b).logIndex]).getName();
						String modelLocation = "ttype=" + enumName;
						ResourceLocation rl = new ResourceLocation(ExPRegistryNames.blockLeaves.getResourceDomain(), ExPRegistryNames.blockLeaves.getResourcePath() + "_" + els.getName());
						ModelResourceLocation mrl = new ModelResourceLocation(rl, modelLocation);
						ret.put(state, mrl);
					}
					
					return ret;
				}
			});
		}
	}

	@Override
	public void init(FMLInitializationEvent evt)
	{
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getGrassColor, ExPBlocks.grass, ExPBlocks.grass_dry, ExPBlocks.grass_dead);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getGrassColor, ExPBlocks.waterLily);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> 0x02661c, ExPBlocks.grass);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> 0x6a7223, ExPBlocks.grass_dry);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> 0x660000, ExPBlocks.grass_dead);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ExPBlocks.waterLily);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) -> 
			worldIn.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass)worldIn.getBlockState(pos.down()).getBlock()).getGrassColor(worldIn.getBlockState(pos.down()), pos.down(), worldIn) : Helpers.getGrassColor(state, worldIn, pos, tintIndex), ExPBlocks.vegetation);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) -> 
			worldIn.getBlockState(pos).getBlock() instanceof IShrub ? ((IShrub)worldIn.getBlockState(pos).getBlock()).getShrubColor(state, pos, worldIn) : Helpers.getGrassColor(state, worldIn, pos, tintIndex), ArrayUtils.addAll(ExPBlocks.shrubs, ExPBlocks.berryBushes));
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getCoralColor, ExPBlocks.coralRock);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getCoralColor, ExPBlocks.coralPlant);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getLeafColor, ExPBlocks.leaves);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) -> 
			worldIn.getTileEntity(pos) instanceof TileOre ? ((TileOre)worldIn.getTileEntity(pos)).type.getColor() : -1, ExPBlocks.ore, ExPBlocks.boulderOre);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ExPBlocks.vegetation);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ArrayUtils.addAll(ExPBlocks.shrubs, ExPBlocks.berryBushes));
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ((ILeaves)Block.getBlockFromItem(stack.getItem())).getLeavesColorForMeta(stack.getMetadata()), ExPBlocks.leaves);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
			EnumOre.values()[(stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length].getColor(), ExPBlocks.ore, ExPBlocks.boulderOre);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) -> 
			worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileFarmland && worldIn.getTileEntity(pos).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP) ? ExPFarmland.getColor(IFarmland.of(worldIn.getTileEntity(pos), EnumFacing.UP)) : -1, ExPBlocks.farmland);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
			EnumMetal.values()[stack.getMetadata()].getColor(), ExPItems.ingot);
	}

	@Override
	public void postInit(FMLPostInitializationEvent evt)
	{

	}
	
	class StateMapperCrop extends StateMapperBase
	{
		Block b;

		public StateMapperCrop(Block b)
		{
			super();
			this.b = b;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state)
		{
			EnumCrop cropType = state.getValue(ExPBlockProperties.CROP_TYPE);
			return new ModelResourceLocation(new ResourceLocation(this.b.getRegistryName().getResourceDomain(), String.format("crops/%s", cropType.getName())), cropType.getData() == null ? "normal" : String.format("stage=%d", Math.min(state.getValue(ExPBlockProperties.CROP_GROWTH_STAGE), cropType.getData().growthStages - 1)));
		}
	}
	
	class StateMapperFluid extends StateMapperBase
	{
		Block b;
		
		public StateMapperFluid(Block b)
		{
			super();
			this.b = b;
		}
		
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state)
		{
			return new ModelResourceLocation(this.b.getRegistryName(), "fluid");
		}
	}
	
	class StateMapperCattail extends StateMapperBase
	{
		public StateMapperCattail()
		{
			super();
		}
		
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state)
		{
			return new ModelResourceLocation("exp:cattail");
		}
	}
}

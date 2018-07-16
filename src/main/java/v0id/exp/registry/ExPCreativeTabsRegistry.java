package v0id.exp.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.tool.IExPTool;

public class ExPCreativeTabsRegistry extends AbstractRegistry
{
	static
	{
		ExPCreativeTabs.tabUnderground = new CreativeTabs(CreativeTabs.getNextID(), "exp.underground"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPBlocks.rock);
			}
		};
		
		ExPCreativeTabs.tabOres = new CreativeTabs(CreativeTabs.getNextID(), "exp.ores"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPBlocks.ore);
			}
		};
		
		ExPCreativeTabs.tabCommon = new CreativeTabs(CreativeTabs.getNextID(), "exp.commonBlocks"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPBlocks.soil);
			}
		};
		
		ExPCreativeTabs.tabPlantlife = new CreativeTabs(CreativeTabs.getNextID(), "exp.plants"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPBlocks.vegetation);
			}
		};
		
		ExPCreativeTabs.tabMiscBlocks = new CreativeTabs(CreativeTabs.getNextID(), "exp.miscBlocks"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPBlocks.freshWater);
			}
		};
		
		ExPCreativeTabs.tabMetals = new CreativeTabs(CreativeTabs.getNextID(), "exp.metals"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPItems.ingot);
			}
		};
		
		ExPCreativeTabs.tabTools = new CreativeTabs(CreativeTabs.getNextID(), "exp.tools"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(IExPTool.allTools.get(Pair.of(EnumToolClass.KNIFE, EnumToolStats.STONE)));
			}
		};
		
		ExPCreativeTabs.tabFood = new CreativeTabs(CreativeTabs.getNextID(), "exp.food"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPItems.food);
			}
		};

		ExPCreativeTabs.tabMiscItems = new CreativeTabs(CreativeTabs.getNextID(), "exp.miscItems"){
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(ExPItems.generic);
			}
		};
	}
	
	public ExPCreativeTabsRegistry()
	{
		super();
	}
}

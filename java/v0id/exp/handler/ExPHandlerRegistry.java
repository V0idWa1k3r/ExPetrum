package v0id.exp.handler;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.item.IItemRegistryEntry;

@Mod.EventBusSubscriber(modid = ExPRegistryNames.modid)
public class ExPHandlerRegistry
{
	public static final List<IBlockRegistryEntry> blockEntries = Lists.newArrayList();
	public static final List<IItemRegistryEntry> itemEntries = Lists.newArrayList();
	
	@SubscribeEvent
	public static void onBlocksRegistry(RegistryEvent.Register<Block> event)
	{
		blockEntries.forEach(e -> e.registerBlock(event.getRegistry()));
	}
	
	@SubscribeEvent
	public static void onItemsRegistry(RegistryEvent.Register<Item> event)
	{
		itemEntries.forEach(e -> e.registerItem(event.getRegistry()));
	}
}

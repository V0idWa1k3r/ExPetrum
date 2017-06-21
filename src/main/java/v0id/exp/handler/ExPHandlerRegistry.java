package v0id.exp.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.registry.ExPBiomeRegistry;
import v0id.exp.registry.ExPBlocksRegistry;
import v0id.exp.registry.ExPItemsRegistry;
import v0id.exp.registry.ExPPotionRegistry;

@Mod.EventBusSubscriber(modid = ExPRegistryNames.modid)
public class ExPHandlerRegistry
{
	@SubscribeEvent
	public static void onBlocksRegistry(RegistryEvent.Register<Block> event)
	{
		ExPBlocksRegistry.instance.registerBlocks(event);
	}
	
	@SubscribeEvent
	public static void onItemsRegistry(RegistryEvent.Register<Item> event)
	{
		ExPBlocksRegistry.instance.registerItemBlocks(event);
		ExPItemsRegistry.instance.registerItems(event);
	}
	
	@SubscribeEvent
	public static void onBiomesRegistry(RegistryEvent.Register<Biome> event)
	{
		ExPBiomeRegistry.instance.registerBiomes(event);
	}
	
	@SubscribeEvent
	public static void onPotionsRegistry(RegistryEvent.Register<Potion> event)
	{
		ExPPotionRegistry.instance.registerPotions(event);
	}
}

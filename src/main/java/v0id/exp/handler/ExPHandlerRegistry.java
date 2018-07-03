package v0id.exp.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.registry.*;

@Mod.EventBusSubscriber(modid = ExPRegistryNames.modid)
public class ExPHandlerRegistry
{
	@SubscribeEvent
	public static void onBlocksRegistry(RegistryEvent.Register<Block> event)
	{
		ExPBlocksRegistry.instance.registerBlocks(event);
	}

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> event)
    {
        ExPRecipeRegistry.instance.registerRecipes(event);
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

	@SubscribeEvent
	public static void onEntitiesRegistry(RegistryEvent.Register<EntityEntry> event)
	{
		ExPEntityRegistry.instance.registerEntities(event);
	}
}

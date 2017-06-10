package v0id.exp;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.CoreModManager;
import v0id.api.core.VoidApi;
import v0id.api.core.logging.LogLevel;
import v0id.api.core.logging.VoidLogger;
import v0id.api.core.util.java.Reflector;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.YearlyTemperatureRange;
import v0id.exp.crop.CropDataLoader;
import v0id.exp.player.inventory.WeightDataLoader;
import v0id.exp.proxy.IExPProxy;
import v0id.exp.registry.AbstractRegistry;
import v0id.exp.registry.ExPBiomeRegistry;
import v0id.exp.registry.ExPBlocksRegistry;
import v0id.exp.registry.ExPCapabilityRegistry;
import v0id.exp.registry.ExPCreativeTabsRegistry;
import v0id.exp.registry.ExPEntityRegistry;
import v0id.exp.registry.ExPEventRegistry;
import v0id.exp.registry.ExPFluidRegistry;
import v0id.exp.registry.ExPItemsRegistry;
import v0id.exp.registry.ExPNetworkRegistry;
import v0id.exp.registry.ExPOreDictRegistry;
import v0id.exp.registry.ExPPotionRegistry;
import v0id.exp.registry.ExPTileRegistry;
import v0id.exp.registry.ExPWeaponAttacksRegistry;
import v0id.exp.registry.ExPWorldRegistry;

@Mod(modid = "exp", useMetadata = true, dependencies = "required-after:voidapi;after:chiselsandbits;after:jei", version = "1.11.2-1.0.0.0")
public class ExPetrum
{
	@Mod.Instance("exp")
	public static ExPetrum instance;
	public static ModContainer containerOfSelf;
	public static boolean isDevEnvironment;
	public static File configDirectory;
	@SidedProxy(clientSide = "v0id.exp.proxy.ExPProxyClient", serverSide = "v0id.exp.proxy.ExPProxyServer")
	public static IExPProxy proxy;
	
	static
	{
		FluidRegistry.enableUniversalBucket();
		try
		{
			// By the way this file contains 682 lines of pure json :P
			// I have not constructed it manually, no
			// I wrote a C# program that did it for me :D
			try (InputStream is = ExPetrum.class.getResourceAsStream("/assets/exp/data/seasonsTemperatureRanges.json"))
			{
				String s = IOUtils.toString(is);
				YearlyTemperatureRange.instance = new Gson().fromJson(s, YearlyTemperatureRange.class);
			}
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to read it's temperatures json file data!", true);
		}
		
		configDirectory = Loader.instance().getConfigDir();
		ExPMisc.modLogger = VoidLogger.createLogger(ExPetrum.class, LogLevel.Fine);
		Stream.of(EnumOre.values()).forEach(EnumOre::registerWorldgen);
		CropDataLoader.loadAllData();
		WeightDataLoader.loadAllData();
		createRegistries();
		Reflector.preloadClass("v0id.exp.handler.ExPHandlerRegistry", false);
	}
	
	public static void createRegistries()
	{
		AbstractRegistry.create(ExPCapabilityRegistry.class);
		AbstractRegistry.create(ExPFluidRegistry.class);
		AbstractRegistry.create(ExPCreativeTabsRegistry.class);
		AbstractRegistry.create(ExPBlocksRegistry.class);
		AbstractRegistry.create(ExPItemsRegistry.class);
		AbstractRegistry.create(ExPEventRegistry.class);
		AbstractRegistry.create(ExPNetworkRegistry.class);
		AbstractRegistry.create(ExPEntityRegistry.class);
		AbstractRegistry.create(ExPTileRegistry.class);
		AbstractRegistry.create(ExPBiomeRegistry.class);
		AbstractRegistry.create(ExPWorldRegistry.class);
		AbstractRegistry.create(ExPOreDictRegistry.class);
		AbstractRegistry.create(ExPWeaponAttacksRegistry.class);
		AbstractRegistry.create(ExPPotionRegistry.class);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		containerOfSelf = Loader.instance().activeModContainer();
		this.configDirectory = evt.getModConfigurationDirectory();
		this.setDevEnvironment();
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum pre initializing.");
		AbstractRegistry.registries.forEach(reg -> reg.preInit(evt));
		VoidApi.proxy.executeOnClient("v0id.exp.client.ClientRegistry", "preInit", VoidApi.proxy.provideClientOnlyInstance("v0id.exp.client.ClientRegistry"), FMLPreInitializationEvent.class, evt);
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum pre initialized.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum initializing.");
		AbstractRegistry.registries.forEach(reg -> reg.init(evt));
		VoidApi.proxy.executeOnClient("v0id.exp.client.ClientRegistry", "init", VoidApi.proxy.provideClientOnlyInstance("v0id.exp.client.ClientRegistry"), FMLInitializationEvent.class, evt);
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum initialized.");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum post initializing.");
		AbstractRegistry.registries.forEach(reg -> reg.postInit(evt));
		VoidApi.proxy.executeOnClient("v0id.exp.client.ClientRegistry", "postInit", VoidApi.proxy.provideClientOnlyInstance("v0id.exp.client.ClientRegistry"), FMLPostInitializationEvent.class, evt);
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum post initialized.");
	}
	
	public void setDevEnvironment()
	{
		try
		{
			Class<CoreModManager> clazz = CoreModManager.class;
			Field f = clazz.getDeclaredField("deobfuscatedEnvironment");
			boolean accessibilityFlag = f.isAccessible();
			f.setAccessible(true);
			isDevEnvironment = f.getBoolean(null);
			f.setAccessible(accessibilityFlag);
			if (isDevEnvironment)
			{
				ExPMisc.modLogger.log(LogLevel.Fine, "ExPetrum has detected dev environment! Additional debug features enabled!");
				ExPMisc.modLogger.setLevel(LogLevel.Debug);
			}
			else
			{
				ExPMisc.modLogger.log(LogLevel.Fine, "ExPetrum has detected normal minecraft environment. No debug features enabled.");
			}
		}
		catch (Exception ex)
		{
			ExPMisc.modLogger.log(LogLevel.Warning, "ExPetrum was unable to determine the environment it is located in! Assuming normal minecraft instance.");
		}
	}
}

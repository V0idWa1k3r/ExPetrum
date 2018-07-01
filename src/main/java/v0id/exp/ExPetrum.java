package v0id.exp;

import com.google.gson.Gson;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.apache.commons.io.IOUtils;
import v0id.core.logging.LogLevel;
import v0id.core.logging.VoidLogger;
import v0id.core.util.java.Reflector;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.YearlyTemperatureRange;
import v0id.core.VoidApi;
import v0id.exp.crop.CropDataLoader;
import v0id.exp.handler.GuiHandler;
import v0id.exp.player.inventory.WeightDataLoader;
import v0id.exp.proxy.IExPProxy;
import v0id.exp.registry.*;
import v0id.exp.util.EntityPackManager;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@Mod(modid = "exp", useMetadata = true, dependencies = "after:chiselsandbits;after:jei", version = "1.11.2-1.0.0.0")
public class ExPetrum
{
	@Mod.Instance("exp")
	public static ExPetrum instance;
	public static ModContainer containerOfSelf;
	public static boolean isDevEnvironment;
	public static final File configDirectory;
	@SidedProxy(clientSide = "v0id.exp.proxy.ExPProxyClient", serverSide = "v0id.exp.proxy.ExPProxyServer")
	public static IExPProxy proxy;
	
	static
	{
		FluidRegistry.enableUniversalBucket();
        loadTemperatureRanges();
        configDirectory = Loader.instance().getConfigDir();
		ExPMisc.modLogger = VoidLogger.createLogger(ExPetrum.class, LogLevel.Fine);
		Stream.of(EnumOre.values()).forEach(EnumOre::registerWorldgen);
		CropDataLoader.loadAllData();
		WeightDataLoader.loadAllData();
		createRegistries();
		Reflector.preloadClass("v0id.exp.handler.ExPHandlerRegistry", false);
	}

    private static void loadTemperatureRanges()
    {
        try
        {
            // By the way this file contains 682 lines of pure json :P
            // I have not constructed it manually, no
            // I wrote a C# program that did it for me :D
            try (InputStream is = ExPetrum.class.getResourceAsStream("/assets/exp/data/seasonsTemperatureRanges.json"))
            {
                String s = IOUtils.toString(is, StandardCharsets.UTF_8);
                YearlyTemperatureRange.instance = new Gson().fromJson(s, YearlyTemperatureRange.class);
            }
        }
        catch (Exception ex)
        {
            FMLCommonHandler.instance().raiseException(ex, "ExPetrum was unable to read it's temperatures json file data!", true);
        }
    }

    public static void createRegistries()
	{
        AbstractRegistry.create(ExPConfigRegistry.class);
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
		AbstractRegistry.create(ExPRecipeRegistry.class);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		VoidApi.getInstance().preInit(evt);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		containerOfSelf = Loader.instance().activeModContainer();
		this.setDevEnvironment();
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum pre initializing.");
        AbstractRegistry.registries.add(proxy);
		AbstractRegistry.registries.forEach(reg -> reg.preInit(evt));
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum pre initialized.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum initializing.");
		AbstractRegistry.registries.forEach(reg -> reg.init(evt));
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum initialized.");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum post initializing.");
		AbstractRegistry.registries.forEach(reg -> reg.postInit(evt));
		ExPMisc.modLogger.log(LogLevel.Debug, "ExPetrum post initialized.");
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
    {
        EntityPackManager.saveAllPacksInfo(false);
        EntityPackManager.cleanup();
    }
	
	private void setDevEnvironment()
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

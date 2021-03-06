package v0id.exp;

import com.google.gson.Gson;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.YearlyTemperatureRange;
import v0id.exp.command.CommandToggledownfall;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod(modid = "exp", useMetadata = true, dependencies = "after:chiselsandbits;after:jei", version = "1.0.12", updateJSON = "https://raw.githubusercontent.com/V0idWa1k3r/ExPetrum/master/version.json", certificateFingerprint = "fed4a2156e996f7a020359ca4c406cdde4ddcefd")
public class ExPetrum
{
	@Mod.Instance("exp")
	public static ExPetrum instance;
	public static ModContainer containerOfSelf;
	public static boolean isDevEnvironment;
	private static boolean fingerprintViolated;
	public static final File configDirectory;

	@SidedProxy(clientSide = "v0id.exp.proxy.ExPProxyClient", serverSide = "v0id.exp.proxy.ExPProxyServer")
	public static IExPProxy proxy;

	static
	{
		FluidRegistry.enableUniversalBucket();
        configDirectory = Loader.instance().getConfigDir();
		ExPMisc.modLogger = LogManager.getLogger("ExPetrum");
        createRegistries();
        loadTemperatureRanges();
        Stream.of(EnumOre.values()).forEach(EnumOre::registerWorldgen);
        CropDataLoader.loadAllData();
        WeightDataLoader.loadAllData();
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
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		containerOfSelf = Loader.instance().activeModContainer();
		this.setDevEnvironment();
        AbstractRegistry.registries.add(proxy);
		AbstractRegistry.registries.forEach(reg -> reg.preInit(evt));
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		AbstractRegistry.registries.forEach(reg -> reg.init(evt));
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		AbstractRegistry.registries.forEach(reg -> reg.postInit(evt));
	}

    @EventHandler
	public void serverStarting(FMLServerStartingEvent evt)
	{
        evt.registerServerCommand(new CommandToggledownfall());
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
    {
        EntityPackManager.saveAllPacksInfo(false);
        EntityPackManager.cleanup();
    }

    @EventHandler
	public void fingerprintViolated(FMLFingerprintViolationEvent event)
	{
	    fingerprintViolated = true;
	    if (event.isDirectory())
        {
            ExPMisc.modLogger.warn("ExPetrum fingerprint doesn't match but we are in a dev environment so that's okay.");
        }
        else
        {
            ExPMisc.modLogger.error("ExPetrum fingerprint doesn't match! Expected {}, got {}!", event.getExpectedFingerprint(), event.getFingerprints().stream().collect(Collectors.joining(" , ")));
        }
	}

    public static boolean isFingerprintViolated()
    {
        return fingerprintViolated;
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
				ExPMisc.modLogger.log(Level.INFO, "ExPetrum has detected dev environment! Additional debug features enabled!");
			}
			else
			{
				ExPMisc.modLogger.log(Level.INFO, "ExPetrum has detected normal minecraft environment. No debug features enabled.");
			}
		}
		catch (Exception ex)
		{
			ExPMisc.modLogger.log(Level.INFO, "ExPetrum was unable to determine the environment it is located in! Assuming normal minecraft instance.");
		}
	}
}

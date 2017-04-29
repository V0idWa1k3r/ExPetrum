package v0id.exp;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.core.VoidApi;
import v0id.api.core.logging.LogLevel;
import v0id.api.core.logging.VoidLogger;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.YearlyTemperatureRange;
import v0id.exp.registry.AbstractRegistry;
import v0id.exp.registry.ExPBiomeRegistry;
import v0id.exp.registry.ExPBlocksRegistry;
import v0id.exp.registry.ExPCapabilityRegistry;
import v0id.exp.registry.ExPEntityRegistry;
import v0id.exp.registry.ExPEventRegistry;
import v0id.exp.registry.ExPFluidRegistry;
import v0id.exp.registry.ExPNetworkRegistry;

@Mod(modid = "exp", useMetadata = true)
public class ExPetrum
{
	@Mod.Instance("exp")
	public static ExPetrum instance;
	
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
		
		ExPMisc.modLogger = VoidLogger.createLogger(ExPetrum.class, LogLevel.Fine);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		AbstractRegistry.create(ExPCapabilityRegistry.class);
		AbstractRegistry.create(ExPBiomeRegistry.class);
		AbstractRegistry.create(ExPFluidRegistry.class);
		AbstractRegistry.create(ExPBlocksRegistry.class);
		AbstractRegistry.create(ExPEventRegistry.class);
		AbstractRegistry.create(ExPNetworkRegistry.class);
		AbstractRegistry.create(ExPEntityRegistry.class);
		AbstractRegistry.registries.forEach(reg -> reg.preInit(evt));
		VoidApi.proxy.executeOnClient("v0id.exp.client.ClientRegistry", "preInit", VoidApi.proxy.provideClientOnlyInstance("v0id.exp.client.ClientRegistry"), FMLPreInitializationEvent.class, evt);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		AbstractRegistry.registries.forEach(reg -> reg.init(evt));
		VoidApi.proxy.executeOnClient("v0id.exp.client.ClientRegistry", "init", VoidApi.proxy.provideClientOnlyInstance("v0id.exp.client.ClientRegistry"), FMLInitializationEvent.class, evt);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt)
	{
		
		AbstractRegistry.registries.forEach(reg -> reg.postInit(evt));
		VoidApi.proxy.executeOnClient("v0id.exp.client.ClientRegistry", "postInit", VoidApi.proxy.provideClientOnlyInstance("v0id.exp.client.ClientRegistry"), FMLPostInitializationEvent.class, evt);
	}
}

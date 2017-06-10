package v0id.exp.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class AbstractRegistry
{
	public static List<AbstractRegistry> registries = Lists.newArrayList();
	
	public AbstractRegistry()
	{
		registries.add(this);
	}
	
	public void preInit(FMLPreInitializationEvent evt){}
	
	public void init(FMLInitializationEvent evt){}
	
	public void postInit(FMLPostInitializationEvent evt){}
	
	public static AbstractRegistry create(Class<? extends AbstractRegistry> clazz) 
	{
		try
		{
			return clazz.newInstance();
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}

package v0id.exp.registry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class AbstractRegistry implements ILifecycleListener
{
	public static List<ILifecycleListener> registries = Lists.newArrayList();
	
	public AbstractRegistry()
	{
		registries.add(this);
	}
	
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

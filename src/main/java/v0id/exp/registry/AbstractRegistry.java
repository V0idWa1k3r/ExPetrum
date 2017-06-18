package v0id.exp.registry;

import com.google.common.collect.Lists;

import java.util.List;

public abstract class AbstractRegistry implements ILifecycleListener
{
	public static final List<ILifecycleListener> registries = Lists.newArrayList();
	
	public AbstractRegistry()
	{
		registries.add(this);
	}
	
	public static void create(Class<? extends AbstractRegistry> clazz)
	{
		try
		{
			clazz.newInstance();
		}
		catch (Exception ignored)
		{
		}
	}
}

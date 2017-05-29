package v0id.exp.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import v0id.api.core.markers.StaticStorage;
import v0id.api.exp.data.IOreDictEntry;

public class OreDictManager
{
	public static List<Class<?>> registryEntries = Lists.newArrayList();
	
	public static void mapStorage(Class<?> clazz)
	{
		assert clazz.isAnnotationPresent(StaticStorage.class) : "Registry must be a static storage!";
		registryEntries.add(clazz);
	}
	
	public static void register()
	{
		for (Class<?> clazz : registryEntries)
		{
			Stream.of(clazz.getDeclaredFields()).filter(f -> Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers())).forEach(OreDictManager::processField);
		}
		
		registryEntries.clear();
	}
	
	private static void processField(Field f)
	{
		try
		{
			if (f.getType().isArray())
			{
				Object o = f.get(null);
				for (int i = 0; i < Array.getLength(o); ++i)
				{
					Object arrayAt = Array.get(o, i);
					if (IOreDictEntry.class.isAssignableFrom(arrayAt.getClass()))
					{
						((IOreDictEntry)arrayAt).registerOreDictNames();
					}
				}
			}
			else
			{
				Object at = f.get(null);
				if (at != null && IOreDictEntry.class.isAssignableFrom(at.getClass()))
				{
					((IOreDictEntry)f.get(null)).registerOreDictNames();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}

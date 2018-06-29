package v0id.exp.util;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.data.IOreDictEntry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;

public class OreDictManager
{
	public static final List<Class<?>> registryEntries = Lists.newArrayList();
	
	public static void mapStorage(Class<?> clazz)
	{
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

	public static String[] getOreNames(ItemStack is)
    {
        int[] ids = OreDictionary.getOreIDs(is);
        String[] ret = new String[ids.length];
        for (int i = 0; i < ids.length; ++i)
        {
            ret[i] = OreDictionary.getOreName(ids[i]);
        }

        return ret;
    }
}

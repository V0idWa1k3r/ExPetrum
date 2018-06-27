package v0id.core.util.java;

import java.lang.reflect.Field;

import javax.annotation.Nullable;

import v0id.core.VCLoggers;
import v0id.core.logging.LogLevel;

@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public class Reflector
{
	public static final Object invalid = new Object();
	
	@SuppressWarnings("UnusedReturnValue")
	public static boolean preloadClass(String className)
	{
		return preloadClass(className, true);
	}
	
	public static boolean preloadClass(String className, boolean silent)
	{
		try
		{
			Class.forName(className);
			return true;
		}
		catch (Throwable t)
		{
			if (!silent)
			{
				VCLoggers.loggerReflector.log(LogLevel.Error, "Could not preload class %s!", t, className);
			}
			
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T>T reflect(Class<?> from, String fieldname, @Nullable Object accessor)
	{
		try
		{
			Field f = from.getDeclaredField(fieldname);
			if (f != null)
			{
				if (!f.isAccessible())
				{
					f.setAccessible(true);
				}
				
				return (T) f.get(accessor);
			}
			else
			{
				VCLoggers.loggerReflector.log(LogLevel.Error, "%s is not a declared field of %s!", fieldname, from.getName());
				return null;
			}
		}
		catch (Exception ex)
		{
			VCLoggers.loggerReflector.log(LogLevel.Error, "An exception occurred trying to get %s of %s!", ex, fieldname, from.getName());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T>T reflect(Class<?> from, int fieldIndex, @Nullable Object accessor)
	{
		try
		{
			Field f = from.getDeclaredFields()[fieldIndex];
			if (!f.isAccessible())
			{
				f.setAccessible(true);
			}
				
			return (T) f.get(accessor);
		}
		catch (Exception ex)
		{
			VCLoggers.loggerReflector.log(LogLevel.Error, "An exception occurred trying to get field at %d of %s!", ex, fieldIndex, from.getName());
			return null;
		}
	}
}

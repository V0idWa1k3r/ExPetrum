package v0id.core.util.cfg;

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.io.FileUtils;
import v0id.core.VCLoggers;
import v0id.core.logging.LogLevel;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public abstract class SerializableConfig<T> extends Configuration
{
	public T dataHolder;
	public static final ArrayList<ConfigAdapter<Object>> adapters = Lists.newArrayList();

	public abstract T provideInstance();
	
	public SerializableConfig(File f)
	{
		super(f);
	}
	
	@Override
	public void save()
	{
		this.serialize();
		super.save();
	}
	
	@Override
	public void load()
	{
		super.load();
		
		try
		{
			// File is empty
			if (this.getConfigFile().length() < 64 && FileUtils.readFileToString(this.getConfigFile(), StandardCharsets.UTF_8).trim().isEmpty())
			{
				this.dataHolder = this.provideInstance();
				this.save();
				super.load();
			}
		}
		catch(Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception trying to deserialize a configuration file!", ex);
		}
		
		this.dataHolder = this.deserialize();
	}
	
	@SuppressWarnings("unchecked")
	public T deserialize()
	{
		try
		{
			if (this.dataHolder == null)
			{
				this.dataHolder = this.provideInstance();
			}
			
			T object = (T) this.dataHolder.getClass().newInstance();
			deserializeRecursive(this.getCategory(CATEGORY_GENERAL), object);
			return object;
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception trying to deserialize a configuration file!", ex);
			return this.dataHolder;
		}
	}
	
	public void deserializeRecursive(ConfigCategory categoryCurrent, Object objectCurrent)
	{
		try
		{
			for (Field f : objectCurrent.getClass().getDeclaredFields())
			{
				if (Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()) && !f.isAnnotationPresent(ConfigIgnore.class))
				{
					if (f.getType().isPrimitive() || f.getType().equals(String.class))
					{
						if (categoryCurrent.containsKey(f.getName()))
						{
							this.deserializePrimitiveField(f, categoryCurrent.get(f.getName()), objectCurrent);
						}
					}
					else
					{
						boolean hasAdapter = false;
						for (ConfigAdapter<Object> a : adapters)
						{
							if (a.acceptsDeserialization(f.getType()))
							{
								f.set(objectCurrent, a.deserialize(categoryCurrent.get(f.getName())));
								hasAdapter = true;
								break;
							}
						}
						
						if (!hasAdapter)
						{
							deserializeRecursive(new ConfigCategory(f.getName(), categoryCurrent), f.getType().newInstance());
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception trying to serialize a configuration file!", ex);
		}
	}
	
	public void deserializePrimitiveField(Field f, Property prop, Object instance) throws IllegalArgumentException, IllegalAccessException
	{
		switch(fromJavaPrimitiveType(f.getType()))
		{
			case INTEGER:
			{
				if (f.getType().equals(Byte.TYPE))
				{
					f.setByte(instance, (byte) prop.getInt());
				}
				
				if (f.getType().equals(Short.TYPE))
				{
					f.setShort(instance, (short) prop.getInt());
				}
				
				if (f.getType().equals(Integer.TYPE))
				{
					f.setInt(instance, prop.getInt());
				}
				
				break;
			}
			
			case DOUBLE:
			{
				if (f.getType().equals(Float.TYPE))
				{
					f.setFloat(instance, (float) prop.getDouble());
				}
				
				if (f.getType().equals(Double.TYPE))
				{
					f.setDouble(instance, prop.getDouble());
				}
				
				if (f.getType().equals(Long.TYPE))
				{
					f.setLong(instance, (long) prop.getDouble());
				}
				
				break;
			}
			
			case BOOLEAN:
			{
				f.setBoolean(instance, prop.getBoolean());
				break;
			}
			
			default:
			{
				f.set(instance, prop.getString());
			}
		}
	}
	
	public void serialize()
	{
		if (this.dataHolder == null)
		{
			this.dataHolder = this.provideInstance();
		}
		
		serializeRecursive(this.getCategory(CATEGORY_GENERAL), this.dataHolder);
	}
	
	public void serializeRecursive(ConfigCategory categoryCurrent, Object toSerialize)
	{
		try
		{
			for (Field f : toSerialize.getClass().getDeclaredFields())
			{
				if (Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()) && !f.isAnnotationPresent(ConfigIgnore.class))
				{
					if (f.getType().isPrimitive() || f.getType().equals(String.class))
					{
						categoryCurrent.put(f.getName(), new Property(f.getName(), f.get(toSerialize).toString(), fromJavaPrimitiveType(f.getType())));
					}
					else
					{
						boolean hasAdapter = false;
						for (ConfigAdapter<Object> a : adapters)
						{
							if (a.accepts(f.get(toSerialize)))
							{
								a.serialize(f.get(toSerialize), new ConfigCategory(f.getName(), categoryCurrent));
								hasAdapter = true;
								break;
							}
						}
						
						if (!hasAdapter)
						{
							serializeRecursive(new ConfigCategory(f.getName(), categoryCurrent), f.get(toSerialize));
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception trying to serialize a configuration file!", ex);
		}
	}
	
	public static Property.Type fromJavaPrimitiveType(Class<?> javaType)
	{
		if (javaType.equals(Byte.TYPE) || javaType.equals(Short.TYPE) || javaType.equals(Integer.TYPE))
		{
			return Property.Type.INTEGER;
		}
		
		if (javaType.equals(Float.TYPE) || javaType.equals(Double.TYPE) || javaType.equals(Long.TYPE))
		{
			return Property.Type.DOUBLE;
		}
		
		if (javaType.equals(Boolean.TYPE))
		{
			return Property.Type.BOOLEAN;
		}
		
		return Property.Type.STRING;
	}
}

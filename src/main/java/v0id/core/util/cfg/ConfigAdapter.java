package v0id.core.util.cfg;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

@SuppressWarnings("WeakerAccess")
public abstract class ConfigAdapter<T>
{
	public abstract void serialize(T t, ConfigCategory category);
	
	public abstract T deserialize(Property prop);
	
	public abstract boolean accepts(Object o);
	
	public abstract boolean acceptsDeserialization(Class<?> c);
}

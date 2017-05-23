package v0id.api.exp.inventory;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public interface IWeightProvider
{
	float provideWeight(ItemStack item);
	
	Pair<Byte, Byte> provideVolume(ItemStack item);
	
	public static Map<Pair<String, Short>, Pair<Byte, Byte>> configVolumes = Maps.newHashMap();
	
	public static void registerVolume(String registryName, short metadata, Pair<Byte, Byte> volume)
	{
		configVolumes.put(Pair.of(registryName, metadata), volume);
	}
	
	public static Optional<Pair<Byte, Byte>> tryGetRegisteredVolume(ItemStack is)
	{
		if (is.isEmpty())
		{
			return Optional.empty();
		}
		
		String name = is.getItem().getRegistryName().toString();
		int meta = is.getMetadata();
		Pair<String, Short> p = Pair.of(name, (short) meta);
		if (configVolumes.containsKey(p))
		{
			return Optional.of(configVolumes.get(p));
		}
		
		Pair<String, Short> p2 = Pair.of(name, (short) OreDictionary.WILDCARD_VALUE);
		if (configVolumes.containsKey(p2))
		{
			return Optional.of(configVolumes.get(p2));
		}
		
		return Optional.empty();
	}
}

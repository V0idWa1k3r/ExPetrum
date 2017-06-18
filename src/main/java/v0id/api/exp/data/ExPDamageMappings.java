package v0id.api.exp.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandom.Item;
import v0id.api.exp.player.BodyPart;

import java.util.Arrays;
import java.util.List;

public class ExPDamageMappings
{
	public static final ListMultimap<DamageSource, DamageMapping> staticMappings = ArrayListMultimap.create();
	public static final List<IDamageHandler> handlers = Lists.newArrayList();
	public static final List<DamageMapping> defaultMappings = Lists.newArrayList();
	
	static
	{
		defaultMappings.add(new DamageMapping(BodyPart.HEAD, 10));
		defaultMappings.add(new DamageMapping(BodyPart.ARM_LEFT, 10));
		defaultMappings.add(new DamageMapping(BodyPart.ARM_RIGHT, 10));
		defaultMappings.add(new DamageMapping(BodyPart.BODY, 10));
		defaultMappings.add(new DamageMapping(BodyPart.LEG_LEFT, 10));
		defaultMappings.add(new DamageMapping(BodyPart.LEG_RIGHT, 10)); 
		
		staticMappings.putAll(DamageSource.ANVIL, Arrays.asList(
			new DamageMapping(BodyPart.HEAD, 100),
			new DamageMapping(BodyPart.BODY, 10)
		));
		
		staticMappings.putAll(DamageSource.FALL, Arrays.asList(
			new DamageMapping(BodyPart.LEG_LEFT, 100),
			new DamageMapping(BodyPart.LEG_RIGHT, 100),
			new DamageMapping(BodyPart.BODY, 10)
		));
		
		staticMappings.putAll(DamageSource.FALLING_BLOCK, Arrays.asList(
			new DamageMapping(BodyPart.HEAD, 100),
			new DamageMapping(BodyPart.BODY, 50),
			new DamageMapping(BodyPart.ARM_LEFT, 50),
			new DamageMapping(BodyPart.ARM_RIGHT, 50),
			new DamageMapping(BodyPart.LEG_LEFT, 10),
			new DamageMapping(BodyPart.LEG_RIGHT, 10)
		));
		
		staticMappings.putAll(DamageSource.HOT_FLOOR, Arrays.asList(
			new DamageMapping(BodyPart.LEG_LEFT, 100),
			new DamageMapping(BodyPart.LEG_RIGHT, 100)
		));
	}
	
	public static List<DamageMapping> provide(DamageSource src)
	{
		if (staticMappings.containsKey(src))
		{
			return staticMappings.get(src);
		}
		
		for (IDamageHandler handler : handlers)
		{
			List<DamageMapping> lst = handler.provide(src);
			if (lst != null && !lst.isEmpty())
			{
				return lst;
			}
		}
		
		return defaultMappings;
	}
	
	@FunctionalInterface
	public interface IDamageHandler
	{
		List<DamageMapping> provide(DamageSource src);
	}
	
	public static class DamageMapping extends Item
	{
		private BodyPart part;
		
		public DamageMapping(BodyPart part, int itemWeightIn)
		{
			super(itemWeightIn);
			this.setPart(part);
		}

		public BodyPart getPart()
		{
			return this.part;
		}

		public void setPart(BodyPart part)
		{
			this.part = part;
		}
		
	}
}

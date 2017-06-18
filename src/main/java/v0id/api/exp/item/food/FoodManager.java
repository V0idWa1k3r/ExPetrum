package v0id.api.exp.item.food;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.player.FoodGroup;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class FoodManager
{
	public static final Map<Pair<Item, Integer>, Pair<Float, EnumMap<FoodGroup, Float>>> simpleMappings = Maps.newHashMap();
	public static final List<Function<ItemStack, Pair<Float, EnumMap<FoodGroup, Float>>>> mappings = Lists.newArrayList();
	
	static
	{
		// Insert your functions to this collection with add(0, func) to make this one always execute last.
		mappings.add(stack -> {
			if (stack.getItem() instanceof ItemFood)
			{
				ItemFood food = (ItemFood) stack.getItem();
				int fRestored = food.getHealAmount(stack);
				float satRestored = food.getSaturationModifier(stack);
				EnumMap<FoodGroup, Float> nutrients = new EnumMap<>(FoodGroup.class);
				Stream.of(FoodGroup.values()).forEach(n -> nutrients.put(n, satRestored));
				return Pair.of((float)fRestored * 100, nutrients);
			}
			
			return null;
		});
	}
	
	public static Pair<Float, EnumMap<FoodGroup, Float>> provideFoodStats(ItemStack stack)
	{
		if (simpleMappings.containsKey(Pair.of(stack.getItem(), stack.getMetadata())))
		{
			return simpleMappings.get(Pair.of(stack.getItem(), stack.getMetadata()));
		}
		else
		{
			for (Function<ItemStack, Pair<Float, EnumMap<FoodGroup, Float>>> f : mappings)
			{
				Pair<Float, EnumMap<FoodGroup, Float>> ret = f.apply(stack);
				if (ret != null)
				{
					return ret;
				}
			}
			
			return null;
		}
	}
}

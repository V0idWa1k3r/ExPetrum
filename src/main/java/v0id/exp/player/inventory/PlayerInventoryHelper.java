package v0id.exp.player.inventory;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.inventory.IWeightProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerInventoryHelper
{
	public static Pair<Byte, Byte> getVolume(ItemStack is)
	{
		return is.getItem() instanceof IWeightProvider ? ((IWeightProvider)is.getItem()).provideVolume(is) : IWeightProvider.tryGetRegisteredVolume(is).orElse(IWeightProvider.DEFAULT_VOLUME);
	}

	public static float getWeight(ItemStack is)
    {
        return (is.getItem() instanceof IWeightProvider ? ((IWeightProvider)is.getItem()).provideWeight(is) : IWeightProvider.tryGetRegisteredWeight(is).orElse(0F)) * is.getCount();
    }

    public static boolean canItemStay(ItemStack is, int sX, int sY, InventoryPlayer inventory)
    {
        for (int x = 0; x < 9; ++x)
        {
            for (int y = 0; y < 3; ++y)
            {
                ItemStack stack = inventory.getStackInSlot(9 + y * 9 + x);
                if (stack != is && !stack.isEmpty())
                {
                    Pair<Byte, Byte> vol = getVolume(stack);
                    int stex = x + vol.getLeft() - 1;
                    int stey = y + vol.getRight() - 1;
                    if (sX >= x && sX <= stex && sY >= y && sY <= stey)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }
	
	@SuppressWarnings("SuspiciousNameCombination")
    public static Map<Pair<Integer, Integer>, Boolean> getSlotData(EntityPlayer player)
	{
		HashMap<Pair<Integer, Integer>, Boolean> ret = Maps.newHashMap();
		for (int i = 9; i < 36; ++i)
		{
			ItemStack is = player.inventory.getStackInSlot(i);
			if (!is.isEmpty())
			{
				int x = (i - 9) % 9;
				int y = (i - 9) / 9;
				ret.put(Pair.of(x, y), true);
				if (getVolume(is) != IWeightProvider.DEFAULT_VOLUME)
				{
					Pair<Byte, Byte> volume = getVolume(is);
					for (byte b = 0; b < volume.getLeft(); ++b)
					{
						for (byte b1 = 0; b1 < volume.getRight(); ++b1)
						{
							ret.put(Pair.of(x + b, y + b1), true);
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	@SuppressWarnings("SuspiciousNameCombination")
    public static int findFirstAvailableSlotFor(ItemStack is, Optional<Map<Pair<Integer, Integer>, Boolean>> data, EntityPlayer player)
	{
		InventoryPlayer inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (ItemStack.areItemsEqual(stack, is) && ItemStack.areItemStackTagsEqual(stack, is))
			{
				int max = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
				if (stack.getCount() < max)
				{
					return Short.MAX_VALUE + i;
				}
			}
		}
		
		for (int i = 0; i < 9; ++i)
		{
			if (inventory.getStackInSlot(i).isEmpty())
			{
				return i;
			}
		}
		
		Map<Pair<Integer, Integer>, Boolean> lookup = data.orElseGet(() -> PlayerInventoryHelper.getSlotData(player));
		Pair<Byte, Byte> volume = getVolume(is);
		slotLoop: for (int i = 9; i < 36; ++i)
		{
			int x = (i - 9) % 9;
			int y = (i - 9) / 9;
			if (x + volume.getLeft() > 9 || y + volume.getRight() > 3)
			{
				continue;
			}
			
			Pair<Integer, Integer> pos = Pair.of(x, y);
			if (lookup.containsKey(pos) && lookup.get(pos))
			{
				continue;
			}
			
			for (int dx = x; dx < x + volume.getLeft(); ++dx)
			{
				for (int dy = y; dy < y + volume.getRight(); ++dy)
				{
					pos = Pair.of(dx, dy);
					if (dx >= 9 || dy >= 3 || (lookup.containsKey(pos) && lookup.get(pos)))
					{
					    continue slotLoop;
					}
				}
			}
			
			return i;
		}
		
		return -1;
	}
	
	public static boolean canSlotAccept(ItemStack is, Optional<Map<Pair<Integer, Integer>, Boolean>> data, EntityPlayer player, int slotIndex)
	{
		// Unmanaged
		if (slotIndex < 9 || slotIndex >= 36)
		{
			return true;
		}
		
		ItemStack current = player.inventory.getStackInSlot(slotIndex);
		Map<Pair<Integer, Integer>, Boolean> lookup = data.orElseGet(() -> PlayerInventoryHelper.getSlotData(player));
		Pair<Byte, Byte> volume = getVolume(is);
		int x = (slotIndex - 9) % 9;
		int y = (slotIndex - 9) / 9;
		if (current.isEmpty())
		{
			for (int dx = x; dx < x + volume.getLeft(); ++dx)
			{
				for (int dy = y; dy < y + volume.getRight(); ++dy)
				{
					Pair<Integer, Integer> pos = Pair.of(dx, dy);
					if (dx >= 9 || dy >= 3 || (lookup.containsKey(pos) && lookup.get(pos)))
					{
						return false;
					}
				}
			}
		}
		else
		{
			Pair<Byte, Byte> currentVolume = getVolume(current);
			if (currentVolume.equals(volume))
			{
				return true;
			}
			else
			{
				if (currentVolume.getLeft() >= volume.getLeft() && currentVolume.getRight() >= volume.getRight())
				{
					return true;
				}
				else
				{
					lookup = Maps.newHashMap(lookup);
					for (int dx = x; dx < x + currentVolume.getLeft(); ++dx)
					{
						for (int dy = y; dy < y + currentVolume.getRight(); ++dy)
						{
							lookup.remove(Pair.of(dx, dy));
						}
					}
					
					for (int dx = x; dx < x + volume.getLeft(); ++dx)
					{
						for (int dy = y; dy < y + volume.getRight(); ++dy)
						{
							Pair<Integer, Integer> pos = Pair.of(dx, dy);
							if (dx >= 9 || dy >= 3 || (lookup.containsKey(pos) && lookup.get(pos)))
							{
								return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}

package v0id.exp.player.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import v0id.api.exp.inventory.IWeightProvider;

public class PlayerInventoryHelper
{
	public static final Pair<Byte, Byte> defaultVolume = Pair.of((byte) 1, (byte) 1);
	
	public static Pair<Byte, Byte> getVolume(ItemStack is)
	{
		return is.getItem() instanceof IWeightProvider ? ((IWeightProvider)is.getItem()).provideVolume(is) : IWeightProvider.tryGetRegisteredVolume(is).orElse(defaultVolume);
	}
	
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
				if (getVolume(is) != defaultVolume)
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
	
	public static int findFirstAvailableSlotFor(ItemStack is, Optional<Map<Pair<Integer, Integer>, Boolean>> data, EntityPlayer player)
	{
		InventoryPlayer inventory = player.inventory;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (ItemStack.areItemsEqual(stack, is) && ItemStack.areItemStackTagsEqual(stack, is))
			{
				int max = Math.min(player.openContainer != null ? player.openContainer.getSlot(i).getItemStackLimit(stack) : inventory.getInventoryStackLimit(), stack.getMaxStackSize());
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
		for (int i = 9; i < 36; ++i)
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
						continue;
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
					lookup = new HashMap(lookup);
					for (int dx = x; dx < x + currentVolume.getLeft(); ++dx)
					{
						for (int dy = y; dy < y + currentVolume.getRight(); ++dy)
						{
							lookup.remove(Pair.<Integer, Integer>of(dx, dy));
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

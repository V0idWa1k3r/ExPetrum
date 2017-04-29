package v0id.api.exp.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A simple modifier for {@link IExPPlayer} data.
 * @author V0idWa1k3r
 *
 */
public interface IModifier extends INBTSerializable<NBTTagCompound>, Comparable<IModifier>
{
	float getValue(IExPPlayer player);
	
	Operator getOperator(IExPPlayer player);
	
	/**
	 * For sorting. Return anything.
	 * @return A string that will be used to sort the modifiers in the list
	 */
	String getSortingName();
	
	@Override
	default int compareTo(IModifier o)
	{
		return this.getSortingName().compareTo(o.getSortingName());
	}
}

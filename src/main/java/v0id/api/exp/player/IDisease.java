package v0id.api.exp.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDisease extends INBTSerializable<NBTTagCompound>
{
	void onUpdate(IExPPlayer player);
	
	boolean isActive(IExPPlayer player);
	
	float getProgress(IExPPlayer player);
	
	float getFeelsModifier(IExPPlayer player);
	
	boolean isDirty(IExPPlayer player);
}

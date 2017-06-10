package v0id.api.exp.combat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class SpecialAttack
{
	public static final Map<String, SpecialAttack> registry = Maps.newHashMap();
	public static final List<SpecialAttack> sortedData = Lists.newArrayList();
	
	private int executionTime;
	public final String id;
	private String sortName;
	public final List<IExecuteCondition> executeConditions = Lists.newArrayList();

	public SpecialAttack(ResourceLocation loc)
	{
		this(loc.toString());
	}
	
	public SpecialAttack(String id)
	{
		super();
		this.id = id;
		registry.put(id, this);
		sortedData.add(this);
		this.setSortName(id);
	}

	public int getExecutionTime()
	{
		return this.executionTime;
	}

	public void setExecutionTime(int executionTime)
	{
		this.executionTime = executionTime;
	}
	
	public abstract void onExecutionTick(EntityPlayer player, int progress);
	
	public abstract void onExecutionStart(EntityPlayer player);
	
	public abstract void onExecutionEnd(EntityPlayer player);
	
	public abstract boolean canExecute(EntityPlayer player, WeaponType currentWeapon, EnumWeaponWeight currentWeaponWeight, boolean invokedWithRightClick);
	
	public AttackWrapper wrap()
	{
		return new AttackWrapper(this, this.getExecutionTime());
	}
	
	public String getSortName()
	{
		return this.sortName;
	}

	public void setSortName(String sortName)
	{
		this.sortName = sortName;
		Collections.sort(sortedData, (SpecialAttack l, SpecialAttack r) -> l.getSortName().compareTo(r.getSortName()));
	}

	public static class AttackWrapper implements INBTSerializable<NBTTagCompound>
	{
		public SpecialAttack attackInstance;
		public int executionTime;
		
		public AttackWrapper(SpecialAttack attackInstance, int executionTime)
		{
			super();
			this.attackInstance = attackInstance;
			this.executionTime = executionTime;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound ret = new NBTTagCompound();
			ret.setInteger("time", this.executionTime);
			ret.setString("id", this.attackInstance.id);
			return ret;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.attackInstance = registry.get(nbt.getString("id"));
			this.executionTime = nbt.getInteger("time");
		}
		
		public void onTick(EntityPlayer player)
		{
			// FIXME Fix the threading nightmare you have created instead of dirty hacks like this one!!!
			Optional.of(this.attackInstance).ifPresent(i -> i.onExecutionTick(player, this.executionTime--));
		}
		
		public void onStart(EntityPlayer player)
		{
			this.attackInstance.onExecutionStart(player);
		}
		
		public void onEnd(EntityPlayer player)
		{
			// Can sometimes desync and mess up
			if (this.attackInstance != null)
			{
				this.attackInstance.onExecutionEnd(player);
			}
		}
	}
	
	// Client-side checks go in these. If all are met and canExecute is true then a packet is sent to the server so the server can check canExecute. If that is also true the attack is executed on both client and server.
	@FunctionalInterface
	public static interface IExecuteCondition
	{
		boolean isMet();
	}
}

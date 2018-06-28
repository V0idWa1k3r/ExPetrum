package v0id.api.exp.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import v0id.core.logging.LogLevel;
import v0id.api.exp.ExPApi;
import v0id.api.exp.world.chunk.IExPChunk;

public interface IExPWorld extends INBTSerializable<NBTTagCompound>
{
	/**
	 * Gets the ticks that have happened since the world was created.
	 * <br> Only counts the ticks that have actually happened.
	 * <br> If there is a time skip (a player sleeps for example) this value adjusts to include all 'skipped' ticks that would have happened had the player not slept.
	 * <br> Same goes for the /time command
	 * @return The persistent ticks
	 */
	long getPersistentTicks();
	
	/**
	 * Sets the persistent ticks.
	 * <br> You should not call this method as it is already handled by the base implementation.
	 * @param newValue : the value to set to
	 */
	void setPersistentTicks(long newValue);
	
	/**
	 * Gets the today date. used for convinience mostly.
	 * @return The calendar object for current date.
	 */
	Calendar today();
	
	/**
	 * Gets the current season
	 * @return Thee current season of the world
	 */
	EnumSeason getCurrentSeason();
	
	/**
	 * Gets the direction the wind is blowing. This is a normalized vector.
	 * @return The drection the wind is blowing represented by a normalized vector
	 */
	Vec3d getWindDirection();
	
	/**
	 * Sets the wind direction to a specified vector.
	 * @param vec : a normalized vector to represent the direction of the wind
	 */
	void setWindDirection(Vec3d vec);
	
	/**
	 * Gets the wind strength
	 * @return The strength of the wind
	 */
	float getWindStrength();
	
	/** Sets the wind strength
	 * @param newValue : the value to set
	 */
	void setWindStrength(float newValue);
	
	/**
	 * Gets the generic 'seasonal' temperature modifier
	 * @return The seasonal temperature modifier
	 */
	float getOverhaulTemperature();
	
	/**
	 * Sets the base value for the temperature modifier.
	 * <br> the temperature is calculated as base + seasonal
	 * @param f : the value to set
	 */
	void setOverhaulTemperatureBase(float f);
	
	/**
	 * Gets a humidity of the world, usually season + accumulated based. It is always 1.0 if it is raining
	 * @return The humidity of the world
	 */
	float getOverhaulHumidity();
	
	/**
	 * Sets the base humidity of the world. 
	 * <br> the humidity is calculated as min(1.0, base + accumulated)
	 * @param f : the value to set
	 */
	void setOverhaulHumidity(float f);
	
	/**
	 * Marks the data as 'needs to send some or all of it's data to client'.
	 * <br>Only functional on server.
	 * <br>Default implementation is thread safe
	 */
	void sendNBT();
	
	/**
	 * Marks the data as 'needs to recieve all of it's data from the server'
	 * <br>Only functional on client
	 * <br>Default implementation is thread safe
	 */
	void requestNBT();
	
	/**
	 * Gets the world object this world is associated with
	 * @return - the world this capability is attached to
	 */
	World getWorld();
	
	/**
	 * The onTick event, duh
	 */
	void onTick();

	IExPChunk getChunkDataAt(ChunkPos pos);
	
	static IExPWorld of(World w)
	{
		assert w != null : "Attempted to get data of NULL world!";
		assert w.profiler != null : "World provider is null which should never happen!";
		if (w.provider.getDimensionType() != DimensionType.OVERWORLD)
		{
			ExPApi.apiLogger.log(LogLevel.Error, "Attempted to get the data of %s but the given world is not overworld! It is never ticked and it's data manipulations are useless!", w.toString());
		}
		
		return w.getCapability(ExPWorldCapability.worldCap, null);
	}
}

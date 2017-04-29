package v0id.api.exp.world;

import java.util.Random;

public class TemperatureRange
{
	public float min;
	public float max;

	public TemperatureRange()
	{
		super();
	}
	
	public TemperatureRange(float min, float max)
	{
		super();
		this.setMin(min);
		this.setMax(max);
	}

	public float getMin()
	{
		return this.min;
	}

	public void setMin(float min)
	{
		this.min = min;
	}

	public float getMax()
	{
		return this.max;
	}

	public void setMax(float max)
	{
		this.max = max;
	}
	
	public float getTemperature(Random rand)
	{
		return (float) (this.getMin() + rand.nextDouble() * (this.getMax() - this.getMin()));
	}
}

package v0id.core.util.java;

import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class Size implements Serializable
{
	private int width;
	private int height;
	
	public Size(int w, int h)
	{
		this.width = w;
		this.height = h;
	}
	
	public int getWidhth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
}

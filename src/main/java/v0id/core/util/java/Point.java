package v0id.core.util.java;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class Point implements Serializable
{
	private int x;
	private int y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
}

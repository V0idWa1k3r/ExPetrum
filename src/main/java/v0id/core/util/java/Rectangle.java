package v0id.core.util.java;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Rectangle implements Serializable
{
	private Point location;
	private Size size;
	
	public Rectangle(Point p, Size s)
	{
		this.location = p;
		this.size = s;
	}
	
	public Rectangle(int x, int y, int w, int h)
	{
		this.location = new Point(x, y);
		this.size = new Size(w, h);
	}
	
	public Point getPosition()
	{
		return this.location;
	}
	
	public Size getSize()
	{
		return this.size;
	}
	
	public int getX()
	{
		return this.location.getX();
	}
	
	public int getY()
	{
		return this.location.getY();
	}
	
	public int getWidth()
	{
		return this.size.getWidhth();
	}
	
	public int getHeight()
	{
		return this.size.getHeight();
	}
}

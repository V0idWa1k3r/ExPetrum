package v0id.core.util.java;

import java.awt.Color;
import java.io.Serializable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ColorHEX implements Serializable
{
	public int getHexcode()
	{
		return hexcode;
	}

	final int hexcode;
	
	public ColorHEX(int i)
	{
		this.hexcode = i;
	}
	
	public static ColorHEX FromRGB(float r, float g, float b)
	{
		r *= 255;
		g *= 255;
		b *= 255;
		return new ColorHEX(((int)r << 16) + ((int)g << 8) + (int)b);
	}
	
	public static ColorHEX FromRGB(ColorRGB color)
	{
		return FromRGB(color.R, color.G, color.B);
	}
	
	public static ColorHEX FromHSV(double h, double s, double v)
	{
		return FromRGB(ColorRGB.FromHSV(h, s, v));
	}
	
	public static ColorHEX FromHSV(ColorHSV color)
	{
		return FromHSV(color.H, color.S, color.V);
	}
	
	public static ColorHEX FromSystem(Color color)
	{
		return new ColorHEX((color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue());
	}
}

package v0id.core.util.java;

import java.awt.*;
import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class ColorHSV implements Serializable
{
	public double getH()
	{
		return H;
	}

	public double getS()
	{
		return S;
	}

	public double getV()
	{
		return V;
	}

	public void setH(double h)
	{
		H = h;
	}

	public void setS(double s)
	{
		S = s;
	}

	public void setV(double v)
	{
		V = v;
	}

	double H;
	double S;
	double V;
	
	public ColorHSV(double d0, double d1, double d2)
	{
		this.H = d0;
		this.S = d1;
		this.V = d2;
	}
	
	public ColorHSV(float f0, float f1, float f2)
	{
		this((double)f0, (double)f1, (double)f2);
	}
	
	/**
	 * For GSON
	 */
	public ColorHSV()
	{
		
	}
	
	public static ColorHSV FromRGB(float r, float g, float b)
	{
		float min, max, delta, h, s, v;
		min = Math.min(Math.min(r, g), b);
		max = Math.max(Math.max(r, g), b);
		v = max;
		delta = max - min;
		if (delta < 0.00001f)
		{
			return new ColorHSV(0, 0, v);
		}
		
		if (max > 0)
		{
			s = delta / max;
		}
		else
		{
			return new ColorHSV(0, 0, v);
		}
		
		if (r == max)
		{
			h = (g - b) / delta;
		}
		else
		{
			if (g == max)
			{
				h = 2 + (b - r) / delta;
			}
			else
			{
				h = 4 + (r - g) / delta;
			}
		}
		
		h *= 60;
		if (h < 0)
		{
			h += 360;
		}
		
		return new ColorHSV(h, s, v);
	}
	
	public static ColorHSV FromRGB(ColorRGB color)
	{
		return FromRGB(color.R, color.G, color.B);
	}
	
	public static ColorHSV FromSystem(Color color)
	{
		return FromRGB(ColorRGB.FromSystem(color));
	}
	
	public static ColorHSV FromHEX(int hexcode)
	{
		return FromRGB(ColorRGB.FromHEX(hexcode));
	}
	
	public static ColorHSV FromHEX(ColorHEX color)
	{
		return FromHEX(color.hexcode);
	}
}

package v0id.api.exp.util;

import java.awt.*;
import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class ColorRGB implements Serializable
{
	public float getR()
	{
		return R;
	}

	public void setR(float r)
	{
		R = r;
	}

	public float getG()
	{
		return G;
	}

	public void setG(float g)
	{
		G = g;
	}

	public float getB()
	{
		return B;
	}

	public void setB(float b)
	{
		B = b;
	}

	float R;
	float G;
	float B;
	
	/**
	 * Gson
	 */
	public ColorRGB()
	{
		
	}
	
	public ColorRGB(float f0, float f1, float f2)
	{
		this.R = f0;
		this.G = f1;
		this.B = f2;
	}
	
	public ColorRGB(double d0, double d1, double d2)
	{
		this((float)d0, (float)d1, (float)d2);
	}
	
	public ColorRGB(byte r, byte g, byte b)
	{
		this(((float)r / 255), ((float)g / 255), ((float)b / 255));
	}
	
	public ColorRGB(int r, int g, int b)
	{
		this(((float)r / 255), ((float)g / 255), ((float)b / 255));
	}
	
	public static ColorRGB FromHSV(double h, double s, double v)
	{
		double hh, p, q, t, ff;
		byte b;
		
		if (s == 0)
		{
			return new ColorRGB(v, v, v);
		}
		
		hh = (h % 360) / 60;
		b = (byte) hh;
		ff = hh - b;
		p = v * (1 - s);
		q = v * (1 - s * ff);
		t = v * (1 - s * (1 - ff));
		if (b == 0)
		{
			return new ColorRGB(v, t, p);
		}
		
		if (b == 1)
		{
			return new ColorRGB(q, v, p);
		}
		
		if (b == 2)
		{
			return new ColorRGB(p, v, t);
		}
		
		if (b == 3)
		{
			return new ColorRGB(p, q, v);
		}
		
		if (b == 4)
		{
			return new ColorRGB(t, p, v);
		}
		
		return new ColorRGB(v, p, q);
	}
	
	public static ColorRGB FromHSV(ColorHSV color)
	{
		return FromHSV(color.H, color.S, color.V);
	}
	
	public static ColorRGB FromSystem(Color color)
	{
		return new ColorRGB(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public static ColorRGB FromHEX(int hex)
	{
		int r = (hex & 0xff0000) >> 16;
		int g = (hex & 0xff00) >> 8;
		int b = hex & 0xff;
		return new ColorRGB(r, g, b);
	}
	
	public static ColorRGB FromHEX(ColorHEX color)
	{
		return FromHEX(color.hexcode);
	}
}

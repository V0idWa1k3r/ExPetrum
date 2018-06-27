package v0id.core.util;

import v0id.core.VoidApi;

// Server-safe translator so reflection on classes that implement vanilla's I18n does not crash the game.
@SuppressWarnings("unused")
public class I18n
{
	public static String format(String unlocalizedDesc, Object... objects)
	{
		return VoidApi.proxy.format(unlocalizedDesc, objects);
	}
}

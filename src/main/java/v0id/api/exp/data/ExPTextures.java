package v0id.api.exp.data;

import net.minecraft.util.ResourceLocation;
import v0id.api.core.markers.StaticStorage;
import v0id.api.exp.player.EnumPlayerProgression;

@StaticStorage
public class ExPTextures
{
	public static ResourceLocation[] PLAYER_HUD = new ResourceLocation[EnumPlayerProgression.values().length];
	
	public static ResourceLocation WEATHER = new ResourceLocation(ExPRegistryNames.modid, "textures/misc/weather.png");
	
	static
	{
		int i = 0;
		for (EnumPlayerProgression progression : EnumPlayerProgression.values())
		{
			PLAYER_HUD[i++] = new ResourceLocation(ExPRegistryNames.modid, String.format("textures/ui/player_%s.png", progression.name().toLowerCase()));
		}
	}
}

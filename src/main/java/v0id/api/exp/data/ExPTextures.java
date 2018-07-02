package v0id.api.exp.data;

import net.minecraft.util.ResourceLocation;
import v0id.core.markers.StaticStorage;
import v0id.api.exp.player.EnumPlayerProgression;

@StaticStorage
public class ExPTextures
{
	public static final ResourceLocation[] PLAYER_HUD = new ResourceLocation[EnumPlayerProgression.values().length];
	
	public static final ResourceLocation WEATHER = new ResourceLocation(ExPRegistryNames.modid, "textures/misc/weather.png");

	public static final ResourceLocation PARTICLES = new ResourceLocation(ExPRegistryNames.modid, "textures/misc/particles.png");

	public static final ResourceLocation
            entityChickenWildGeneric                                                                           = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/chicken/wild_all.png"),
            entityChickenWildColor                                                                             = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/chicken/wild_overlay.png"),
            entityChickenWildMale                                                                              = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/chicken/wild_male.png");

	public static final ResourceLocation
            guiCampfire                                                                                        	= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/campfire.png"),
            guiInv9                                                                                       		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/inv_9.png"),
            guiInv4                                                                                       		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/inv_4.png"),
            guiInv1                                                                         	               	= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/inv_1.png"),
            guiBtns                                                                        	               		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/buttons.png"),
            guiPotteryStation                                                                        	   		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/pottery_station.png"),
            guiForge                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/forge.png"),
            guiQuern                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/quern.png"),
            guiAnvil                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/anvil.png"),
            guiNone	                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/none.png");

    static
	{
		int i = 0;
		for (EnumPlayerProgression progression : EnumPlayerProgression.values())
		{
			PLAYER_HUD[i++] = new ResourceLocation(ExPRegistryNames.modid, String.format("textures/ui/player_%s.png", progression.name().toLowerCase()));
		}
	}
}

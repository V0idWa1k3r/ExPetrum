package v0id.api.exp.data;

import net.minecraft.util.ResourceLocation;
import v0id.api.exp.player.EnumPlayerProgression;

public class ExPTextures
{
	public static final ResourceLocation[] PLAYER_HUD = new ResourceLocation[EnumPlayerProgression.values().length];
	
	public static final ResourceLocation WEATHER = new ResourceLocation(ExPRegistryNames.modid, "textures/misc/weather.png");

	public static final ResourceLocation PARTICLES = new ResourceLocation(ExPRegistryNames.modid, "textures/misc/particles.png");

	public static final ResourceLocation
            entityChickenWildGeneric                                                                            = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/chicken/wild_all.png"),
            entityChickenWildColor                                                                              = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/chicken/wild_overlay.png"),
            entityChickenWildMale                                                                               = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/chicken/wild_male.png"),
			entityCowGeneric                                                                                    = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/cow/all.png"),
			entityCowColor                                                                                      = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/cow/overlay.png"),
			entityCowMale                                                                                       = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/cow/male.png"),
			entityCowFemale                                                                                     = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/cow/female.png"),
			entitySheepGeneric                                                                                  = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/sheep/all.png"),
			entitySheepColor                                                                                    = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/sheep/overlay.png"),
			entitySheepMale                                                                                     = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/sheep/male.png"),
			entityPigGeneric                                                                                    = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/pig/all.png"),
			entityPigColor                                                                                      = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/pig/overlay.png"),
			entityPigMale                                                                                       = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/pig/male.png"),
			entityPigFemale                                                                                     = new ResourceLocation(ExPRegistryNames.modid, "textures/entities/pig/female.png");

	public static final ResourceLocation[] AGES = new ResourceLocation[EnumPlayerProgression.values().length];

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
            guiNone	                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/none.png"),
            guiCrucible                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/crucible.png"),
            guiInv9Sq                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/inv_9_sq.png"),
            guiBarrel                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/barrel.png"),
            guiBloomery                                                                        	  		 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/bloomery.png"),
            guiInv18                                                                       	  			 		= new ResourceLocation(ExPRegistryNames.modid, "textures/gui/inv_18.png"),
			guiJEI                                                                                              = new ResourceLocation(ExPRegistryNames.modid, "textures/gui/jei/guis.png"),
			guiMechanicalQuern                                                                                  = new ResourceLocation(ExPRegistryNames.modid, "textures/gui/mechanical_quern.png"),
			guiMechanicalPotteryStation                                                                         = new ResourceLocation(ExPRegistryNames.modid, "textures/gui/mechanical_pottery_station.png"),
			guiBlastFurnace				                                                                        = new ResourceLocation(ExPRegistryNames.modid, "textures/gui/blast_furnace.png");

    static
	{
		int i = 0;
		for (EnumPlayerProgression progression : EnumPlayerProgression.values())
		{
		    AGES[i] = new ResourceLocation(ExPRegistryNames.modid, String.format("textures/ui/age_%s.png", progression.name().toLowerCase()));
			PLAYER_HUD[i++] = new ResourceLocation(ExPRegistryNames.modid, String.format("textures/ui/player_%s.png", progression.name().toLowerCase()));
		}
	}
}

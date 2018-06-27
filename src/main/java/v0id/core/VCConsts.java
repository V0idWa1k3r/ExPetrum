package v0id.core;

import v0id.core.markers.StaticStorage;

@SuppressWarnings("WeakerAccess")
@StaticStorage
public class VCConsts 
{
	public static final String 
		mcVersion																						= "1.12";
	
	public static final int
		versionGlobal 																					= 0,
		versionApi 																					= 0,
		versionMod																						= 0,
		versionBuild																					= 0;

	public static final String 
		modid 																							= "voidapi",
		modname																							= "V01dAP1",
		version																							= mcVersion + '-' + versionGlobal + "." + versionApi + '.' + versionMod + "." + versionBuild;
}

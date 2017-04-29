package v0id.api.exp.player;

public enum EnumPlayerProgression
{
	STONE_AGE,			// Player starts with this one. Stone tools and stuff are available.
	CHALCOLITHIC,		// Also known as the copper age. Achieved by getting first copper tools
	BRONZE_AGE,			// Achieved by creating first bronze age
	IRON_AGE,			// Achieved by making first iron
	EARLY_ROTARY_AGE,	// Achieved by getting first rotary power
	STEEL_AGE,			// Achieved by getting first steel
	
	// ANYTHING BELOW HERE IS NOT CURRENTLY IMPLEMENTED!
	// It is however intended.
	
	STEAM_AGE,			// Achieved by creating first steam boiler
	INDUSTRIAL_AGE,		// Achieved by creating steam-processing machines and rotary steam power
	ELECTRICAL_AGE,		// Achieved by creating first electricity
	MACHINE_AGE,		// Achieved by creating electrical processing structures
	ROBOTICS_AGE,		// Achieved by creating your first robot
	SPACE_AGE;			// Achieved by launching a satellite
}

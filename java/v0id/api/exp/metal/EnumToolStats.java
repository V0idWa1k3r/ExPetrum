package v0id.api.exp.metal;

import net.minecraft.util.IStringSerializable;
import v0id.api.exp.player.EnumPlayerProgression;

public enum EnumToolStats implements IStringSerializable
{
	STONE(null, 128, 1, 3, 0.8F, 2.0F, EnumPlayerProgression.STONE_AGE),
	COPPER(EnumMetal.COPPER, 384, 2, 4, 0.8F, 4.0F, EnumPlayerProgression.CHALCOLITHIC),
	BRONZE(EnumMetal.BRONZE, 768, 3, 5, 1F, 5.0F, EnumPlayerProgression.BRONZE_AGE),
	BISMUTH_BRONZE(EnumMetal.BISMUTH_BRONZE, 844, 3, 5, 0.9F, 4.6F, EnumPlayerProgression.BRONZE_AGE),
	LEADED_BRONZE(EnumMetal.HIGH_LEADED_BRONZE, 576, 3, 6, 1.0F, 4.8F, EnumPlayerProgression.BRONZE_AGE),
	MANGANESE_BRONZE(EnumMetal.MANGANESE_BRONZE, 1340, 3, 3, 0.8F, 3.0F, EnumPlayerProgression.BRONZE_AGE),
	ROSE_ALLOY(EnumMetal.ROSE_ALLOY, 684, 3, 5, 1.1F, 5.0F, EnumPlayerProgression.BRONZE_AGE),
	IRON(EnumMetal.IRON, 1536, 5, 6, 1.0F, 6.0F, EnumPlayerProgression.IRON_AGE),
	STEEL(EnumMetal.STEEL, 3072, 7, 8, 1.0F, 7.0F, EnumPlayerProgression.STEEL_AGE),
	TOOL_STEEL(EnumMetal.TOOL_STEEL, 9216, 7, 8, 1.0F, 8.0F, EnumPlayerProgression.MACHINE_AGE);
	
	private EnumToolStats(EnumMetal material, int durability, int harvestLevel, float damage, float weaponDamageMultiplier, float efficiency, EnumPlayerProgression associatedProgression)
	{
		this.material = material;
		this.durability = durability;
		this.harvestLevel = harvestLevel;
		this.damage = damage;
		this.weaponDamageMultiplier = weaponDamageMultiplier;
		this.efficiency = efficiency;
		this.associatedProgression = associatedProgression;
	}
	
	public EnumMetal getMaterial()
	{
		return this.material;
	}

	public int getDurability()
	{
		return this.durability;
	}

	public int getHarvestLevel()
	{
		return this.harvestLevel;
	}

	public float getDamage()
	{
		return this.damage;
	}

	public float getWeaponDamageMultiplier()
	{
		return this.weaponDamageMultiplier;
	}

	public EnumPlayerProgression getAssociatedProgression()
	{
		return this.associatedProgression;
	}

	private final EnumMetal material;
	private final int durability;
	private final int harvestLevel;
	private final float damage;
	private final float weaponDamageMultiplier;
	private final float efficiency;
	private final EnumPlayerProgression associatedProgression;
	
	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

	public float getEfficiency()
	{
		return this.efficiency;
	}
}

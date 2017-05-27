package v0id.api.exp.metal;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public enum EnumMetal
{
	// Pure metals
	TIN(0xadadad, false, null, "tin"),
	BARIUM(0x4e5661, false, null, "barium"),
	COPPER(0xc2431a, true, null, "copper"),
	ALUMINIUM(0x9fa6b0, false, null, "aluminium", "aluminum"),
	CHROMIUM(0x7c8e92, false, null, "chromium"),
	COBALT(0x5c5b6b, false, null, "cobalt"),
	TANTALUM(0x6c7b88, false, null, "tantalum"),
	MAGNESIUM(0x6f6856, false, null, "magnesium"),
	LEAD(0x4c4a4b, false, null, "lead"),
	GOLD(0xffde5c, false, null, "gold"),
	IRON(0xc0c1b9, true, null, "iron"),
	TITANIUM(0x8c8c8e, false, null, "titanium"),
	MOLYBDENUM(0x635d5d, false, null, "molybdenum"),
	NICKEL(0xbcb6a0, false, null, "nickel", "ferrous"),
	MANGANESE(0xa79e9f, false, null, "manganese"),
	TUNGSTEN(0xdbd9cd, false, null, "tungsten", "wolfram", "wolframium"),
	PLATINUM(0xffffff, false, null, "platinum", "shiny"),
	ZINC(0x555555, false, null, "zinc"),
	URANIUM(0x313131, false, null, "uranium", "yellorium"),
	BISMUTH(0x564b45, false, null, "bismuth"),
	SILVER(0xbcbcbc, false, null, "silver"),
	IRIDIUM(0x866c85, false, null, "iridium"),
	OSMIUM(0x90a5b1, false, null, "osmium"),
	
	// Alloys
	BRONZE(0x977035, true, new AlloyComposition(Pair.of(COPPER, Pair.of(0.8F, 0.9F)), Pair.of(TIN, Pair.of(0.1F, 0.2F))), "bronze"),
	BISMUTH_BRONZE(0xa9998a, true, new AlloyComposition(Pair.of(COPPER, Pair.of(0.85F, 0.9F)), Pair.of(TIN, Pair.of(0.05F, 0.1F)), Pair.of(ZINC, Pair.of(0F, 0.1F)), Pair.of(BISMUTH, Pair.of(0.01F, 0.05F))), "bronze", "bronzeBismuth"),
	HIGH_LEADED_BRONZE(0x5a413a, true, new AlloyComposition(Pair.of(COPPER, Pair.of(0.7F, 0.8F)), Pair.of(TIN, Pair.of(0.05F, 0.15F)), Pair.of(LEAD, Pair.of(0.15F, 0.25F))), "bronze", "bronzeLead", "bronzeHighLead"),
	BRASS(0x9b835d, false, new AlloyComposition(Pair.of(COPPER, Pair.of(0.6F, 0.7F)), Pair.of(ZINC, Pair.of(0.3F, 0.4F))), "brass"),
	MANGANESE_BRONZE(0xd4bfa4, true, new AlloyComposition(Pair.of(COPPER, Pair.of(0.55F, 0.65F)), Pair.of(ZINC, Pair.of(0.25F, 0.4F)), Pair.of(MANGANESE, Pair.of(0.01F, 0.05F))), "bronze", "bronzeManganese"),
	ALUMINUM_BRONZE(0xc3a871, false, new AlloyComposition(Pair.of(COPPER, Pair.of(0.8F, 0.9F)), Pair.of(ALUMINIUM, Pair.of(0.1F, 0.15F))), "bronze", "bronzeAluminium", "bronzeAluminum"),
	STERLING_SILVER(0xeeede9, false, new AlloyComposition(Pair.of(SILVER, Pair.of(0.9F, 0.95F)), Pair.of(COPPER, Pair.of(0.05F, 0.1F))), "silver", "silverSterling"),
	ELECTRUM(0xffc453, false, new AlloyComposition(Pair.of(SILVER, Pair.of(0.2F, 0.8F)), Pair.of(GOLD, Pair.of(0.2F, 0.8F))), "electrum"),
	PLATINUM_STERLING(0xeeeeee, false, new AlloyComposition(Pair.of(STERLING_SILVER, Pair.of(0.95F, 0.99F)), Pair.of(PLATINUM, Pair.of(0.01F, 0.05F))), "platinum", "shiny", "platinumSterling"),
	INVAR(0x9a9581, false, new AlloyComposition(Pair.of(IRON, Pair.of(0.5F, 0.7F)), Pair.of(NICKEL, Pair.of(0.3F, 0.5F))), "invar"),
	
	// Special cases : steel
	CAST_IRON(0x222222, false, null, "steel", "steelCrude"),
	PIG_IRON(0xb3a3a3, false, null, "steel", "steelWeak"),
	STEEL(0x6a6a6a, true, null, "steel"),
	HSLA_STEEL(0x717171, false, null, "steel", "steelHSLA", "HSLA", "steelHighStrengthLowAlloy"),
	STAINLESS_STEEL(0x7f7f7f, false, null, "steel", "steelStainless"),
	TOOL_STEEL(0x666666, true, null, "steel", "steelTool"),
	
	SOLDER(0x91847f, false, new AlloyComposition(Pair.of(TIN, Pair.of(0.7F, 0.9F)), Pair.of(LEAD, Pair.of(0.1F, 0.3F))), "solder"),
	ALNICO(0x854e49, false, new AlloyComposition(Pair.of(ALUMINIUM, Pair.of(0.05F, 0.15F)), Pair.of(NICKEL, Pair.of(0.15F, 0.25F)), Pair.of(COBALT, Pair.of(0.05F, 0.25F)), Pair.of(IRON, Pair.of(0.35F, 0.75F))), "alnico"),
	DURALUMIN(0x9db59d, false, new AlloyComposition(Pair.of(ALUMINIUM, Pair.of(0.9F, 0.95F)), Pair.of(COPPER, Pair.of(0.05F, 0.1F))), "duralumin"),
	ROSE_ALLOY(0x705860, true, new AlloyComposition(Pair.of(BISMUTH, Pair.of(0.4F, 0.5F)), Pair.of(LEAD, Pair.of(0.25F, 0.4F)), Pair.of(TIN, Pair.of(0.1F, 0.25F))), "alloyRose"),
	NICHROME(0xafb8b3, false, new AlloyComposition(Pair.of(NICKEL, Pair.of(0.7F, 0.8F)), Pair.of(CHROMIUM, Pair.of(0.2F, 0.3F))), "nichrome"),
	VITALLIUM(0x161841, false, new AlloyComposition(Pair.of(COBALT, Pair.of(0.6F, 0.65F)), Pair.of(CHROMIUM, Pair.of(0.25F, 0.3F)), Pair.of(MOLYBDENUM, Pair.of(0.05F, 0.1F))), "vitallium");
	
	EnumMetal(int color, boolean canToolsBeMadeOf, AlloyComposition composition, String... oreDictNames)
	{
		this.color = color;
		this.isToolMaterial = canToolsBeMadeOf;
		this.composition = composition;
		this.oreDictPostfix = oreDictNames;
	}
	
	public int getColor()
	{
		return this.color;
	}

	public boolean isToolMaterial()
	{
		return this.isToolMaterial;
	}

	public AlloyComposition getComposition()
	{
		return this.composition;
	}

	public String[] getOreDictPostfix()
	{
		return this.oreDictPostfix;
	}

	private final int color;
	private final boolean isToolMaterial;
	private final AlloyComposition composition;
	private final String[] oreDictPostfix;
	
	public static class AlloyComposition
	{
		public static final List<Pair<EnumMetal, Pair<Float, Float>>> compositionData = Lists.newArrayList();
		
		public AlloyComposition(Pair<EnumMetal, Pair<Float, Float>>...pairs)
		{
			compositionData.addAll(Arrays.asList(pairs));
		}
	}
}

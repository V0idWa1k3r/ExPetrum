package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

import java.util.stream.Stream;

public class ItemGeneric extends Item implements IWeightProvider, IOreDictEntry
{
    public enum EnumGenericType
    {
        CLAY("clay", 0.05F, IWeightProvider.DEFAULT_VOLUME, "clay", "itemClay"),
        TWINE("twine", 0.001F, IWeightProvider.DEFAULT_VOLUME, "twine", "plantTwine"),
        PLANT_FIBER("plant_fiber", 0.004F, IWeightProvider.DEFAULT_VOLUME, "fiber", "plantFiber"),
        LIGNITE("lignite", 0.05F, IWeightProvider.DEFAULT_VOLUME, "coal"),
        BITUMINOUS_COAL("bituminous_coal", 0.05F, IWeightProvider.DEFAULT_VOLUME, "coal"),
        ANTHRACITE("anthracite", 0.05F, IWeightProvider.DEFAULT_VOLUME, "coal"),
        ASH("ash", 0F, IWeightProvider.DEFAULT_VOLUME, "ash", "dustAsh"),
        CHARCOAL("charcoal", 0.02F, IWeightProvider.DEFAULT_VOLUME, "coal", "charcoal"),
        KAOLIN("kaolin", 0.1F, IWeightProvider.DEFAULT_VOLUME, "kaolin", "oreKaolin"),
        KAOLIN_POWDER("kaolin_powder", 0.02F, IWeightProvider.DEFAULT_VOLUME, "powderKaolin", "dustKaolin"),
        KAOLIN_BRICK("kaolin_brick", 0.1F, IWeightProvider.DEFAULT_VOLUME, "brickKaolin", "ingotKaolin"),
        FIRE_CLAY("fire_clay", 0.15F, IWeightProvider.DEFAULT_VOLUME, "fireClay", "clayFire"),
        FIRE_BRICK("fire_brick", 0.15F, IWeightProvider.DEFAULT_VOLUME, "fireBrick", "brickFire", "ingotFired"),
        FLINT_POWDER("flint_powder", 0.01F, IWeightProvider.DEFAULT_VOLUME, "powderFlint", "dustFlint"),
        FLUX("flux", 0.01F, IWeightProvider.DEFAULT_VOLUME, "flux"),
        COPPER_COATED_FIRE_BRICK("copper_coated_fire_brick", 0.3F, IWeightProvider.DEFAULT_VOLUME),
        HAY("hay", 0F, IWeightProvider.DEFAULT_VOLUME, "hay", "itemHay"),
        HIDE("hide", 2F, IWeightProvider.DEFAULT_VOLUME, "hide"),
        WOOL("wool", 0.5F, IWeightProvider.DEFAULT_VOLUME, "rawWool", "woolRaw"),
        COPPER_RIM("copper_rim", 0.3F, IWeightProvider.DEFAULT_VOLUME, "rimCopper"),
        SOAKED_HIDE("soaked_hide", 3F, IWeightProvider.DEFAULT_VOLUME, "hideSoaked"),
        PREPARED_HIDE("prepared_hide", 2F, IWeightProvider.DEFAULT_VOLUME, "hidePrepared"),
        LEATHER("leather", 2F, IWeightProvider.DEFAULT_VOLUME, "leather"),
        LEATHER_STRAPS("leather_straps", 1F, IWeightProvider.DEFAULT_VOLUME, "strapsLeather"),
        SOAKED_WOOL("soaked_wool", 0.8F, IWeightProvider.DEFAULT_VOLUME, "woolSoaked"),
        COPPER_PINS("copper_pins", 0.01F, IWeightProvider.DEFAULT_VOLUME, "pinsCopper"),
        CARDED_WOOL("carded_wool", 0.2F, IWeightProvider.DEFAULT_VOLUME, "woolCarded"),
        WOOL_YARN("wool_yarn", 0.1F, IWeightProvider.DEFAULT_VOLUME, "string", "yarn", "yarnWool", "fiber"),
        WOVEN_CLOTH("woven_cloth", 0.4F, IWeightProvider.DEFAULT_VOLUME, "wool", "woolWhite", "cloth"),
        KNITTING_NEEDLE("knitting_needle", 0.05F, IWeightProvider.DEFAULT_VOLUME, "knittingNeedle"),
        IRON_BLOOM("iron_bloom", 1F, IWeightProvider.DEFAULT_VOLUME, "bloomIron"),
        REFINED_IRON_BLOOM("refined_iron_bloom", 0.5F, IWeightProvider.DEFAULT_VOLUME, "bloomIronRefined"),
        TREATED_STICK("treated_stick", 0.01F, IWeightProvider.DEFAULT_VOLUME, "stickWood", "stickTreated", "stickTreatedWood", "stickWoodTreated"),
        LOCK("lock", 0.1F, IWeightProvider.DEFAULT_VOLUME, "lockIron"),
        NAILS("nails", 0.001F, IWeightProvider.DEFAULT_VOLUME, "nailsIron"),
        SMALL_GEAR("small_gear", 0.01F, IWeightProvider.DEFAULT_VOLUME, "gearIronSmall"),
        MEDIUM_GEAR("medium_gear", 0.08F, IWeightProvider.DEFAULT_VOLUME, "gearIronMedium"),
        LARGE_GEAR("large_gear", 0.2F, IWeightProvider.DEFAULT_VOLUME, "gearIron"),
        GEAR_ASSEMBLY("gear_assembly", 0.5F, IWeightProvider.DEFAULT_VOLUME, "gearAssembly"),
        WATER_WHEEL_BLADE("water_wheel_blade", 2.5F, Pair.of((byte)2, (byte)1), "bladeWaterWheel"),
        WINDMILL_BLADE("windmill_blade", 5F, Pair.of((byte)1, (byte)2), "bladeWindmill"),
        SAW_BLADE("saw_blade", 10F, Pair.of((byte)2, (byte)2), "bladeSawmill"),
        CHARRED_BONE("charred_bone", 0.1F, IWeightProvider.DEFAULT_VOLUME, "bone"),
        BONE_ASH("bone_ash", 0.05F, IWeightProvider.DEFAULT_VOLUME, "ashBone", "boneAsh"),
        IRON_STICK("iron_stick", 0.1F, IWeightProvider.DEFAULT_VOLUME, "stickIron", "rodIron"),
        WOOD_PULP("wood_pulp", 0.01F, IWeightProvider.DEFAULT_VOLUME, "pulpWood", "sawdust", "dustWood"),
        PRESSED_WOOD_PULP("pressed_pulp", 0.01F, IWeightProvider.DEFAULT_VOLUME, "compressedPulp", "platePulpWood", "plateSawdust", "plateWood"),
        ROCK_SALT("rock_salt", 0.3F, IWeightProvider.DEFAULT_VOLUME, "saltRock", "oreSalt"),
        SALT("salt", 0.1F, IWeightProvider.DEFAULT_VOLUME, "salt", "dustSalt", "foodSalt");

        EnumGenericType(String name, float weight, Pair<Byte, Byte> volume, String... oreDictNames)
        {
            this.name = name;
            this.weight = weight;
            this.volume = volume;
            this.oreDictNames = oreDictNames;
        }

        private String name;
        private float weight;
        private Pair<Byte, Byte> volume;
        private String[] oreDictNames;

        public String getName()
        {
            return name;
        }
    }

    public ItemGeneric()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemGeneric));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscItems);
        this.setHasSubtypes(true);
    }

    @Override
    public void registerOreDictNames()
    {
        Stream.of(EnumGenericType.values()).forEach(s -> Stream.of(s.oreDictNames).forEach(name -> OreDictionary.registerOre(name, new ItemStack(this, 1, s.ordinal()))));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return EnumGenericType.values()[item.getMetadata()].weight;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return EnumGenericType.values()[item.getMetadata()].volume;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (int i = 0; i < EnumGenericType.values().length; ++i)
        {
            subItems.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + EnumGenericType.values()[stack.getMetadata()].getName();
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack)
    {
        EnumGenericType type = EnumGenericType.values()[itemStack.getMetadata()];
        return type == EnumGenericType.LIGNITE ? 1000 : type == EnumGenericType.BITUMINOUS_COAL || type == EnumGenericType.CHARCOAL ? 1600 : type == EnumGenericType.ANTHRACITE ? 3200 : super.getItemBurnTime(itemStack);
    }
}

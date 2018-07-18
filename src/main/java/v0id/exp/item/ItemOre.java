package v0id.exp.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.ExPetrum;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class ItemOre extends Item implements IWeightProvider, IOreDictEntry, IMeltableMetal
{
    public ItemOre()
    {
        super();
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemOre));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabOres);
        this.setHasSubtypes(true);
    }

    @Override
    public void registerOreDictNames()
    {
        Stream.of(EnumOre.values()).forEach(this::registerOreDict);
    }

    public void registerOreDict(EnumOre metal)
    {
        String s = metal.name().toLowerCase();
        OreDictionary.registerOre("ore" + Character.toUpperCase(s.charAt(0)) + s.substring(1), new ItemStack(this, 1, metal.ordinal()));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.05F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (EnumOre ore : EnumOre.values())
        {
            items.add(new ItemStack(this, 1, ore.ordinal()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + EnumOre.values()[stack.getMetadata()].getName();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        EntityPlayer player = ExPetrum.proxy.getClientPlayer();
        if (player != null)
        {
            IExPPlayer playerData = IExPPlayer.of(player);
            if (playerData.getProgressionStage().ordinal() >= EnumPlayerProgression.INDUSTRIAL_AGE.ordinal())
            {
                tooltip.add(EnumOre.values()[stack.getMetadata()].getFormula());
            }
        }
    }

    @Override
    public EnumMetal getMetal(ItemStack is)
    {
        return EnumOre.values()[is.getMetadata()].getMeltsInto();
    }

    @Override
    public float getMeltingTemperature(ItemStack is)
    {
        return EnumOre.values()[is.getMetadata()].getMeltingTemperature();
    }

    @Override
    public int getMetalAmound(ItemStack is)
    {
        return 10 * is.getCount();
    }
}

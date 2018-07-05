package v0id.exp.registry;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.*;
import v0id.exp.item.tool.*;

import java.util.Arrays;
import java.util.List;

public class ExPItemsRegistry extends AbstractRegistry
{
    public static ExPItemsRegistry instance;
    public static List<Item> registryEntries;
	
	public ExPItemsRegistry()
	{
		super();
		instance = this;
	}

	public void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        registryEntries = Lists.newArrayList();
        registryEntries.addAll(Arrays.asList(
                new ItemRock(),
                new ItemStick(),
                new ItemToolHead(),
                new ItemSeeds(),
                new ItemFood(),
                new ItemIngot(),
                new ItemKnife(),
                new ItemPickaxe(),
                new ItemAxe(),
                new ItemShovel(),
                new ItemHoe(),
                new ItemSword(),
                new ItemScythe(),
                new ItemBattleaxe(),
                new ItemHammer(),
                new ItemSpear(),
                new ItemWateringCan(),
                new ItemGardeningSpade(),
                new ItemBasket(),
                new ItemSaw(),
                new ItemGeneric(),
                new ItemChisel(),
                new ItemOre(),
                new ItemMold(false),
                new ItemMold(true),
                new ItemPottery(),
                new ItemFireStarter(),
                new ItemWoodenBucket(),
                new ItemGrindstone(),
                new ItemMetalGeneric(),
                new ItemWoolCard()
        ));

        for (int i = 2; i < EnumToolStats.values().length; ++i)
        {
            registryEntries.add(new ItemTuyere(EnumToolStats.values()[i]));
        }

        registryEntries.addAll(Arrays.asList(

        ));

        registryEntries.forEach(registry::register);
    }
}

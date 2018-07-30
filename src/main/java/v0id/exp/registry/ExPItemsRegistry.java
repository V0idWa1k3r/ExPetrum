package v0id.exp.registry;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.item.EnumArmorStats;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.*;
import v0id.exp.item.tool.*;

public class ExPItemsRegistry extends AbstractRegistry
{
    public static ExPItemsRegistry instance;
	
	public ExPItemsRegistry()
	{
		super();
		instance = this;
	}

	public void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        event.getRegistry().registerAll(
                new ItemRock(),
                new ItemStick(),
                new ItemToolHead(),
                new ItemSeeds(),
                new ItemFood(),
                new ItemIngot(),
                new ItemBasket(),
                new ItemGeneric(),
                new ItemOre(),
                new ItemMold(false),
                new ItemMold(true),
                new ItemPottery(),
                new ItemFireStarter(),
                new ItemWoodenBucket(),
                new ItemGrindstone(),
                new ItemMetalGeneric(),
                new ItemWoolCard()
        );

        for (EnumToolStats stats : EnumToolStats.values())
        {
            event.getRegistry().registerAll(
                    new ItemKnife(stats),
                    new ItemPickaxe(stats),
                    new ItemAxe(stats),
                    new ItemShovel(stats),
                    new ItemHoe(stats),
                    new ItemSword(stats),
                    new ItemScythe(stats),
                    new ItemBattleaxe(stats),
                    new ItemHammer(stats),
                    new ItemSpear(stats),
                    new ItemWateringCan(stats),
                    new ItemGardeningSpade(stats),
                    new ItemSaw(stats),
                    new ItemChisel(stats)
            );
        }

        for (int i = 2; i < EnumToolStats.values().length; ++i)
        {
            event.getRegistry().register(new ItemTuyere(EnumToolStats.values()[i]));
        }

        event.getRegistry().register(new ItemFlintAndIron());
        for (EnumArmorStats armor : EnumArmorStats.values())
        {
            event.getRegistry().register(new ItemArmor(EntityEquipmentSlot.HEAD, armor));
            event.getRegistry().register(new ItemArmor(EntityEquipmentSlot.CHEST, armor));
            event.getRegistry().register(new ItemArmor(EntityEquipmentSlot.LEGS, armor));
            event.getRegistry().register(new ItemArmor(EntityEquipmentSlot.FEET, armor));
        }

        event.getRegistry().registerAll(
            new ItemArmorFramework(),
            new ItemBackpack(false),
            new ItemBackpack(true),
            new ItemFluidBottle(),
            new ItemGem()
        );
    }
}

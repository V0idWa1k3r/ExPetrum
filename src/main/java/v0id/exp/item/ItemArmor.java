package v0id.exp.item;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.EnumArmorStats;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ItemArmor extends net.minecraft.item.ItemArmor implements IWeightProvider, IOreDictEntry
{
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public final EnumArmorStats stats;
    public static final Map<Pair<EntityEquipmentSlot, EnumArmorStats>, ItemArmor> items = Maps.newHashMap();

    public ItemArmor(EntityEquipmentSlot equipmentSlotIn, EnumArmorStats stats)
    {
        super(ExPMisc.armorMaterialExPetrum, 0, equipmentSlotIn);
        this.stats = stats;
        this.setMaxDamage(this.stats.durability);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemArmor + "_" + this.stats.name + "_" + (this.getEquipmentSlot() == EntityEquipmentSlot.HEAD ? "helmet" : this.getEquipmentSlot() == EntityEquipmentSlot.CHEST ? "chestplate" : this.getEquipmentSlot() == EntityEquipmentSlot.LEGS ? "leggings" : "boots")));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
        items.put(Pair.of(equipmentSlotIn, stats), this);
    }

    @Override
    public void registerOreDictNames()
    {
        OreDictionary.registerOre((this.getEquipmentSlot() == EntityEquipmentSlot.HEAD ? "helmet" : this.getEquipmentSlot() == EntityEquipmentSlot.FEET ? "boots" : this.getEquipmentSlot() == EntityEquipmentSlot.LEGS ? "leggings" : "chestplate") + Character.toUpperCase(this.stats.name.charAt(0)) + this.stats.name.substring(1), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return this.stats.weight * this.getArmorTypeMultiplier();
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return this.getEquipmentSlot() == EntityEquipmentSlot.HEAD || this.getEquipmentSlot() == EntityEquipmentSlot.FEET ? Pair.of((byte)2, (byte)2) : Pair.of((byte)3, (byte)3);
    }

    public float getArmorTypeMultiplier()
    {
        switch (this.getEquipmentSlot())
        {
            case HEAD:
            {
                return 0.15F;
            }

            case CHEST:
            {
                return 0.4F;
            }

            case LEGS:
            {
                return 0.3F;
            }

            case FEET:
            {
                return 0.15F;
            }

            default:
            {
                return 0F;
            }
        }
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        multimap.removeAll(SharedMonsterAttributes.ARMOR.getName());
        multimap.removeAll(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName());
        if (equipmentSlot == this.armorType)
        {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.stats.resistance * this.getArmorTypeMultiplier(), 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)this.stats.toughness * this.getArmorTypeMultiplier(), 0));
        }

        return multimap;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return "exp:textures/items/armor/" + this.stats.name + "/layer_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }
}

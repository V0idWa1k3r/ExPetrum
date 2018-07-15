package v0id.exp.item.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.ItemIngot;

import java.util.Set;

public abstract class ItemExPTool extends ItemTool implements IExPTool
{
	public static final Set<Block> effectiveOn = Sets.newHashSet(new Block[]{});
    public final EnumToolClass type;
    public final EnumToolStats stats;

	public ItemExPTool(EnumToolStats stats, EnumToolClass type)
	{
		super(1, 1, ExPMisc.materialExPetrum, effectiveOn);
		this.setFull3D();
		this.setHasSubtypes(true);
		allTools.put(Pair.of(type, stats), this);
        this.stats = stats;
        this.type = type;
	}

    @Override
    public EnumToolStats getStats(ItemStack is)
    {
        return this.stats;
    }

    @Override
    public EnumToolClass getToolClass()
    {
        return this.type;
    }

    @Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		EnumMetal metal = this.getStats(toRepair).getMaterial();
		EnumMetal ingotMetal = repair.getItem() instanceof ItemIngot ? EnumMetal.values()[Math.min(repair.getMetadata(), EnumMetal.values().length - 1)] : null;
		return metal != null && metal.equals(ingotMetal);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState)
	{
		return this.getToolClass().getName().equals(toolClass) ? this.getStats(stack).getHarvestLevel() : super.getHarvestLevel(stack, toolClass, player, blockState);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return Sets.newHashSet(this.getToolClass().getName());
	}

	public void setSelfRegistryName(ResourceLocation loc)
    {
        ItemStack stack = new ItemStack(this, 1, 0);
        this.setRegistryName(new ResourceLocation(loc.getResourceDomain(), loc.getResourcePath() + "." + (this.getStats(stack).getMaterial() != null ? this.getStats(stack).getName() : "stone")));
    }

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return this.getToolCompound(stack).getInteger("damageMax");
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> ret = HashMultimap.create();
		if (slot == EntityEquipmentSlot.MAINHAND)
        {
			ret.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage(stack), 0));
			ret.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.getAttackSpeed(stack), 0));
        }
		
		return ret;
    }
}

package v0id.exp.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.IChiselable;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.IWeapon;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.IInitializableItem;

import java.util.Arrays;

public class ItemChisel extends ItemExPWeapon implements IWeapon, IWeightProvider, IInitializableItem, IOreDictEntry
{
	public ItemChisel()
	{
		super();
		this.initItem();
	}

	@Override
	public void registerOreDictNames()
	{
		Arrays.stream(EnumToolStats.values()).forEach(mat -> Arrays.stream(ExPOreDict.itemChisel).forEach(name ->
		{
			OreDictionary.registerOre(name, new ItemStack(this, 1, mat.ordinal()));
			OreDictionary.registerOre(name + Character.toUpperCase(mat.name().charAt(0)) + mat.name().toLowerCase().substring(1), new ItemStack(this, 1, mat.ordinal()));
		}));
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemChisel));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getStats(item).getWeight() * this.getToolClass().getWeight();
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return IWeightProvider.DEFAULT_VOLUME;
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.NONE;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.LIGHT;
	}

	@Override
	public EnumToolClass getToolClass()
	{
		return EnumToolClass.CHISEL;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 0.3F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -1.5F;
	}

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState at = worldIn.getBlockState(pos);
        if (at.getBlock() instanceof IChiselable)
        {
            ItemStack hammer = ItemStack.EMPTY;
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack.getItem() instanceof ItemHammer)
                {
                    hammer = stack;
                    break;
                }
            }

            if (hammer.isEmpty())
            {
                return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }

            IBlockState to = ((IChiselable) at.getBlock()).chisel(at, worldIn, pos);
            if (to != at)
            {
                for (int i = 0; i < 16; ++i)
                {
                    worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + worldIn.rand.nextDouble(), pos.getY() + worldIn.rand.nextDouble(), pos.getZ() + worldIn.rand.nextDouble(), 0, 0, 0, Block.getStateId(at));
                }

                worldIn.playSound(null, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 0.1F, 2.0f);
                worldIn.setBlockState(pos, to, 3);
                player.getHeldItem(hand).damageItem(1, player);
                hammer.damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}

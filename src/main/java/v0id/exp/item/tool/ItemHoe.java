package v0id.exp.item.tool;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.IWeapon;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.tile.crop.EnumPlantNutrient;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.exp.tile.TileFarmland;

import java.util.Arrays;
import java.util.stream.Stream;

public class ItemHoe extends ItemExPTool implements IWeapon, IWeightProvider, IOreDictEntry
{
	public ItemHoe(EnumToolStats stats)
	{
		super(stats, EnumToolClass.HOE);
		this.setSelfRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemHoe));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

	@Override
	public void registerOreDictNames()
	{
		Arrays.stream(EnumToolStats.values()).forEach(mat -> Arrays.stream(ExPOreDict.itemHoe).forEach(name ->
		{
			OreDictionary.registerOre(name, new ItemStack(this, 1, mat.ordinal()));
			OreDictionary.registerOre(name + Character.toUpperCase(mat.name().charAt(0)) + mat.name().toLowerCase().substring(1), new ItemStack(this, 1, mat.ordinal()));
		}));
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getStats(item).getWeight() * this.getToolClass().getWeight();
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)2, (byte)3);
	}

	@Override
	public WeaponType getType(ItemStack is)
	{
		return WeaponType.NONE;
	}

	@Override
	public EnumWeaponWeight getWeaponWeight(ItemStack is)
	{
		return EnumWeaponWeight.HEAVY;
	}

	@Override
	public float getAttackDamage(ItemStack is)
	{
		return this.getStats(is).getDamage() * this.getStats(is).getWeaponDamageMultiplier() * 0.4F;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return -1F;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getBlock().isAssociatedBlock(Blocks.GRASS) || state.getBlock().isAssociatedBlock(Blocks.DIRT))
		{
			if (worldIn.isRemote)
			{
				return EnumActionResult.SUCCESS;
			}
			else
			{
				player.getHeldItem(hand).damageItem(1, player);
				float nutrients = state.getBlock().isAssociatedBlock(Blocks.GRASS) ? 1 : 0.2F;
				if (state.getPropertyKeys().contains(ExPBlockProperties.DIRT_CLASS))
				{
					EnumDirtClass base = state.getValue(ExPBlockProperties.DIRT_CLASS);
					worldIn.setBlockState(pos, ExPBlocks.farmland.getDefaultState().withProperty(ExPBlockProperties.DIRT_CLASS, base));
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1, 0.1F);
					TileEntity tile = worldIn.getTileEntity(pos);
					if (tile != null && tile instanceof TileFarmland)
					{
						IFarmland farmlandData = IFarmland.of(tile);
						Stream.of(EnumPlantNutrient.values()).forEach(n -> farmlandData.setNutrient(n, nutrients));
					}
				}
				else
				{
					ExPMisc.modLogger.warn("Unknown soil block at %s!", pos);
				}
				
				return EnumActionResult.SUCCESS;
			}
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

}

package v0id.exp.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.IAcceptsWaterCan;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.ExPetrum;
import v0id.exp.item.IInitializableItem;

import java.util.Arrays;
import java.util.List;

public class ItemWateringCan extends ItemExPTool implements IWeightProvider, IInitializableItem, IOreDictEntry
{
	public ItemWateringCan(EnumToolStats stats)
	{
		super(stats, EnumToolClass.WATERING_CAN);
		this.initItem();
	}
	
	@Override
	public float getAttackDamage(ItemStack is)
	{
		return 0;
	}

	@Override
	public float getAttackSpeed(ItemStack is)
	{
		return 0;
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return this.getStats(item).getWeight() * this.getToolClass().getWeight();
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)2, (byte)2);
	}

	@Override
	public void registerOreDictNames()
	{
		Arrays.stream(EnumToolStats.values()).forEach(mat -> Arrays.stream(ExPOreDict.itemWateringCan).forEach(name ->
		{
			OreDictionary.registerOre(name, new ItemStack(this, 1, mat.ordinal()));
			OreDictionary.registerOre(name + Character.toUpperCase(mat.name().charAt(0)) + mat.name().toLowerCase().substring(1), new ItemStack(this, 1, mat.ordinal()));
		}));
	}

	@Override
	public void initItem()
	{
		this.setSelfRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemWateringCan));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabTools);
		this.setHasSubtypes(true);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new WateringCanCapability(stack);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if (!(player instanceof EntityPlayer))
		{
			return;
		}
		
		int tier = this.getStats(stack).getTier();
		RayTraceResult rtr = this.rayTrace(player.world, (EntityPlayer) player, true);
		if (rtr != null && rtr.typeOfHit == Type.BLOCK)
		{
			BlockPos pos = rtr.getBlockPos();
			World w = player.getEntityWorld();
			IBlockState blockHit = w.getBlockState(pos);
			Fluid f = FluidRegistry.lookupFluidForBlock(blockHit.getBlock());
			if (f == null && blockHit.getBlock() instanceof IFluidBlock)
			{
				f = ((IFluidBlock)blockHit.getBlock()).getFluid();
			}
			
			IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (FluidRegistry.WATER.equals(f))
			{
				if (!player.world.isRemote)
				{
					this.tryFillWithWater(player, tier, pos, w, blockHit, f, cap);
				}
			}
			else
			{
				if (f == null)
				{
					tryWaterBasedOnTier(player, tier, pos, w, blockHit, cap, stack);
				}
			}
		}
		
		super.onUsingTick(stack, player, count);
	}

	public void tryWaterBasedOnTier(EntityLivingBase player, int tier, BlockPos pos, World w, IBlockState blockHit, IFluidHandlerItem cap, ItemStack is)
	{
		IFluidTankProperties[] props = cap.getTankProperties();
		if (props != null && props.length > 0)
		{
			if (props[0].getContents() != null && props[0].getContents().amount > 0)
			{
				EntityPlayer p = (EntityPlayer) player;
				switch(tier)
				{
					case 1:
					{
						Vec3d look = p.getLookVec();
						EnumFacing directionFacing = EnumFacing.getFacingFromVector((float)look.x, 0F, (float)look.z);
						this.tryHydrateBlock(p, tier, pos, w, blockHit, cap, is);
						this.tryHydrateBlock(p, tier, pos.offset(directionFacing), w, w.getBlockState(pos.offset(directionFacing)), cap, is);
						break;
					}
					
					case 2:
					{
						Vec3d look = p.getLookVec();
						EnumFacing directionFacing = EnumFacing.getFacingFromVector((float)look.x, 0F, (float)look.z);
						this.tryHydrateBlock(p, tier, pos, w, blockHit, cap, is);
						this.tryHydrateBlock(p, tier, pos.offset(directionFacing), w, w.getBlockState(pos.offset(directionFacing)), cap, is);
						this.tryHydrateBlock(p, tier, pos.offset(directionFacing).offset(directionFacing), w, w.getBlockState(pos.offset(directionFacing).offset(directionFacing)), cap, is);
						break;
					}
					
					case 3:
					{
						for (int i = 0; i < 9; ++i)
						{
							this.tryHydrateBlock(p, tier, pos.add(i % 3 - 1, 0, i / 3 - 1), w, w.getBlockState(pos.add(i % 3 - 1, 0, i / 3 - 1)), cap, is);
						}
						
						break;
					}
					
					case 4:
					{
						for (int i = 0; i < 25; ++i)
						{
							this.tryHydrateBlock(p, tier, pos.add(i % 5 - 2, 0, i / 5 - 2), w, w.getBlockState(pos.add(i % 5 - 2, 0, i / 5 - 2)), cap, is);
						}
						
						break;
					}
					
					case 5:
					{
						for (int i = 0; i < 49; ++i)
						{
							this.tryHydrateBlock(p, tier, pos.add(i % 7 - 3, 0, i / 7 - 3), w, w.getBlockState(pos.add(i % 7 - 3, 0, i / 7 - 3)), cap, is);
						}
						
						break;
					}
					
					case 0:
					default:
					{
						this.tryHydrateBlock(p, tier, pos, w, blockHit, cap, is);
						break;
					}
				}
			}
		}
	}
	
	public void tryHydrateBlock(EntityPlayer player, int tier, BlockPos pos, World w, IBlockState state, IFluidHandlerItem cap, ItemStack is)
	{
		while ((state.getMaterial() == Material.PLANTS || w.isAirBlock(pos)) && pos.getY() > 0)
		{
			pos = pos.down();
			state = w.getBlockState(pos);
		}
		
		if (state.getBlock() instanceof IAcceptsWaterCan)
		{
			((IAcceptsWaterCan)state.getBlock()).acceptWatering(player, w, pos, state, cap, is, tier);
		}
		
		w.spawnParticle(EnumParticleTypes.WATER_SPLASH, pos.getX() + w.rand.nextFloat(), pos.getY() + 1, pos.getZ() + w.rand.nextFloat(), 0, 0, 0);
	}

	public void tryFillWithWater(EntityLivingBase player, int tier, BlockPos pos, World w, IBlockState blockHit, Fluid f, IFluidHandlerItem cap)
	{
		if (player.ticksExisted % (60 / (tier + 1)) == 0)
		{
			if (blockHit.getBlock() instanceof BlockFluidFinite)
			{
				BlockFluidFinite fluidBlock = (BlockFluidFinite) blockHit.getBlock();
				FluidStack toFill = new FluidStack(f, 100);
				if (cap.fill(toFill, false) == 100)
				{
					int quantaCurrent = fluidBlock.getQuantaValue(w, pos);
					if (quantaCurrent == 1)
					{
						w.setBlockToAir(pos);
					}
					else
					{
						w.setBlockState(pos, blockHit.withProperty(BlockFluidBase.LEVEL, quantaCurrent - 2), 3);
					}
					
					cap.fill(toFill, true);
				}
			}
			else
			{
				cap.fill(((IFluidBlock)blockHit.getBlock()).drain(w, pos, true), true);
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, world, tooltip, advanced);
		IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (cap != null)
		{
			IFluidTankProperties[] props = cap.getTankProperties();
			if (props != null && props.length > 0)
			{
				FluidStack fStack = props[0].getContents();
				if (fStack == null || fStack.amount == 0)
				{
					tooltip.add(I18n.format("exp.txt.item.desc.water.none"));
				}
				else
				{
                    EntityPlayer playerIn = ExPetrum.proxy.getClientPlayer();
                    if (playerIn != null)
                    {
                        if (IExPPlayer.of(playerIn).getProgressionStage().ordinal() < EnumPlayerProgression.IRON_AGE.ordinal())
                        {
                            float percentage = (float) fStack.amount / props[0].getCapacity();
                            if (percentage <= 0.25F)
                            {
                                tooltip.add(I18n.format("exp.txt.item.desc.water.leq25"));
                            }
                            else
                            {
                                if (percentage <= 0.5F)
                                {
                                    tooltip.add(I18n.format("exp.txt.item.desc.water.leq50"));
                                }
                                else
                                {
                                    if (percentage <= 0.75F)
                                    {
                                        tooltip.add(I18n.format("exp.txt.item.desc.water.leq75"));
                                    }
                                    else
                                    {
                                        tooltip.add(I18n.format("exp.txt.item.desc.water.100"));
                                    }
                                }
                            }
                        }
                        else
                        {
                            tooltip.add(I18n.format("exp.txt.item.desc.water", fStack.amount));
                        }
                    }
				}
			}
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged || !ItemStack.areItemsEqual(oldStack, newStack);
	}
}

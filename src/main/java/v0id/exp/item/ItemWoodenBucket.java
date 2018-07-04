package v0id.exp.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.player.IExPPlayer;
import v0id.core.VoidApi;
import v0id.core.util.I18n;

import java.util.Arrays;
import java.util.List;

public class ItemWoodenBucket extends Item implements IWeightProvider, IInitializableItem, IOreDictEntry
{
    public ItemWoodenBucket()
    {
        super();
        this.initItem();
    }

    @Override
    public void registerOreDictNames()
    {
        Arrays.stream(ExPOreDict.itemWoodenBucket).forEach(name -> OreDictionary.registerOre(name, new ItemStack(this, 1, 1)));
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return item.getMetadata() == 0 ? 0.4F : 0.4F + this.getWater(item) / 10F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    public int getWater(ItemStack item)
    {
        return item.hasTagCompound() ? item.getTagCompound().getInteger("exp:liquidAmount") : 0;
    }

    public void setWater(ItemStack item, int amount)
    {
        if (amount == 0)
        {
            if (item.hasTagCompound())
            {
                item.getTagCompound().removeTag("exp:liquidAmount");
                if (item.getTagCompound().hasNoTags())
                {
                    item.setTagCompound(null);
                }
            }
        }
        else
        {
            if (!item.hasTagCompound())
            {
                item.setTagCompound(new NBTTagCompound());
            }

            item.getTagCompound().setInteger("exp:liquidAmount", amount);
        }
    }

    public void setWaterType(ItemStack item, Fluid fluid)
    {
        if (fluid == null)
        {
            item.setItemDamage(0);
            this.setWater(item, 0);
        }
        else
        {
            item.setItemDamage(fluid == ExPFluids.freshWater || fluid == FluidRegistry.WATER ? 1 : fluid == ExPFluids.saltWater ? 2 : 3);
        }
    }

    public Fluid getStoredFluid(ItemStack item)
    {
        return item.getMetadata() == 0 ? null : item.getMetadata() == 1 ? FluidRegistry.WATER : item.getMetadata() == 2 ? ExPFluids.saltWater : ExPFluids.milk;
    }

    @Override
    public void initItem()
    {
        this.setHasSubtypes(true);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemWoodenBucket));
        this.setMaxStackSize(1);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabTools);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (int i = 0; i < 4; ++i)
        {
            ItemStack is = new ItemStack(this, 1, i);
            if (i != 0)
            {
                this.setWater(is, 10);
            }

            items.add(is);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
    {
        RayTraceResult rtr = this.rayTrace(player.world, player, true);
        if (rtr != null && rtr.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = rtr.getBlockPos();
            World w = player.getEntityWorld();
            ItemStack is = player.getHeldItem(handIn);
            IBlockState blockHit = w.getBlockState(pos);
            Fluid f = FluidRegistry.lookupFluidForBlock(blockHit.getBlock());
            Fluid f1 = this.getStoredFluid(is);
            if (f == null && blockHit.getBlock() instanceof IFluidBlock)
            {
                f = ((IFluidBlock)blockHit.getBlock()).getFluid();
            }

            if ((f1 == null || f == f1) && (f == FluidRegistry.WATER || f == ExPFluids.saltWater))
            {
                int levelFluid = blockHit.getValue(BlockFluidFinite.LEVEL) + 1;
                int levelCurrent = this.getWater(is);
                int levelAdded = levelCurrent + levelFluid > 10 ? 10 - levelCurrent : levelFluid;
                if (f1 == null)
                {
                    this.setWaterType(is, f);
                    this.setWater(is, levelAdded);
                }
                else
                {
                    this.setWater(is, levelCurrent + levelAdded);
                }

                if (levelAdded != 0)
                {
                    if (levelFluid <= levelAdded)
                    {
                        w.setBlockToAir(pos);
                    }
                    else
                    {
                        w.setBlockState(pos, blockHit.withProperty(BlockFluidFinite.LEVEL, levelFluid - levelAdded));
                    }
                }

                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack is = player.getHeldItem(hand);
        Fluid f = this.getStoredFluid(is);
        if (f != null && worldIn.isAirBlock(pos.offset(facing)) && (f == FluidRegistry.WATER || f == ExPFluids.saltWater))
        {
            Block b = is.getMetadata() == 1 ? ExPBlocks.freshWater : ExPBlocks.saltWater;
            worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, 1F);
            if (!worldIn.isRemote)
            {
                worldIn.setBlockState(pos.offset(facing), b.getDefaultState().withProperty(BlockFluidFinite.LEVEL, this.getWater(is) - 1));
                this.setWater(is, 0);
                this.setWaterType(is, null);
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, world, tooltip, advanced);
        int val = this.getWater(stack);
        if (val == 0)
        {
            tooltip.add(I18n.format("exp.txt.item.desc.water.none"));
        }
        else
        {
            EntityPlayer playerIn = VoidApi.proxy.getClientPlayer();
            if (playerIn != null)
            {
                if (IExPPlayer.of(playerIn).getProgressionStage().ordinal() < EnumPlayerProgression.IRON_AGE.ordinal())
                {
                    float percentage = (float) val / 10F;
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
                    tooltip.add(I18n.format("exp.txt.item.desc.water", val * 100));
                }
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + stack.getMetadata();
    }
}

package v0id.exp.item;

import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.item.IMold;
import v0id.api.exp.metal.AlloyHelper;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.exp.ExPetrum;
import v0id.exp.block.BlockPottery;
import v0id.exp.player.inventory.PlayerInventoryHelper;
import v0id.exp.tile.TilePot;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPottery extends Item implements IInitializableItem, IWeightProvider, IContainerTickable
{
    public enum EnumPotteryType
    {
        CLAY_POT(0.3F),
        CLAY_JUG(0.1F),
        CLAY_BOWL(0.05F),
        CERAMIC_POT(0.3F),
        CERAMIC_JUG(0.1F),
        CERAMIC_BOWL(0.05F),
        CERAMIC_JUG_FULL(1.1F);

        EnumPotteryType(float f)
        {
            this.weight = f;
        }

        private float weight;
    }

    public ItemPottery()
    {
        super();
        this.initItem();
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        IItemHandler cap = item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            float w = 0F;
            for (int i = 0; i < cap.getSlots(); ++i)
            {
                w += PlayerInventoryHelper.getWeight(cap.getStackInSlot(i));
            }

            return EnumPotteryType.values()[item.getMetadata()].weight + w;
        }

        return EnumPotteryType.values()[item.getMetadata()].weight;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    @Override
    public void initItem()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(ExPCreativeTabs.tabMiscItems);
        this.setRegistryName(ExPRegistryNames.itemPottery);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setMaxStackSize(1);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (tab != this.getCreativeTab())
        {
            return;
        }

        for (EnumPotteryType type : EnumPotteryType.values())
        {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack is = player.getHeldItem(hand);
        EnumPotteryType type = EnumPotteryType.values()[is.getMetadata()];
        if (type == EnumPotteryType.CERAMIC_BOWL || type == EnumPotteryType.CERAMIC_JUG || type == EnumPotteryType.CERAMIC_POT)
        {
            if (type == EnumPotteryType.CERAMIC_POT && is.hasTagCompound() && is.getTagCompound().hasKey("metals"))
            {
                return EnumActionResult.PASS;
            }

            if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
            {
                pos = pos.offset(facing);
            }

            if (!is.isEmpty()  && player.canPlayerEdit(pos, facing, is) && worldIn.mayPlace(ExPBlocks.pottery, pos, false, facing, (Entity)null))
            {
                if (worldIn.isAirBlock(pos) || worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
                {
                    IBlockState toSet = ExPBlocks.pottery.getDefaultState().withProperty(ExPBlockProperties.POTTERY_TYPE, type == EnumPotteryType.CERAMIC_POT ? BlockPottery.EnumPotteryType.POT : type == EnumPotteryType.CERAMIC_BOWL ? BlockPottery.EnumPotteryType.BOWL : BlockPottery.EnumPotteryType.JUG);
                    worldIn.setBlockState(pos, toSet, 3);
                    SoundType soundtype = toSet.getBlock().getSoundType(toSet, worldIn, pos, player);
                    worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (type == EnumPotteryType.CERAMIC_POT)
                    {
                        TilePot pot = (TilePot)worldIn.getTileEntity(pos);
                        if (pot != null)
                        {
                            IItemHandler capSelf = is.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                            IItemHandler capPot = pot.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                            for (int i = 0; i < capSelf.getSlots() && i < capPot.getSlots(); ++i)
                            {
                                capPot.insertItem(i, capSelf.getStackInSlot(i), false);
                            }
                        }
                    }

                    is.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack is = playerIn.getHeldItem(handIn);
        if (is.getMetadata() == EnumPotteryType.CERAMIC_POT.ordinal())
        {
            playerIn.openGui(ExPetrum.instance, 2, worldIn, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, is);
        }
        else
        {
            if (is.getMetadata() == EnumPotteryType.CERAMIC_JUG.ordinal())
            {
                RayTraceResult rtr = this.rayTrace(playerIn.world, playerIn, true);
                if (rtr != null && rtr.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    BlockPos pos = rtr.getBlockPos();
                    World w = playerIn.getEntityWorld();
                    IBlockState blockHit = w.getBlockState(pos);
                    Fluid f = FluidRegistry.lookupFluidForBlock(blockHit.getBlock());
                    if (f == null && blockHit.getBlock() instanceof IFluidBlock)
                    {
                        f = ((IFluidBlock) blockHit.getBlock()).getFluid();
                    }

                    if (f == FluidRegistry.WATER)
                    {
                        f = ExPFluids.freshWater;
                    }

                    if (f == ExPFluids.freshWater)
                    {
                        if (blockHit.getBlock() instanceof BlockFluidFinite)
                        {
                            int current = blockHit.getValue(BlockFluidFinite.LEVEL);
                            if (current <= 1)
                            {
                                w.setBlockToAir(pos);
                            }
                            else
                            {
                                w.setBlockState(pos, blockHit.withProperty(BlockFluidFinite.LEVEL, current - 1));
                            }
                        }
                        else
                        {
                            w.setBlockToAir(pos);
                        }

                        is.setItemDamage(EnumPotteryType.CERAMIC_JUG_FULL.ordinal());
                        playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
                    }
                }
            }
            else
            {
                if (is.getMetadata() == EnumPotteryType.CERAMIC_JUG_FULL.ordinal())
                {
                    playerIn.setActiveHand(handIn);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, is);
                }
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return stack.getMetadata() == EnumPotteryType.CERAMIC_JUG_FULL.ordinal() ? EnumAction.DRINK : super.getItemUseAction(stack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return stack.getMetadata() == EnumPotteryType.CERAMIC_JUG_FULL.ordinal() ? 40 : super.getMaxItemUseDuration(stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (stack.getMetadata() == EnumPotteryType.CERAMIC_JUG_FULL.ordinal() && entityLiving instanceof EntityPlayer)
        {
            IExPPlayer player = IExPPlayer.of((EntityPlayer) entityLiving);
            if (player != null && !worldIn.isRemote)
            {
                player.setThirst(player.getThirst() + 1000, true);
                stack.setItemDamage(EnumPotteryType.CERAMIC_JUG.ordinal());
            }
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void onContainerTick(ItemStack is, World w, BlockPos pos, TileEntity container)
    {
        this.onUpdate(is, w, null, 0, false);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("metals") && cap.getStackInSlot(0).getItem() instanceof IMold)
            {
                String metalName = stack.getTagCompound().getCompoundTag("metals").getKeySet().toArray(new String[0])[0];
                if (TemperatureUtils.getTemperature(stack) >= EnumMetal.valueOf(metalName.toUpperCase()).getMeltingTemperature() * 0.5F)
                {
                    ((IMold) cap.getStackInSlot(0).getItem()).tryFill(cap.getStackInSlot(0), (metal, value) ->
                    {
                        NBTTagCompound metals = stack.getTagCompound().getCompoundTag("metals");
                        String name = metal.name().toLowerCase();
                        if (metals.hasKey(name))
                        {
                            if (value >= metals.getFloat(name))
                            {
                                metals.removeTag(name);
                                if (metals.hasNoTags())
                                {
                                    stack.getTagCompound().removeTag("metals");
                                    if (stack.getTagCompound().hasNoTags())
                                    {
                                        stack.setTagCompound(null);
                                    }
                                }
                            }
                            else
                            {
                                metals.setFloat(name, metals.getFloat(name) - value);
                            }
                        }
                    }, s ->
                    {
                        ((ItemStackHandler) cap).setStackInSlot(0, s);
                        TemperatureUtils.setTemperature(s, TemperatureUtils.getTemperature(stack));
                    }, EnumMetal.valueOf(metalName.toUpperCase()), stack.getTagCompound().getCompoundTag("metals").getFloat(metalName));
                }
            }

            boolean melted = false;
            for (int i = 0; i < cap.getSlots(); ++i)
            {
                final int lambdaCapture = i;
                ItemStack is = cap.getStackInSlot(i);
                is.getItem().onUpdate(is, worldIn, entityIn, i, false);
                if (TemperatureUtils.getTemperature(is) < TemperatureUtils.getTemperature(stack))
                {
                    TemperatureUtils.incrementTemperature(is, 0.5F);
                    if (is.getItem() instanceof IMeltableMetal)
                    {
                        EnumMetal metal = ((IMeltableMetal) is.getItem()).getMetal(is);
                        float meltingPoint = ((IMeltableMetal) is.getItem()).getMeltingTemperature(is);
                        if (TemperatureUtils.getTemperature(is) >= meltingPoint)
                        {
                            if (!stack.hasTagCompound())
                            {
                                stack.setTagCompound(new NBTTagCompound());
                            }

                            if (!stack.getTagCompound().hasKey("metals"))
                            {
                                stack.getTagCompound().setTag("metals", new NBTTagCompound());
                            }

                            stack.getTagCompound().getCompoundTag("metals").setFloat(metal.name().toLowerCase(), ((IMeltableMetal) is.getItem()).getMetalAmound(is) + (stack.getTagCompound().getCompoundTag("metals").hasKey(metal.name().toLowerCase()) ? stack.getTagCompound().getCompoundTag("metals").getFloat(metal.name().toLowerCase()) : 0));
                            ((ItemStackHandler)cap).setStackInSlot(i, ItemStack.EMPTY);
                            melted = true;
                            continue;
                        }
                    }

                    if (melted && stack.hasTagCompound() && stack.getTagCompound().hasKey("metals"))
                    {
                        checkForAlloys(stack);
                    }

                    RecipesSmelting.checkForSmelting(itemstack -> ((ItemStackHandler)cap).setStackInSlot(lambdaCapture, itemstack), is, false, true);
                }
                else
                {
                    TemperatureUtils.tickItem(is, true);
                }
            }
        }
    }

    public void checkForAlloys(ItemStack is)
    {
        NBTTagCompound tag = is.getTagCompound().getCompoundTag("metals");
        if (tag.getSize() > 1)
        {
            String[] keys = tag.getKeySet().toArray(new String[0]);
            List<Pair<EnumMetal, Integer>> data = Lists.newArrayList();
            for (String s : keys)
            {
                data.add(Pair.of(EnumMetal.valueOf(s.toUpperCase()), (int) tag.getFloat(s)));
            }

            EnumMetal metal = AlloyHelper.getAlloy(data.toArray(new Pair[0]));
            for (String s : keys)
            {
                tag.removeTag(s);
            }

            tag.setFloat(ObjectUtils.firstNonNull(metal, EnumMetal.ANY).name().toLowerCase(), data.stream().mapToInt(Pair::getRight).sum());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + EnumPotteryType.values()[stack.getMetadata()].name().toLowerCase();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        if (stack.getMetadata() == EnumPotteryType.CERAMIC_POT.ordinal())
        {
            return new ICapabilitySerializable<NBTTagCompound>()
            {
                private ItemStackHandler inventory = new ItemStackHandler(4);

                @Override
                public NBTTagCompound serializeNBT()
                {
                    NBTTagCompound ret = new NBTTagCompound();
                    ret.setTag("inventory", inventory.serializeNBT());
                    return ret;
                }

                @Override
                public void deserializeNBT(NBTTagCompound nbt)
                {
                    inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
                }

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
                {
                    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
                {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
                }
            };
        }

        return super.initCapabilities(stack, nbt);
    }
}

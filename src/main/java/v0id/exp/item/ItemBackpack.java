package v0id.exp.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.exp.ExPetrum;
import v0id.exp.player.inventory.PlayerInventoryHelper;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBackpack extends Item implements IWeightProvider, IContainerTickable
{
    public final boolean isBig;

    public ItemBackpack(boolean b)
    {
        super();
        this.isBig = b;
        this.setHasSubtypes(true);
        this.setCreativeTab(ExPCreativeTabs.tabMiscItems);
        this.setRegistryName(this.isBig ? ExPRegistryNames.itemTravelersBackpack : ExPRegistryNames.itemLightBackpack);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setMaxStackSize(1);
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

            return w + (this.isBig ? 0.5F : 0.1F);
        }

        return this.isBig ? 0.5F : 0.1F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return IWeightProvider.DEFAULT_VOLUME;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.openGui(ExPetrum.instance, 17, worldIn, 0, 0, 0);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
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
            for (int i = 0; i < cap.getSlots(); ++i)
            {
                Integer lambdaCapture = i;
                ItemStack is = cap.getStackInSlot(i);
                is.getItem().onUpdate(is, worldIn, entityIn, i, false);
                TemperatureUtils.tickItem(is, TemperatureUtils.getTemperature(is) >= TemperatureUtils.getTemperature(stack));
                RecipesSmelting.checkForSmelting(itemstack -> ((ItemStackHandler)cap).setStackInSlot(lambdaCapture, itemstack), is, false, true);
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ICapabilitySerializable<NBTTagCompound>()
        {
            private ItemStackHandler inventory = new ItemStackHandler(ItemBackpack.this.isBig ? 18 : 9);

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
}

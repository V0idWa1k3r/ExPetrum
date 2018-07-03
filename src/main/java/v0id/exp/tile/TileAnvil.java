package v0id.exp.tile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.item.IContainerTickable;
import v0id.api.exp.item.IHammer;
import v0id.api.exp.metal.AnvilMinigame;
import v0id.api.exp.recipe.RecipesAnvil;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.exp.item.ItemGeneric;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAnvil extends TileEntity implements ITickable
{
    public int tickIndex;

    public ItemStackHandler inventory = new ItemStackHandler(6)
    {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return slot <= 3 ? 1 : super.getStackLimit(slot, stack);
        }
    };

    public void sendUpdatePacket()
    {
        if (this.world != null && !this.world.isRemote)
        {
            NBTTagCompound sent = new NBTTagCompound();
            sent.setTag("tileData", this.serializeNBT());
            sent.setTag("blockPosData", new DimBlockPos(this.getPos(), this.getWorld().provider.getDimension()).serializeNBT());
            VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64));
        }
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        ++this.tickIndex;
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
            ItemStack is = this.inventory.getStackInSlot(i);
            if (this.tickIndex % 5 == 0 || i == 4 || i == 5)
            {
                TemperatureUtils.tickItem(is, true);
            }

            if (is.getItem() instanceof IContainerTickable)
            {
                ((IContainerTickable) is.getItem()).onContainerTick(is, this.world, this.pos, this);
            }
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.serializeNBT());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.serializeNBT();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.deserializeNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.deserializeNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        NBTTagCompound ret = super.writeToNBT(compound);
        ret.setTag("inventory", this.inventory.serializeNBT());

        return ret;
    }

    public int getTier()
    {
        return this.world.getBlockState(this.pos).getValue(ExPBlockProperties.ANVIL_MATERIAL).getTier();
    }

    public void prepareIngot(RecipesAnvil.IAnvilRecipe recipe)
    {
        if (this.getTier() >= recipe.getRequiredTier())
        {
            ItemStack ingot = this.inventory.getStackInSlot(3);
            if (!ingot.hasTagCompound())
            {
                ingot.setTagCompound(new NBTTagCompound());
            }

            if (!ingot.getTagCompound().hasKey("exp:smithing"))
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("heat", 0);
                tag.setInteger("integrity", 100);
                tag.setInteger("integrityLost", 0);
                tag.setInteger("progress", 0);
                tag.setInteger("recipe", recipe.getID());
                ingot.getTagCompound().setTag("exp:smithing", tag);
                this.drawCards();
            }
        }
    }

    public void weld()
    {
        ItemStack ingot1 = this.inventory.getStackInSlot(0);
        ItemStack ingot2 = this.inventory.getStackInSlot(1);
        if (this.inventory.getStackInSlot(2).isEmpty())
        {
            RecipesAnvil.WeldingRecipe rec = RecipesAnvil.getWeldingRecipe(ingot1, ingot2);
            if (rec != null && rec.anvilTier <= this.getTier())
            {
                ItemStack flux = this.inventory.getStackInSlot(5);
                if (!flux.isEmpty() && flux.getItem() instanceof ItemGeneric && flux.getMetadata() == ItemGeneric.EnumGenericType.FLUX.ordinal())
                {
                    this.inventory.setStackInSlot(2, rec.itemOut.copy());
                    TemperatureUtils.setTemperature(this.inventory.getStackInSlot(2), (TemperatureUtils.getTemperature(ingot1) + TemperatureUtils.getTemperature(ingot2)) / 2F);
                    flux.shrink(1);
                    ingot1.shrink(1);
                    ingot2.shrink(1);
                    this.world.playSound(null, this.pos, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.BLOCKS, 1F, 0.5F);
                }
            }
        }
    }

    public void drawCards()
    {
        ItemStack ingot = this.inventory.getStackInSlot(3);
        int heat = ingot.hasTagCompound() && ingot.getTagCompound().hasKey("exp:smithing") ? ingot.getTagCompound().getCompoundTag("exp:smithing").getInteger("heat") : 0;
        int totalWeight = AnvilMinigame.Card.allCards.stream().mapToInt(c -> c.getWeight(heat)).sum();
        NBTTagCompound tag = ingot.getTagCompound().getCompoundTag("exp:smithing");
        int cards = 2 + this.world.rand.nextInt(2);
        tag.setByte("cards", (byte) cards);
        AnvilMinigame.ensureAllCardsAreRegistered();
        for (int i = 0; i < cards; ++i)
        {
            int weight = this.world.rand.nextInt(totalWeight);
            AnvilMinigame.Card c = null;
            for (int j = 0; j < AnvilMinigame.Card.allCards.size(); ++j)
            {
                weight -= AnvilMinigame.Card.allCards.get(j).getWeight(heat);
                if (weight <= 0)
                {
                    c = AnvilMinigame.Card.allCards.get(j);
                    break;
                }
            }

            tag.setByte("card_" + i, (byte) c.id);
        }
    }

    public void acceptCardPacket(int i, EntityPlayerMP sender)
    {
        ItemStack ingot = this.inventory.getStackInSlot(3);
        ItemStack hammer = this.inventory.getStackInSlot(4);
        if (!ingot.isEmpty() && !hammer.isEmpty() && ingot.hasTagCompound() && ingot.getTagCompound().hasKey("exp:smithing") && hammer.getItem() instanceof IHammer)
        {
            AnvilMinigame.Card c = AnvilMinigame.Card.allCards.get(i);
            RecipesAnvil.IAnvilRecipe recipe = RecipesAnvil.allRecipes.get(ingot.getTagCompound().getCompoundTag("exp:smithing").getInteger("recipe"));
            if (recipe.matches(ingot, (int) TemperatureUtils.getTemperature(ingot)) && recipe.getRequiredTier() <= this.getTier())
            {
                c.doEffects(ingot, hammer, sender, (WorldServer) sender.world, this.pos, j ->
                {
                    ingot.getTagCompound().getCompoundTag("exp:smithing").setInteger("progress", ingot.getTagCompound().getCompoundTag("exp:smithing").getInteger("progress") + j);
                    if (ingot.getTagCompound().getCompoundTag("exp:smithing").getInteger("progress") > recipe.getProgressRequired(ingot))
                    {
                        this.inventory.setStackInSlot(3, recipe.getResult(ingot));
                    }
                });

                if (this.inventory.getStackInSlot(3) == ingot)
                {
                    this.drawCards();
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory) : super.getCapability(capability, facing);
    }
}

package v0id.exp.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import v0id.api.exp.data.ExPItems;
import v0id.exp.client.gui.GuiCampfire;
import v0id.exp.client.gui.GuiPot;
import v0id.exp.client.gui.GuiPotteryStation;
import v0id.exp.container.ContainerCampfire;
import v0id.exp.container.ContainerPot;
import v0id.exp.container.ContainerPotteryStation;
import v0id.exp.tile.TileCampfire;
import v0id.exp.tile.TilePotteryStation;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler
{
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID)
        {
            case 0:
            {
                return new GuiCampfire(player.inventory, (TileCampfire) tile);
            }

            case 1:
            {
                return new GuiPotteryStation(player.inventory, (TilePotteryStation) tile);
            }

            case 2:
            {
                ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
                if (is.getItem() != ExPItems.pottery || !is.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                {
                    is = player.getHeldItem(EnumHand.OFF_HAND);
                }

                return new GuiPot(player.inventory, is);
            }

            case 3:
            {
                return new GuiPot(player.inventory, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            }
        }

        return null;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID)
        {
            case 0:
            {
                return new ContainerCampfire(player.inventory, (TileCampfire) tile);
            }

            case 1:
            {
                return new ContainerPotteryStation(player.inventory, (TilePotteryStation) tile);
            }

            case 2:
            {
                ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
                if (is.getItem() != ExPItems.pottery || !is.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                {
                    is = player.getHeldItem(EnumHand.OFF_HAND);
                }

                return new ContainerPot(player.inventory, is);
            }

            case 3:
            {
                return new ContainerPot(player.inventory, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            }
        }

        return null;
    }
}

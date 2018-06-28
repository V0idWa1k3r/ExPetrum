package v0id.exp.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import v0id.exp.client.gui.GuiCampfire;
import v0id.exp.container.ContainerCampfire;
import v0id.exp.tile.TileCampfire;

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
        }

        return null;
    }
}

package v0id.exp.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import v0id.api.exp.data.ExPPackets;
import v0id.core.network.IPacketHandler;
import v0id.core.network.VoidNetwork;
import v0id.exp.tile.TileAnvil;

public class PacketHandlerCardClick implements IPacketHandler
{
	@Override
	public void handleData(NBTTagCompound data, EntityPlayer player)
	{
		World w = DimensionManager.getWorld(data.getInteger("world"));
        BlockPos pos = new BlockPos(data.getInteger("posX"), data.getInteger("posY"), data.getInteger("posZ"));
        w.getMinecraftServer().addScheduledTask(() ->
        {
            TileEntity tile = w.getTileEntity(pos);
            if (tile instanceof TileAnvil)
            {
                ((TileAnvil) tile).acceptCardPacket(data.getInteger("card"), (EntityPlayerMP) player);
            }
        });
	}

    @Override
    public void handleData(NBTTagCompound data)
    {
    }

    @Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}

	public static void sendPacket(TileAnvil tile, int cardID)
	{
	    NBTTagCompound tag = new NBTTagCompound();
	    tag.setInteger("world", tile.getWorld().provider.getDimension());
	    tag.setInteger("posX", tile.getPos().getX());
	    tag.setInteger("posY", tile.getPos().getY());
	    tag.setInteger("posZ", tile.getPos().getZ());
	    tag.setInteger("card", cardID);
		VoidNetwork.sendDataToServer(ExPPackets.CARD_CLICK, tag);
	}
	
	public static void sendRequestPacket()
	{
	}
}

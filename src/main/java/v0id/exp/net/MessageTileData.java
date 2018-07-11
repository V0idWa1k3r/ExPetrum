package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.api.exp.tile.ISyncableTile;
import v0id.exp.ExPetrum;

public class MessageTileData implements IMessage
{
    public BlockPos pos;
    public boolean doRefresh;
    public NBTTagCompound dataTag;

    public MessageTileData()
    {
    }

    public MessageTileData(ISyncableTile tile, boolean doRefresh)
    {
        this.doRefresh = doRefresh;
        this.pos = ((TileEntity)tile).getPos();
        this.dataTag = tile.serializeData();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.doRefresh = buf.readBoolean();
        this.dataTag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeBoolean(this.doRefresh);
        ByteBufUtils.writeTag(buf, this.dataTag);
    }

    public static class Handler implements IMessageHandler<MessageTileData, IMessage>
    {
        @Override
        public IMessage onMessage(MessageTileData message, MessageContext ctx)
        {
            ExPetrum.proxy.getClientThreadListener().addScheduledTask(() ->
            {
                if (ExPetrum.proxy.getClientWorld().isBlockLoaded(message.pos))
                {
                    TileEntity tile = ExPetrum.proxy.getClientWorld().getTileEntity(message.pos);
                    if (tile instanceof ISyncableTile)
                    {
                        ((ISyncableTile) tile).readData(message.dataTag);
                        if (message.doRefresh)
                        {
                            ExPetrum.proxy.getClientWorld().markBlockRangeForRenderUpdate(message.pos, message.pos);
                        }
                    }
                }
            });

            return null;
        }
    }
}

package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.exp.tile.TileAnvil;

public class MessageWeld implements IMessage
{
    public BlockPos pos;
    public int dimension;

    public MessageWeld(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public MessageWeld()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeInt(this.dimension);
    }

    public static class Handler implements IMessageHandler<MessageWeld, IMessage>
    {
        @Override
        public IMessage onMessage(MessageWeld message, MessageContext ctx)
        {
            World w = DimensionManager.getWorld(message.dimension);
            w.getMinecraftServer().addScheduledTask(() ->
            {
                TileEntity tile = w.getTileEntity(message.pos);
                if (tile instanceof TileAnvil)
                {
                    ((TileAnvil) tile).weld();
                }
            });

            return null;
        }
    }
}

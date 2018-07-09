package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.exp.tile.TileMechanicalPotteryStation;

public class MessageSelectPottery implements IMessage
{
    public BlockPos pos;
    public int dimension;
    public int recipe;

    public MessageSelectPottery(BlockPos pos, int dimension, int recipe)
    {
        this.pos = pos;
        this.dimension = dimension;
        this.recipe = recipe;
    }

    public MessageSelectPottery()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.dimension = buf.readInt();
        this.recipe = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeInt(this.dimension);
        buf.writeInt(this.recipe);
    }

    public static class Handler implements IMessageHandler<MessageSelectPottery, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSelectPottery message, MessageContext ctx)
        {
            World w = DimensionManager.getWorld(message.dimension);
            w.getMinecraftServer().addScheduledTask(() ->
            {
                TileEntity tile = w.getTileEntity(message.pos);
                if (tile instanceof TileMechanicalPotteryStation)
                {
                    ((TileMechanicalPotteryStation) tile).selectedRecipe = message.recipe;
                }
            });

            return null;
        }
    }
}

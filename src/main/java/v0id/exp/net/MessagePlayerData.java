package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.ExPetrum;

public class MessagePlayerData implements IMessage
{
    public NBTTagCompound dataTag;

    public MessagePlayerData(NBTTagCompound dataTag)
    {
        this.dataTag = dataTag;
    }

    public MessagePlayerData()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.dataTag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.dataTag);
    }

    public static class Handler implements IMessageHandler<MessagePlayerData, IMessage>
    {
        @Override
        public IMessage onMessage(MessagePlayerData message, MessageContext ctx)
        {
            ExPetrum.proxy.getClientThreadListener().addScheduledTask(() ->
            {
                IExPPlayer.of(ExPetrum.proxy.getClientPlayer()).deserializeNBT(message.dataTag);
            });

            return null;
        }
    }
}

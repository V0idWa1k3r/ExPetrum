package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.ExPetrum;

public class MessageNewAge implements IMessage
{
    public EnumPlayerProgression age;

    public MessageNewAge(EnumPlayerProgression age)
    {
        this.age = age;
    }

    public MessageNewAge()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.age = EnumPlayerProgression.values()[buf.readByte()];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte((byte)this.age.ordinal());
    }

    public static class Handler implements IMessageHandler<MessageNewAge, IMessage>
    {
        @Override
        public IMessage onMessage(MessageNewAge message, MessageContext ctx)
        {
            ExPetrum.proxy.getClientThreadListener().addScheduledTask(() ->
            {
                EntityPlayer reciever = ExPetrum.proxy.getClientPlayer();
                if (reciever != null)
                {
                    IExPPlayer.of(reciever).triggerStage(message.age);
                }
            });

            return null;
        }
    }
}

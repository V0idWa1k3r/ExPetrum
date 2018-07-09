package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.ExPetrum;

public class MessageWorldData implements IMessage
{
    public NBTTagCompound dataTag;

    public MessageWorldData(NBTTagCompound dataTag)
    {
        this.dataTag = dataTag;
    }

    public MessageWorldData()
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

    public static class Handler implements IMessageHandler<MessageWorldData, IMessage>
    {
        @Override
        public IMessage onMessage(MessageWorldData message, MessageContext ctx)
        {
            ExPetrum.proxy.getClientThreadListener().addScheduledTask(() -> IExPWorld.of(ExPetrum.proxy.getClientWorld()).deserializeNBT(message.dataTag));
            return null;
        }
    }
}

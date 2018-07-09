package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.exp.settings.SettingsManager;

import java.util.stream.StreamSupport;

public class MessageSettings implements IMessage
{
    public NBTTagCompound dataTag;

    public MessageSettings(NBTTagCompound dataTag)
    {
        this.dataTag = dataTag;
    }

    public MessageSettings()
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

    public static class Handler implements IMessageHandler<MessageSettings, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSettings message, MessageContext ctx)
        {
            StreamSupport.stream(message.dataTag.getTagList("settings", Constants.NBT.TAG_COMPOUND).spliterator(), false).map(b -> (NBTTagCompound)b).forEach(tag ->
            {
                String id = tag.getString("id");
                String value = tag.getString("json");
                SettingsManager.acceptServerConfig(id, value);
            });

            return null;
        }
    }
}

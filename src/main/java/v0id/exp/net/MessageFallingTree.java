package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.exp.ExPetrum;
import v0id.exp.entity.EntityFallingTree;

public class MessageFallingTree implements IMessage
{
    public NBTTagCompound dataTag;
    public int netID;

    public MessageFallingTree(Entity e, NBTTagCompound dataTag)
    {
        this.dataTag = dataTag;
        this.netID = e.getEntityId();
    }

    public MessageFallingTree()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.netID = buf.readInt();
        this.dataTag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.netID);
        ByteBufUtils.writeTag(buf, this.dataTag);
    }

    public static class Handler implements IMessageHandler<MessageFallingTree, IMessage>
    {
        @Override
        public IMessage onMessage(MessageFallingTree message, MessageContext ctx)
        {
            ExPetrum.proxy.getClientThreadListener().addScheduledTask(() ->
            {
                World w = ExPetrum.proxy.getClientWorld();
                Entity fallingTreeRef = w.getEntityByID(message.netID);
                if (fallingTreeRef instanceof EntityFallingTree)
                {
                    ((EntityFallingTree) fallingTreeRef).deserializeDataFromNBT(message.dataTag);
                }

            });

            return null;
        }
    }
}

package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import v0id.exp.ExPetrum;
import v0id.exp.combat.ServerCombatHandler;

public class MessageSpecialAttack implements IMessage
{
    public NBTTagCompound dataTag;

    public MessageSpecialAttack(NBTTagCompound dataTag)
    {
        this.dataTag = dataTag;
    }

    public MessageSpecialAttack()
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

    public static class Handler implements IMessageHandler<MessageSpecialAttack, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSpecialAttack message, MessageContext ctx)
        {
            if (ctx.side == Side.SERVER)
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> ServerCombatHandler.handle(message.dataTag));
            }
            else
            {
                ExPetrum.proxy.getClientThreadListener().addScheduledTask(() -> ExPetrum.proxy.handleSpecialAttackPacket(message.dataTag));
            }

            return null;
        }
    }
}

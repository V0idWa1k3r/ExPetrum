package v0id.exp.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import v0id.exp.ExPetrum;
import v0id.exp.entity.EntityProjectile;

public class MessageProjectileCollide implements IMessage
{
    public int networkID;
    public double posX;
    public double posY;
    public double posZ;
    public float yaw;
    public float pitch;

    public MessageProjectileCollide(int networkID, double posX, double posY, double posZ, float yaw, float pitch)
    {
        this.networkID = networkID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public MessageProjectileCollide()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.networkID = buf.readInt();
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.networkID);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
    }

    public static class Handler implements IMessageHandler<MessageProjectileCollide, IMessage>
    {
        @Override
        public IMessage onMessage(MessageProjectileCollide message, MessageContext ctx)
        {
            ExPetrum.proxy.getClientThreadListener().addScheduledTask(() ->
            {
                World world = ExPetrum.proxy.getClientWorld();
                Entity e = world.getEntityByID(message.networkID);
                if (e instanceof EntityProjectile)
                {
                    e.posX = message.posX;
                    e.posY = message.posY;
                    e.posZ = message.posZ;
                    e.rotationYaw = message.yaw;
                    e.rotationPitch = message.pitch;
                    e.motionX = e.motionY = e.motionZ = 0;
                    ((EntityProjectile) e).inGround = true;
                }
            });

            return null;
        }
    }
}

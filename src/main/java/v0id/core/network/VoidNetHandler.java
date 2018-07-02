package v0id.core.network;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.VCLoggers;
import v0id.core.VoidApi;
import v0id.core.logging.LogLevel;
import v0id.core.util.MC;

public class VoidNetHandler implements IMessageHandler<Packet, IMessage>
{
	@Override
	public IMessage onMessage(Packet message, MessageContext ctx) 
	{
		if (VoidNetwork.handlers.containsKey(message.id))
		{
			VoidNetwork.handlers.get(message.id).handleData(message.data, FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER ? ctx.getServerHandler().player : VoidApi.proxy.getClientPlayer());
		}
		else
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Packet with ID %d arrived at %s but it has no handler registered and thus can't be processed!", message.id, MC.getSide().name());
		}
		
		return null;
	}

}

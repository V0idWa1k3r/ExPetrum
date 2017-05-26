package v0id.exp.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.core.VoidApi;
import v0id.api.core.network.IPacketHandler;
import v0id.api.core.util.MC;
import v0id.exp.ExPetrum;
import v0id.exp.combat.ServerCombatHandler;

public class PacketHandlerSpecialAttack implements IPacketHandler
{
	@Override
	public void handleData(NBTTagCompound data)
	{
		Side s = MC.getSide();
		if (s == Side.SERVER)
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> ServerCombatHandler.handle(data));
		}
		else
		{
			VoidApi.proxy.getClientListener().addScheduledTask(() -> ExPetrum.proxy.handleSpecialAttackPacket(data));
		}
	}

	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}
}

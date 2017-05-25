package v0id.exp.combat;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.core.logging.LogLevel;
import v0id.api.core.network.VoidNetwork;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.player.IExPPlayer;

public class ServerCombatHandler
{
	public static void handle(NBTTagCompound tag)
	{
		try
		{
			UUID requesterID = UUID.fromString(tag.getString("uuid"));
			SpecialAttack attack = SpecialAttack.registry.get(tag.getString("attackID"));
			boolean rightClick = tag.getBoolean("rightClick");
			EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(requesterID);
			ItemStack is = !player.getHeldItemMainhand().isEmpty() ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
			if (attack.canExecute(player, WeaponType.getType(is), EnumWeaponWeight.getWeaponWeight(is), rightClick))
			{
				IExPPlayer.of(player).setCurrentSpecialAttack(attack.wrap(), true);
				VoidNetwork.sendDataToClient(ExPPackets.SPECIAL_ATTACK, tag, player);
			}
		}
		catch (Exception ex)
		{
			ExPMisc.modLogger.log(LogLevel.Error, "Malformed client combat packet! %s", ex, tag);
		}
	}
}

package v0id.exp.combat;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.net.ExPNetwork;

import java.util.UUID;

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
                ExPNetwork.sendSpecialAttackSync(tag, player);
			}
		}
		catch (Exception ex)
		{
			ExPMisc.modLogger.error("Malformed client combat packet!", ex);
		}
	}
}

package v0id.exp.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import v0id.api.exp.combat.EnumWeaponWeight;
import v0id.api.exp.combat.SpecialAttack;
import v0id.api.exp.combat.SpecialAttack.IExecuteCondition;
import v0id.api.exp.combat.WeaponType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.util.NBTChain;
import v0id.exp.net.ExPNetwork;

public class ClientCombatHandler
{
	public static void handle(NBTTagCompound tag)
	{
		try
		{
			SpecialAttack attack = SpecialAttack.registry.get(tag.getString("attackID"));
			IExPPlayer.of(Minecraft.getMinecraft().player).setCurrentSpecialAttack(attack.wrap(), true);
		}
		catch (Exception ex)
		{
			ExPMisc.modLogger.error("Malformed server combat packet!", ex);
		}
	}
	
	public static void processClick(EntityPlayer player, boolean rightClick)
	{
		if (!player.getHeldItemMainhand().isEmpty() || (!player.getHeldItemOffhand().isEmpty() && rightClick))
		{
			ItemStack is = !player.getHeldItemMainhand().isEmpty() ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
			for (SpecialAttack attack : SpecialAttack.sortedData)
			{
				if (attack.canExecute(player, WeaponType.getType(is), EnumWeaponWeight.getWeaponWeight(is), rightClick))
				{
					boolean b = true;
					for (IExecuteCondition condition : attack.executeConditions)
					{
						if (!condition.isMet())
						{
							b = false;
							break;
						}
					}
					
					if (!b)
					{
						continue;
					}

					ExPNetwork.sendSpecialAttackClick(NBTChain.startChain().withString("uuid", player.getPersistentID().toString()).withString("attackID", attack.id).withBool("rightClick", rightClick).endChain());
					return;
				}
			}
		}
	}
}

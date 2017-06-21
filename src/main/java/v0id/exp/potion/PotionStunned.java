package v0id.exp.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import v0id.api.exp.data.ExPRegistryNames;

import java.util.Collections;

public class PotionStunned extends Potion implements IInitializablePotion
{
	public static final String uuid = "28339fcf-3758-4ad5-9100-2d146536239d";
	
	public PotionStunned()
	{
		super(true, 0x000000);
		this.initPotion();
	}

	@Override
	public void initPotion()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.potionStunned));
		this.setPotionName(this.getRegistryName().toString().replace(':', '.'));
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, uuid, -10, 2);
	}

	@Override
	public boolean shouldRender(PotionEffect effect)
	{
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public java.util.List<net.minecraft.item.ItemStack> getCurativeItems()
    {
		return Collections.EMPTY_LIST;
    }
}

package v0id.exp.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.data.ExPRegistryNames;

import java.util.Collections;

public class PotionStunned extends Potion implements IInitializablePotion, IPotionRegistryEntry
{
	public static final String uuid = "28339fcf-3758-4ad5-9100-2d146536239d";
	
	public PotionStunned()
	{
		super(true, 0x000000);
		this.initPotion();
	}

	@Override
	public void registerPotion(IForgeRegistry<Potion> registry)
	{
		registry.register(this);
	}

	@Override
	public void initPotion()
	{
		this.setRegistryName(ExPRegistryNames.potionStunned);
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

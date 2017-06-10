package v0id.exp.registry;

import v0id.api.exp.data.ExPPotions;
import v0id.exp.potion.PotionStunned;

public class ExPPotionRegistry extends AbstractRegistry
{
	static
	{
		ExPPotions.stunned = new PotionStunned();
	}
	
	public ExPPotionRegistry()
	{
		super();
	}
}

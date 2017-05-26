package v0id.exp.registry;

import v0id.api.exp.data.ExPWeaponAttacks;
import v0id.exp.combat.impl.PiercingDash;
import v0id.exp.combat.impl.Slash;

public class ExPWeaponAttacksRegistry extends AbstractRegistry
{
	static
	{
		ExPWeaponAttacks.piercingDash = new PiercingDash();
		ExPWeaponAttacks.slash = new Slash();
	}
	
	public ExPWeaponAttacksRegistry()
	{
		super();
	}
}

package v0id.exp.registry;

import v0id.api.exp.data.ExPWeaponAttacks;
import v0id.exp.combat.impl.DownStrike;
import v0id.exp.combat.impl.PiercingDash;
import v0id.exp.combat.impl.ShieldSlam;
import v0id.exp.combat.impl.Slash;
import v0id.exp.combat.impl.Spin;

public class ExPWeaponAttacksRegistry extends AbstractRegistry
{
	static
	{
		ExPWeaponAttacks.piercingDash = new PiercingDash();
		ExPWeaponAttacks.slash = new Slash();
		ExPWeaponAttacks.downStrike = new DownStrike();
		ExPWeaponAttacks.spin = new Spin();
		ExPWeaponAttacks.shieldSlam = new ShieldSlam();
	}
	
	public ExPWeaponAttacksRegistry()
	{
		super();
	}
}

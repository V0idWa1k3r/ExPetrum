package v0id.exp.registry;

import v0id.api.exp.data.ExPWeaponAttacks;
import v0id.exp.combat.impl.Behead;
import v0id.exp.combat.impl.DownStrike;
import v0id.exp.combat.impl.ItemThrow;
import v0id.exp.combat.impl.PiercingDash;
import v0id.exp.combat.impl.ShieldSlam;
import v0id.exp.combat.impl.Slash;
import v0id.exp.combat.impl.Spin;
import v0id.exp.combat.impl.Stab;

public class ExPWeaponAttacksRegistry extends AbstractRegistry
{
	static
	{
		ExPWeaponAttacks.piercingDash = new PiercingDash();
		ExPWeaponAttacks.slash = new Slash();
		ExPWeaponAttacks.downStrike = new DownStrike();
		ExPWeaponAttacks.spin = new Spin();
		ExPWeaponAttacks.shieldSlam = new ShieldSlam();
		ExPWeaponAttacks.behead = new Behead();
		ExPWeaponAttacks.stab = new Stab();
		ExPWeaponAttacks.itemThrow = new ItemThrow();
	}
	
	public ExPWeaponAttacksRegistry()
	{
		super();
	}
}

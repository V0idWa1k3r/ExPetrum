package v0id.api.exp.combat.condition;

import org.lwjgl.input.Mouse;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.api.exp.combat.SpecialAttack.IExecuteCondition;

@SideOnly(Side.CLIENT)
public class ExecuteConditionMouseButton implements IExecuteCondition
{
	private final int btn;
	
	public ExecuteConditionMouseButton(int button)
	{
		this.btn = button;
	}
	
	@Override
	public boolean isMet()
	{
		return (this.btn & this.getMouseMask()) != 0;
	}

	public int getMouseMask()
	{
		return ((Mouse.isButtonDown(0) ? 1 : 0) << 2) + ((Mouse.isButtonDown(1) ? 1 : 0) << 1) + (Mouse.isButtonDown(2) ? 1 : 0);
	}
}

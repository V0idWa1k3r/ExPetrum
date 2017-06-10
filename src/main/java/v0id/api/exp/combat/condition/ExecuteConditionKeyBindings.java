package v0id.api.exp.combat.condition;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.api.exp.combat.SpecialAttack.IExecuteCondition;

@SideOnly(Side.CLIENT)
public class ExecuteConditionKeyBindings implements IExecuteCondition
{
	public final List<KeyBinding> bindings = Lists.newArrayList();
	
	public ExecuteConditionKeyBindings(KeyBinding...bindings)
	{
		this.bindings.addAll(Arrays.asList(bindings));
	}
	
	@Override
	public boolean isMet()
	{
		for (KeyBinding kb : this.bindings)
		{
			if (!kb.isKeyDown())
			{
				return false;
			}
		}
		
		return true;
	}

}

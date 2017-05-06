package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPItems;
import v0id.exp.item.ItemRock;
import v0id.exp.item.ItemStick;
import v0id.exp.item.ItemToolHead;

public class ExPItemsRegistry extends AbstractRegistry
{
	public ExPItemsRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		ExPItems.rock = new ItemRock();
		ExPItems.stick = new ItemStick();
		ExPItems.toolHead = new ItemToolHead();
	}
}

package v0id.exp.registry;

import v0id.api.exp.data.ExPItems;
import v0id.exp.item.ItemRock;
import v0id.exp.item.ItemStick;
import v0id.exp.item.ItemToolHead;

public class ExPItemsRegistry extends AbstractRegistry
{
	static
	{
		ExPItems.rock = new ItemRock();
		ExPItems.stick = new ItemStick();
		ExPItems.toolHead = new ItemToolHead();
	}
	
	public ExPItemsRegistry()
	{
		super();
	}
}

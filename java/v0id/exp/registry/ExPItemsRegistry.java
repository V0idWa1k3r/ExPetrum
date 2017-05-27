package v0id.exp.registry;

import v0id.api.exp.data.ExPItems;
import v0id.exp.item.ItemFood;
import v0id.exp.item.ItemIngot;
import v0id.exp.item.ItemRock;
import v0id.exp.item.ItemSeeds;
import v0id.exp.item.ItemStick;
import v0id.exp.item.ItemToolHead;

public class ExPItemsRegistry extends AbstractRegistry
{
	static
	{
		ExPItems.rock = new ItemRock();
		ExPItems.stick = new ItemStick();
		ExPItems.toolHead = new ItemToolHead();
		ExPItems.seeds = new ItemSeeds();
		ExPItems.food = new ItemFood();
		ExPItems.ingot = new ItemIngot();
	}
	
	public ExPItemsRegistry()
	{
		super();
	}
}

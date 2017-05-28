package v0id.exp.registry;

import v0id.api.exp.data.ExPItems;
import v0id.exp.item.ItemFood;
import v0id.exp.item.ItemIngot;
import v0id.exp.item.ItemRock;
import v0id.exp.item.ItemSeeds;
import v0id.exp.item.ItemStick;
import v0id.exp.item.ItemToolHead;
import v0id.exp.item.tool.ItemAxe;
import v0id.exp.item.tool.ItemBattleaxe;
import v0id.exp.item.tool.ItemHammer;
import v0id.exp.item.tool.ItemHoe;
import v0id.exp.item.tool.ItemKnife;
import v0id.exp.item.tool.ItemPickaxe;
import v0id.exp.item.tool.ItemScythe;
import v0id.exp.item.tool.ItemShovel;
import v0id.exp.item.tool.ItemSpear;
import v0id.exp.item.tool.ItemSword;

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
		ExPItems.knife = new ItemKnife();
		ExPItems.pickaxe = new ItemPickaxe();
		ExPItems.axe = new ItemAxe();
		ExPItems.shovel = new ItemShovel();
		ExPItems.hoe = new ItemHoe();
		ExPItems.sword = new ItemSword();
		ExPItems.scythe = new ItemScythe();
		ExPItems.battleaxe = new ItemBattleaxe();
		ExPItems.hammer = new ItemHammer();
		ExPItems.spear = new ItemSpear();
	}
	
	public ExPItemsRegistry()
	{
		super();
	}
}

package v0id.exp.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.block.EnumShrubState;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.block.*;
import v0id.exp.block.fluid.*;
import v0id.exp.block.plant.*;
import v0id.exp.block.tree.BlockLeaf;
import v0id.exp.block.tree.BlockLog;

import java.util.Arrays;
import java.util.List;

public class ExPBlocksRegistry extends AbstractRegistry
{
	public static ExPBlocksRegistry instance;
	public static List<Block> registryEntries;
	
	public ExPBlocksRegistry()
	{
		super();
		instance = this;
	}

	public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();
        registryEntries = Arrays.asList(
                new BlockStone(),
                new BlockSoil(),
                new BlockGrass(),
                new BlockGrass.Dry(),
                new BlockGrass.Dead(),
                new BlockSaltWater(),
                new BlockFreshWater(),
                new BlockLava(),
                new BlockWaterLily(),
                new BlockCattail(),
                new BlockVegetation(),
                new BlockSand(),
                new BlockSeaweed(),
                new BlockCoralRock(),
                new BlockCoralPlant(),
                new BlockLog(0),
                new BlockLog(1),
                new BlockLog(2),
                new BlockLog(3),
                new BlockLog(4),
                new BlockLog(5),
                new BlockLog(6),
                new BlockLog(7),
                new BlockLog(8),
                new BlockLog.Decorative(0),
                new BlockLog.Decorative(1),
                new BlockLog.Decorative(2),
                new BlockLog.Decorative(3),
                new BlockLog.Decorative(4),
                new BlockLog.Decorative(5),
                new BlockLog.Decorative(6),
                new BlockLog.Decorative(7),
                new BlockLog.Decorative(8),
                new BlockLeaf(0),
                new BlockLeaf(1),
                new BlockLeaf(2),
                new BlockLeaf(3),
                new BlockLeaf(4),
                new BlockLeaf(5),
                new BlockLeaf(6),
                new BlockLeaf(7),
                new BlockLeaf(8),
                new BlockOre(),
                new BlockPebble(),
                new BlockBoulder(),
                new BlockBoulderOre(),
                new BlockOil(),
                new BlockShrub(EnumShrubState.NORMAL),
                new BlockShrub(EnumShrubState.BLOOMING),
                new BlockShrub(EnumShrubState.AUTUMN),
                new BlockShrub(EnumShrubState.DEAD),
                new BlockBerryBush(EnumShrubState.NORMAL),
                new BlockBerryBush(EnumShrubState.BLOOMING),
                new BlockBerryBush(EnumShrubState.AUTUMN),
                new BlockBerryBush(EnumShrubState.DEAD),
                new BlockSnow(),
                new BlockIce(),
                new BlockWorkedBoulder(),
                new BlockCrop(),
                new BlockFarmland(),
                new BlockFruit(),
                new BlockGenericShrubbery(),
                new BlockFlint(),
                new BlockClay(),
                new BlockDecoratedStone(BlockDecoratedStone.EnumDecorationType.TILE),
                new BlockDecoratedStone(BlockDecoratedStone.EnumDecorationType.BRICK),
                new BlockDecoratedStone(BlockDecoratedStone.EnumDecorationType.SMALL_BRICK),
                new BlockCampfire(),
                new BlockPotteryStation(),
                new BlockPottery(),
                new BlockLogPile(),
                new BlockCharcoal(),
                new BlockKaolin(),
                new BlockPlanks(0),
                new BlockPlanks(1),
                new BlockPlanks(2),
                new BlockCraftingTable(0),
                new BlockCraftingTable(1),
                new BlockCraftingTable(2),
                new BlockCrate(),
                new BlockForge(),
                new BlockCobblestone(),
                new BlockQuern(),
                new BlockTrough(),
                new BlockAnvil(),
                new BlockCrucible(),
                new BlockHay(),
                new BlockNestingBox(),
                new BlockBarrel(),
                new BlockScrapingRack(),
                new BlockBellows(),
                new BlockSpinningWheel(),
                new BlockMoltenMetal(),
                new BlockBloomery(),
                new BlockPress(),
                new BlockChest(),
                new BlockShaft(),
                new BlockGearbox(),
                new BlockWaterWheel(),
                new BlockStructurePiece(),
                new BlockSplitterGearbox(),
                new BlockMechanicalQuern(),
                new BlockWindmill(),
                new BlockMechanicalBellows(),
                new BlockSaw()
        );

        registryEntries.forEach(registry::register);
    }

    public void registerItemBlocks(RegistryEvent.Register<Item> event)
    {
        registryEntries.stream().filter(e -> e instanceof IItemBlockProvider).map(e -> (IItemBlockProvider)e).forEach(e -> e.registerItem(event.getRegistry()));
    }

    @Override
    public void init(FMLInitializationEvent evt)
    {
        ExPBlocks.logs = new Block[]{ ExPBlocks.log0, ExPBlocks.log1, ExPBlocks.log2, ExPBlocks.log3, ExPBlocks.log4, ExPBlocks.log5, ExPBlocks.log6, ExPBlocks.log7, ExPBlocks.log8 };
        ExPBlocks.logsDeco = new Block[]{ ExPBlocks.logDeco0, ExPBlocks.logDeco1, ExPBlocks.logDeco2, ExPBlocks.logDeco3, ExPBlocks.logDeco4, ExPBlocks.logDeco5, ExPBlocks.logDeco6, ExPBlocks.logDeco7, ExPBlocks.logDeco8 };
        ExPBlocks.leaves = new Block[]{ ExPBlocks.leaf0, ExPBlocks.leaf1, ExPBlocks.leaf2, ExPBlocks.leaf3, ExPBlocks.leaf4, ExPBlocks.leaf5, ExPBlocks.leaf6, ExPBlocks.leaf7, ExPBlocks.leaf8 };
        ExPBlocks.shrubs = new Block[]{ ExPBlocks.shrubNormal, ExPBlocks.shrubBlooming, ExPBlocks.shrubAutumn, ExPBlocks.shrubDead };
        ExPBlocks.berryBushes = new Block[]{ ExPBlocks.berryBushNormal, ExPBlocks.berryBushBerries, ExPBlocks.berryBushAutumn, ExPBlocks.berryBushDead };
        ExPBlocks.planks = new Block[]{ ExPBlocks.planks0, ExPBlocks.planks1, ExPBlocks.planks2 };
        ExPBlocks.craftingTables = new Block[]{ ExPBlocks.craftingTable0, ExPBlocks.craftingTable1, ExPBlocks.craftingTable2 };
    }
}

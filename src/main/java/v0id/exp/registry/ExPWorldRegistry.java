package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.gen.TreeGenerators;
import v0id.exp.world.gen.WorldTypeExP;
import v0id.exp.world.gen.tree.TreeGenImpl;
import v0id.exp.world.gen.tree.TreeGenerator;

public class ExPWorldRegistry extends AbstractRegistry
{
	public ExPWorldRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		ExPMisc.worldTypeExP = new WorldTypeExP("expworld");
		TreeGenerators.generators.put(EnumTreeType.KALOPANAX, new TreeGenerator(EnumTreeType.KALOPANAX).defaultTrunk(5, 8).sphereLeaves());
		TreeGenerators.generators.put(EnumTreeType.BIRCH, new TreeGenerator(EnumTreeType.BIRCH).defaultTrunk(7, 12).withLeaves(TreeGenImpl::leavesGenBirchImpl));
		TreeGenerators.generators.put(EnumTreeType.ACACIA, new TreeGenerator(EnumTreeType.ACACIA).defaultTrunk(5, 8).withLeaves(TreeGenImpl::leavesGenAcaciaImpl));
		TreeGenerators.generators.put(EnumTreeType.CHESTNUT, new TreeGenerator(EnumTreeType.CHESTNUT).defaultTrunk(4, 7).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.OAK, new TreeGenerator(EnumTreeType.OAK).defaultTrunk(4, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.HICKORY, new TreeGenerator(EnumTreeType.HICKORY).defaultTrunk(6, 14).withLeaves(TreeGenImpl::leavesGenHickoryImpl));
		TreeGenerators.generators.put(EnumTreeType.BAOBAB, new TreeGenerator(EnumTreeType.BAOBAB).defaultTrunk(9, 12).withLeaves(TreeGenImpl::leavesGenBaobabImpl));
		TreeGenerators.generators.put(EnumTreeType.KAPOK, new TreeGenerator(EnumTreeType.KAPOK).defaultTrunk(6,10).sphereLeaves());
		TreeGenerators.generators.put(EnumTreeType.EUCALYPTUS, new TreeGenerator(EnumTreeType.EUCALYPTUS).defaultTrunk(8, 16).withLeaves(TreeGenImpl::leavesGenEucalyptusImpl));
		TreeGenerators.generators.put(EnumTreeType.ASH, new TreeGenerator(EnumTreeType.ASH).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.WILLOW, new TreeGenerator(EnumTreeType.WILLOW).defaultTrunk(5, 8).withLeaves(TreeGenImpl::leavesGenWillowImpl));
		TreeGenerators.generators.put(EnumTreeType.MAPLE, new TreeGenerator(EnumTreeType.MAPLE).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.REDWOOD, new TreeGenerator(EnumTreeType.REDWOOD).defaultTrunk(8, 12).evergreenLeaves());
		TreeGenerators.generators.put(EnumTreeType.FIR, new TreeGenerator(EnumTreeType.FIR).defaultTrunk(6, 9).evergreenLeaves());
		TreeGenerators.generators.put(EnumTreeType.VIBURNUM, new TreeGenerator(EnumTreeType.VIBURNUM).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.CEDAR, new TreeGenerator(EnumTreeType.CEDAR).defaultTrunk(7, 13).evergreenLeaves());
		TreeGenerators.generators.put(EnumTreeType.SPRUCE, new TreeGenerator(EnumTreeType.SPRUCE).defaultTrunk(8, 16).evergreenLeaves());
		TreeGenerators.generators.put(EnumTreeType.PINE, new TreeGenerator(EnumTreeType.PINE).defaultTrunk(7, 12).evergreenLeaves());
		TreeGenerators.generators.put(EnumTreeType.ELM, new TreeGenerator(EnumTreeType.ELM).defaultTrunk(5, 11).sphereLeaves());
		TreeGenerators.generators.put(EnumTreeType.PALM, new TreeGenerator(EnumTreeType.PALM).defaultTrunk(7, 13).withLeaves(TreeGenImpl::leavesGenPalmImpl));
		TreeGenerators.generators.put(EnumTreeType.TEAK, new TreeGenerator(EnumTreeType.TEAK).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.ASPEN, new TreeGenerator(EnumTreeType.ASPEN).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.ROWAN, new TreeGenerator(EnumTreeType.ROWAN).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.MANGROVE, new TreeGenerator(EnumTreeType.MANGROVE).defaultTrunk(9, 14).sphereLeaves());
		TreeGenerators.generators.put(EnumTreeType.TUPELO, new TreeGenerator(EnumTreeType.TUPELO).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.PARROTIA, new TreeGenerator(EnumTreeType.PARROTIA).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.SWEETGUM, new TreeGenerator(EnumTreeType.SWEETGUM).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.JACKWOOD, new TreeGenerator(EnumTreeType.JACKWOOD).defaultTrunk(5, 8).defaultLeaves());
		TreeGenerators.generators.put(EnumTreeType.TSUGA, new TreeGenerator(EnumTreeType.TSUGA).defaultTrunk(8, 12).evergreenLeaves());
		TreeGenerators.generators.put(EnumTreeType.KOELREUTERIA, new TreeGenerator(EnumTreeType.KOELREUTERIA).defaultTrunk(5, 8).defaultLeaves());
	}
	
}

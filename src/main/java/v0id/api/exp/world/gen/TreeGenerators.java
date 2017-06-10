package v0id.api.exp.world.gen;

import java.util.Map;
import com.google.common.collect.Maps;
import v0id.api.exp.block.EnumTreeType;

public class TreeGenerators
{
	public static final Map<EnumTreeType, ITreeGenerator> generators = Maps.newEnumMap(EnumTreeType.class);
}

package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.ExPetrum;
import v0id.exp.entity.EntityFallingTree;
import v0id.exp.entity.EntityGravFallingBlock;
import v0id.exp.entity.EntityThrownWeapon;

public class ExPEntityRegistry extends AbstractRegistry
{
	public ExPEntityRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		EntityRegistry.registerModEntity(ExPRegistryNames.entityGravFallingBlock, EntityGravFallingBlock.class, ExPRegistryNames.entityGravFallingBlock.toString().replace(':', '.'), 0, ExPetrum.instance, 32, 1, true);
		EntityRegistry.registerModEntity(ExPRegistryNames.entityFallingTree, EntityFallingTree.class, ExPRegistryNames.entityFallingTree.toString().replace(':', '.'), 1, ExPetrum.instance, 32, 1, true);
		EntityRegistry.registerModEntity(ExPRegistryNames.entityThrownWeapon, EntityThrownWeapon.class, ExPRegistryNames.entityThrownWeapon.toString().replace(':', '.'), 2, ExPetrum.instance, 32, 1, true);
		super.preInit(evt);
	}
}

package v0id.exp.registry;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.EntityFallingTree;
import v0id.exp.entity.EntityGravFallingBlock;
import v0id.exp.entity.EntityThrownWeapon;
import v0id.exp.entity.impl.*;

public class ExPEntityRegistry extends AbstractRegistry
{
    public static ExPEntityRegistry instance;

    public ExPEntityRegistry()
    {
        super();
        instance = this;
    }

	public void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create().entity(EntityGravFallingBlock.class).name(ExPRegistryNames.entityGravFallingBlock.toString().replace(':', '.')).id(ExPRegistryNames.entityGravFallingBlock, 0).tracker(32, 1, true).build(),
                EntityEntryBuilder.create().entity(EntityFallingTree.class).name(ExPRegistryNames.entityFallingTree.toString().replace(':', '.')).id(ExPRegistryNames.entityFallingTree, 1).tracker(32, 1, true).build(),
                EntityEntryBuilder.create().entity(EntityThrownWeapon.class).name(ExPRegistryNames.entityThrownWeapon.toString().replace(':', '.')).id(ExPRegistryNames.entityThrownWeapon, 2).tracker(32, 1, true).build(),
                EntityEntryBuilder.create().entity(Chicken.class).name(ExPRegistryNames.entityChicken.toString().replace(':', '.')).id(ExPRegistryNames.entityChicken, 3).tracker(64, 1, true).egg(0xffffff, 0xff0000).build(),
                EntityEntryBuilder.create().entity(Cow.class).name(ExPRegistryNames.entityCow.toString().replace(':', '.')).id(ExPRegistryNames.entityCow, 4).tracker(64, 1, true).egg(0xffffff, 0x331100).build(),
                EntityEntryBuilder.create().entity(Sheep.class).name(ExPRegistryNames.entitySheep.toString().replace(':', '.')).id(ExPRegistryNames.entitySheep, 5).tracker(64, 1, true).egg(0xffffff, 0x888888).build(),
                EntityEntryBuilder.create().entity(Pig.class).name(ExPRegistryNames.entityPig.toString().replace(':', '.')).id(ExPRegistryNames.entityPig, 6).tracker(64, 1, true).egg(0xffffff, 0xaa0000).build(),
                EntityEntryBuilder.create().entity(Wolf.class).name(ExPRegistryNames.entityWolf.toString().replace(':', '.')).id(ExPRegistryNames.entityWolf, 7).tracker(64, 1, true).egg(0xffffff, 0x000000).build()
        );
    }
}

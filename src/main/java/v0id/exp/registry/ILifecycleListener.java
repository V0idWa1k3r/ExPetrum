package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by V0idWa1k3r on 10-Jun-17.
 */
public interface ILifecycleListener
{
    default void preInit(FMLPreInitializationEvent evt)
    {

    }

    default void init(FMLInitializationEvent evt)
    {

    }

    default void postInit(FMLPostInitializationEvent evt)
    {

    }
}

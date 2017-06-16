package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPMisc;
import v0id.exp.settings.SettingsManager;

import java.io.File;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public class ExPConfigRegistry extends AbstractRegistry
{
    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        ExPMisc.rootSettingsManager = new SettingsManager(new File(new File(evt.getModConfigurationDirectory(), "ExPetrum"), "Settings"));
    }
}

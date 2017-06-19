package v0id.exp.settings.impl;

import v0id.api.exp.settings.SyncableSettings;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
@SyncableSettings("flags")
public class SettingsFlags extends WritableSettings
{
    public static transient SettingsFlags instance;

    public boolean enableCustomInventory = true;
    public boolean enableNewCombat = true;
    public String __enableInvasiveChangesComment = "Should ExPetrum use reflection to forcibly change some settings/data to make the client's experience better? This includes setting the world type to ExPetrum when creating a new world by default.";
    public boolean enableInvasiveChanges = true;

    public SettingsFlags()
    {
        super();
        instance = this;
    }
}

package v0id.exp.settings.impl;

import com.google.gson.annotations.Expose;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
@SuppressWarnings("CanBeFinal")
public class SettingsClient extends WritableSettings
{
    public boolean enableCustomSky = true;
    public boolean enableCustomRain = true;
    public boolean disableFog = true;

    @Expose(serialize = false, deserialize = false)
    public static SettingsClient instance;

    public SettingsClient()
    {
        super();
        instance = this;
    }
}

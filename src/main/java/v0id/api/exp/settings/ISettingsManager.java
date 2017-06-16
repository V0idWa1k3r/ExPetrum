package v0id.api.exp.settings;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public interface ISettingsManager
{
    @Nullable
    <T>T get(String key);

    <T>void set(String key, @Nullable T value);
}

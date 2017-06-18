package v0id.exp.settings.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.settings.ISettingsManager;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public abstract class WritableSettings implements ISettingsManager
{
    public transient final Map<String, Object> underlyingExposureKVs = Maps.newHashMap();
    private transient final Map<String, Field> collectedFldData = Maps.newHashMap();
    public transient File self;
    public static transient final Gson serializer = new Gson();

    public WritableSettings()
    {
        this.init(this.getClass());
    }

    @SuppressWarnings("UnusedReturnValue")
    public WritableSettings setFile(File f)
    {
        this.self = f;
        this.collectedFldData.forEach((String s, Field fld) ->
        {
            try
            {
                this.underlyingExposureKVs.put(s, fld.get(this));
            }
            catch (IllegalAccessException ignored)
            {
                // Silence
            }
        });

        return this;
    }

    public void init(Class<? extends WritableSettings> clazz)
    {
        Arrays.stream(clazz.getDeclaredFields()).filter(f -> Modifier.isPublic(f.getModifiers())).forEach(f -> collectedFldData.put(f.getName(), f));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T get(String key)
    {
        return (T) this.underlyingExposureKVs.get(key);
    }

    @Override
    public <T> void set(String key, @Nullable T value)
    {
        T valueCurrent = this.get(key);
        if (valueCurrent != value)
        {
            this.underlyingExposureKVs.put(key, value);
            try
            {
                this.collectedFldData.get(key).set(this, value);
            }
            catch (IllegalAccessException ignored)
            {
                // Silence
            }
        }
    }

    public void saveData()
    {
        String json = serializer.toJson(this);
        try (FileOutputStream fos = new FileOutputStream(this.self))
        {
            IOUtils.write(json, fos, StandardCharsets.UTF_8);
        }
        catch (Exception ex)
        {
            ExPMisc.modLogger.log(LogLevel.Error, "Could not save settings of %s to %s!", ex, this.getClass(), this.self);
        }
    }
}

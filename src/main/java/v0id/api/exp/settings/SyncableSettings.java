package v0id.api.exp.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 * Classes that are an implementation of ISettingsManager that have this annotation present will be synced to the client as the client joins the server.
 * The class must also have a field with the following descriptor: public static %CLASSNAME% instance;
 * @author V0idWa1k3r
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SyncableSettings
{
    String value();
}

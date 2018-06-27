package v0id.core.util.cfg;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@SuppressWarnings("WeakerAccess")
@Retention(RUNTIME)
@Target(FIELD)

/*
  @author V0idWa1k3r
 * @Description
 * Put over your configuration entry to disable it's serialization.
 */
public @interface ConfigIgnore
{

}

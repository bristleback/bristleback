package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 20:20:55 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ConfigurationElement {

  String value() default "";

}

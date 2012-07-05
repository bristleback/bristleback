package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 10:34:25 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Outgoing {

  String actionClassName() default "";

  Class<?> actionClass() default Object.class;
}

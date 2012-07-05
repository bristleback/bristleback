package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-02 14:26:59 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Action {

  String name() default "";

  Serialize[] response() default {};
}

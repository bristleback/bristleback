package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 21:37:47 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AnnotatedObjectSender {

  String value() default "";

  Serialize[] serialize() default {};
}

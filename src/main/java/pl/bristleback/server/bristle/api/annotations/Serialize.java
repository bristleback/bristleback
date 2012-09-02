package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-04 16:40:46 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Serialize {

  String serializationName() default "";

  Class target() default Object.class;

  Class containerElementClass() default Object.class;

  Property[] properties() default {};

  boolean required() default false;
}

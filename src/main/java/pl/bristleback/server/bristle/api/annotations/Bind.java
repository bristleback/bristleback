package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-26 12:34:56 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Bind {

  Property[] properties() default {};

  boolean required() default false;

  /**
   * Indicates whether exceptions should return detailed information about which fields contains errors.
   * This flag is temporary disabled.
   *
   * @return true if detailed error messages should be sent for bound object.
   */
  boolean detailedErrors() default false;
}

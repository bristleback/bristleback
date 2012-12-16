package pl.bristleback.server.bristle.serialization.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be placed before each action parameter to provide validation.
 * Currently only required and skipped flags are supported. Formats and other validations will be provided in next versions.
 * <p/>
 * Created on: 2011-07-26 12:34:56 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Bind {

  Property[] properties() default {};

  /**
   * This property should be used when field is of simple type.
   *
   * @return true if this simple action parameter is required, false otherwise.
   */
  boolean required() default false;

  /**
   * Indicates whether exceptions should return detailed information about which fields contains errors.
   * This flag is temporary disabled.
   *
   * @return true if detailed error messages should be sent for bound object.
   */
  boolean detailedErrors() default false;
}

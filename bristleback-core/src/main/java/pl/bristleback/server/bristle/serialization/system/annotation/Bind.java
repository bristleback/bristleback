package pl.bristleback.server.bristle.serialization.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be placed before each action parameter to provide validation.
 * Currently only format, required and skipped flags are supported. Other validations will be provided in next versions.
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

  /**
   * Specifies format in which annotated objects will be serialized/deserialized. Note that there must be
   * {@link pl.bristleback.server.bristle.serialization.system.json.extractor.FormattingValueSerializer} bean registered
   * for the type that wants to be serialized/deserialized using specified format.
   *
   * @return format format in which objects of the specified type will be serialized/deserialized.
   */
  String format() default "";
}

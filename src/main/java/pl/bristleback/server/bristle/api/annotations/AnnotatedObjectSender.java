package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * All {@link pl.bristleback.server.bristle.message.ConditionObjectSender ConditionObjectSender}
 * fields must be marked with this annotation to be properly initialized by Bristleback Server.
 * All non default serialization operations are defined within this annotation.
 * <p/>
 * Created on: 2011-07-05 21:37:47 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AnnotatedObjectSender {

  /**
   * Defines custom serialization operations. Single sender is able to contain multiple non default serializations.
   *
   * @return non default serialization operations.
   */
  Serialize[] serialize() default {};
}

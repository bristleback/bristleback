package pl.bristleback.server.bristle.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks method as action method.
 * Both default and non default actions must be annotated to be processed by the action controller.
 * <p/>
 * Created on: 2011-10-02 14:26:59 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Action {

  /**
   * Name of this action. Names should fallow normal methods naming conventions, e.g., should start with small letter,
   * contain only alphanumeric characters, etc.
   * If custom name is not specified, action method name is used.
   * There cannot be multiple action classes with the same name.
   *
   * @return action name.
   */
  String name() default "";
}

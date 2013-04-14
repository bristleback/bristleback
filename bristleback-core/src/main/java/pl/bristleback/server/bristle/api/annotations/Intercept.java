package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.api.action.ActionInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks single action or whole action class as intercepted by the interceptors listed in annotation values.
 * Interceptors must be the valid Spring beans.
 * Both interceptor bean classes and interceptor bean names can be specified
 * (note that if bean class is provided and there is no bean of that type or more than one bean is found,
 * exception is thrown).
 * <p/>
 * Created on: 24.01.13 19:34 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Intercept {

  /**
   * Interceptor classes. If bean of any given type cannot be found or there is more than one bean defined,
   * exception is thrown.
   *
   * @return Interceptor classes.
   */
  Class<? extends ActionInterceptor>[] value() default {};

  /**
   * Spring bean names of the interceptors.
   *
   * @return Spring bean names of the interceptors.
   */
  String[] refs() default {};
}

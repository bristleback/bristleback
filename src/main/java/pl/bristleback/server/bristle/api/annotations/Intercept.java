package pl.bristleback.server.bristle.api.annotations;

import pl.bristleback.server.bristle.api.action.ActionInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * //@todo class description
 * <p/>
 * Created on: 24.01.13 19:34 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Intercept {

  Class<? extends ActionInterceptor>[] value();
}

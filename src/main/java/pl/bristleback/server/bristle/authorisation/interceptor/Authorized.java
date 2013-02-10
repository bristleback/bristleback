package pl.bristleback.server.bristle.authorisation.interceptor;

import pl.bristleback.server.bristle.api.annotations.Intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Prototype of simple Bristleback built in authorization.
 * <p/>
 * Created on: 09.02.13 18:48 <br/>
 *
 * @author Wojciech Niemiec
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Intercept(AuthorizationInterceptor.class)
public @interface Authorized {

  String[] value();
}
